package com.rakbank.order.service;

import com.rakbank.commons.dto.GradeFeeDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradeFeeService {

    private static final Map<Integer, BigDecimal> GRADE_FEE_MAP = createMap();

    public List<GradeFeeDto> getGradeFees() {
        return GRADE_FEE_MAP.entrySet()
                .stream()
                .map(e -> new GradeFeeDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private static Map<Integer, BigDecimal> createMap() {
        Map<Integer, BigDecimal> result = new HashMap<>();
        result.put(1, new BigDecimal(100.0));
        result.put(2, new BigDecimal(200.0));
        result.put(3, new BigDecimal(300.0));
        result.put(4, new BigDecimal(400.0));
        result.put(5, new BigDecimal(500.0));
        result.put(6, new BigDecimal(600.0));
        result.put(7, new BigDecimal(700.0));
        result.put(8, new BigDecimal(800.0));
        result.put(9, new BigDecimal(900.0));
        result.put(10, new BigDecimal(1000.0));
        return Collections.unmodifiableMap(result);
    }
}
