package com.gyg.service;

import com.gyg.repository.StatisticsRepository;
import com.gyg.dto.SupplierStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public List<SupplierStats> getSupplierStats() {
        return statisticsRepository.getSupplierStats();
    }
}
