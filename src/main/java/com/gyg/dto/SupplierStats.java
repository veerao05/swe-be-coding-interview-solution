package com.gyg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierStats {


    private Long supplierId;
    private String supplierName;
    private Long activityCount;
    private Double averageRating;
    private Double totalRevenue;
}
