package com.gyg.controller;

import com.gyg.dto.SupplierStats;
import com.gyg.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/stats/suppliers")
    public ResponseEntity<List<SupplierStats>> supplierStats() {
        return ResponseEntity.ok(statisticsService.getSupplierStats());
    }
}
