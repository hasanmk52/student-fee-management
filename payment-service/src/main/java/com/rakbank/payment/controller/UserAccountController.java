package com.rakbank.payment.controller;

import com.rakbank.commons.dto.UserAccountDto;
import com.rakbank.payment.entity.UserAccount;
import com.rakbank.payment.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-account")
@AllArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UserAccountDto> getUserAccounts(){
        return userAccountService.getAllUserAccounts().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Create User Account", description = "Creates a new User Account. Here cardType acceptable values are either MASTERCARD or VISA and userId is studentId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Account Created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserAccountDto.class))}),
            @ApiResponse(responseCode = "400", description = "User Account bad data", content = @Content)})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserAccountDto> createUserAccount(@Valid @RequestBody UserAccountDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntityToDto(userAccountService.createUserAccount(dto)));
    }

    private UserAccountDto convertEntityToDto(UserAccount entity) {
        UserAccountDto userAccountDto = new UserAccountDto(entity.getId().toString(),
                entity.getUserId(),
                entity.getCardNumber(),
                entity.getCardType(),
                entity.getBalance());
        return userAccountDto;
    }

}
