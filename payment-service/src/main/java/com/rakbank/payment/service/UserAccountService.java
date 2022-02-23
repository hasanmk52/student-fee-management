package com.rakbank.payment.service;

import com.rakbank.commons.dto.StudentDto;
import com.rakbank.commons.dto.UserAccountDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.payment.entity.UserAccount;
import com.rakbank.payment.repository.UserAccountRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.management.timer.Timer;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserAccountService {

    private static final String STUDENT_URL = "http://localhost:8081/student-service/student";
    private List<StudentDto> students;

    private final UserAccountRepository userAccountRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public UserAccount createUserAccount(UserAccountDto userAccountDto) {
        //Assuming student is user, so validate if student exists and fetch info
        StudentDto studentDto = getStudent(userAccountDto.getUserId());
        if(studentDto == null) {
           throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot create user account as Student not found for Student Id = %s", userAccountDto.getUserId());
        }
        UserAccount userAccount = new UserAccount(studentDto.getStudentId(),
                studentDto.getFullName(),
                userAccountDto.getCardNumber(),
                userAccountDto.getCardType(),
                userAccountDto.getBalance());
        log.info("Created User Account={} successfully!", userAccount);
        return userAccountRepository.save(userAccount);
    }

    public List<UserAccount> getAllUserAccounts(){
        return userAccountRepository.findAll();
    }

    @Scheduled(initialDelay = 1000, fixedRate = Timer.ONE_HOUR)
    @Cacheable(value = "students", unless = "#result == null")
    public void getAllStudents() {
        try {
            ResponseEntity<List<StudentDto>> responseEntity = restTemplate.exchange(
                    STUDENT_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StudentDto>>(){});
            log.info("Fetched students data");
            students = responseEntity.getBody();
        } catch (HttpServerErrorException | ResourceAccessException e) {
            log.error("Error sending request to student-service, retry later. {}", ExceptionUtils.getRootCauseMessage(e));
            students = new ArrayList<>();
        }
    }

    //Implement resilience retry, fetch from cached data if retry attempts are exhausted
    @Retry(name = "studentRetry", fallbackMethod = "localCacheStudent")
    public StudentDto getStudent(String studentId) {
        try {
            log.info("Fetching student..");
            return restTemplate.getForObject(STUDENT_URL + "/" + studentId, StudentDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    private StudentDto localCacheStudent(String studentId, ResourceAccessException re) {
        log.info("Returning student={} info from cache", studentId);
        return students.stream()
                .filter(studentDto -> studentId.equals(studentDto.getStudentId()))
                .findFirst().get();
    }
}
