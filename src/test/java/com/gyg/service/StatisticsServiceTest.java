package com.gyg.service;

import com.gyg.repository.StatisticsRepository;
import com.gyg.dto.SupplierStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatisticsServiceTest {

    private StatisticsRepository statisticsRepository;
    private StatisticsService statisticsService;

    @BeforeEach
    void setup() {
        statisticsRepository = mock(StatisticsRepository.class);
        statisticsService = new StatisticsService(statisticsRepository);
    }

    @Test
    void testGetSupplierStats_returnsList() {
        var stat = mock(SupplierStats.class);
        when(statisticsRepository.getSupplierStats()).thenReturn(List.of(stat));

        var result = statisticsService.getSupplierStats();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testGetSupplierStats_emptyList() {
        when(statisticsRepository.getSupplierStats()).thenReturn(List.of());

        var result = statisticsService.getSupplierStats();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSupplierStats_delegatesToRepository() {
        when(statisticsRepository.getSupplierStats()).thenReturn(List.of());

        statisticsService.getSupplierStats();

        verify(statisticsRepository).getSupplierStats();
    }
}