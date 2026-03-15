package com.gyg.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SupplierDto {
    private long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;
    private List<ActivityDto> activities;
}
