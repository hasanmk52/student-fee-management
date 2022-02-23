package com.rakbank.order.controller;

import com.rakbank.commons.dto.GradeFeeDto;
import com.rakbank.order.service.GradeFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grade-fee")
@AllArgsConstructor
public class GradeFeeController {

    private final GradeFeeService gradeFeeService;

    @Operation(summary = "Get Fees for Grades", description = "Get Fees for Grades (Just some lookup data to create an a fee order)", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched grade fees",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GradeFeeDto.class))}),
            @ApiResponse(responseCode = "404", description = "Grade fees not found", content = @Content)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GradeFeeDto> getGradeFees(){
        return gradeFeeService.getGradeFees();
    }

}
