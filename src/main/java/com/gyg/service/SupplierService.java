package com.gyg.service;

import com.gyg.dto.ActivityDto;
import com.gyg.dto.SupplierDto;
import com.gyg.entity.Activity;
import com.gyg.entity.Supplier;
import com.gyg.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    private final ActivityService activityService;

    public List<SupplierDto> AllSuppliers() {
        return supplierRepository.findAll().stream().map(this::toSupplierToDto).toList();
    }

    private SupplierDto toSupplierToDto(Supplier supplier) {
        return SupplierDto.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .zip(supplier.getZip())
                .city(supplier.getCity())
                .country(supplier.getCountry())
                //.activities(supplier.getActivities())
                .activities(toActivityDtos(supplier.getActivities()))
                .build();
    }

    private List<ActivityDto> toActivityDtos(List<Activity> activities) {

        if (activities == null) return List.of();
        return activities.stream().map(a -> ActivityDto.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .price(a.getPrice())
                        .currency(a.getCurrency())
                        .rating(a.getRating())
                        .specialOffer(a.isSpecialOffer())
                        .supplierName(a.getSupplierName())
                        .build())
                .toList();
    }

    public List<SupplierDto> searchSuppliers(String search) {
        return supplierRepository.searchSupplier(search).stream().map(this::toSupplierToDto).toList();
    }
}