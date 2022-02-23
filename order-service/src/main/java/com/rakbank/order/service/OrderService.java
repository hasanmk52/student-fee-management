package com.rakbank.order.service;

import com.rakbank.commons.constants.OrderStatus;
import com.rakbank.commons.constants.PaymentStatus;
import com.rakbank.commons.dto.OrderRequestDto;
import com.rakbank.commons.dto.StudentDto;
import com.rakbank.commons.exception.ErrorCode;
import com.rakbank.commons.exception.StudentFeeManagementException;
import com.rakbank.order.entity.OrderPurchase;
import com.rakbank.order.repository.OrderRepository;
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
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private static final String STUDENT_URL = "http://localhost:8081/student-service/student";
    private List<StudentDto> students;

    private final OrderRepository orderRepository;
    private final OrderStatusPublisher orderStatusPublisher;
    private final RestTemplate restTemplate;

    @Transactional
    public OrderPurchase createOrder(OrderRequestDto orderRequestDto) {
        //First validate if student exists and is a valid one
        StudentDto studentDto = getStudent(orderRequestDto.getStudentId());
        if(studentDto == null) {
            throw new StudentFeeManagementException(ErrorCode.BAD_DATA, "Cannot create Order Request as Student not found for Student Id = %s", orderRequestDto.getStudentId());
        }
        validateForDuplicateOrder(orderRequestDto);
        OrderPurchase order = convertDtoToEntity(orderRequestDto);
        order.setId(UUID.randomUUID());
        order = orderRepository.save(order);
        log.info("Created order for student id={} with order id = {}", order.getStudentId(), order.getId());

        orderRequestDto.setOrderId(order.getId().toString());
        //produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        return order;
    }

    public Optional<OrderPurchase> find(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId));
    }

    public List<OrderPurchase> getAllOrders(){
        return orderRepository.findAll();
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

    @Retry(name = "orderService", fallbackMethod = "localCacheStudent")
    public StudentDto getStudent(String studentId) {
        try {
            log.info("Fetching student..");
            return restTemplate.getForObject(STUDENT_URL + "/" + studentId, StudentDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public StudentDto localCacheStudent(String studentId, ResourceAccessException ex) {
        log.info("Returning student={} info from cache", studentId);
        return students.stream()
                .filter(studentDto -> studentId.equals(studentDto.getStudentId()))
                .findFirst().get();
    }

    private OrderPurchase convertDtoToEntity(OrderRequestDto dto) {
        return new OrderPurchase(dto.getStudentId(),
                dto.getStudentGrade(),
                dto.getAmount(),
                OrderStatus.ORDER_CREATED,
                PaymentStatus.PAYMENT_PENDING);
    }

    //Reject request if order already created for student id and student grade
    private void validateForDuplicateOrder(OrderRequestDto orderRequestDto) {
        orderRepository.findOrderPurchaseByStudentIdAndStudentGrade(orderRequestDto.getStudentId(), orderRequestDto.getStudentGrade())
                .ifPresent(op -> {
                    if(op.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
                        throw new StudentFeeManagementException(ErrorCode.NOT_ALLOWED, "An order has already been created for student = %s and is in process..", orderRequestDto.getStudentId());
                    }
                });
    }
}
