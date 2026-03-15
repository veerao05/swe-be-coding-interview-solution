package com.gyg.service;

import com.gyg.dto.ActivityDto;
import com.gyg.entity.Activity;
import com.gyg.error.ActivityNotFoundException;
import com.gyg.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityDto> getActivities() {
        List<Activity> activities = activityRepository.findAllWithSuppliers();
        return activities.stream().map(this::toActivityToDto).toList();

        /*activities.stream().forEach(activity -> {
            result.add(ActivityDto.builder()
                    .id(activity.getId())
                    .title(activity.getTitle())
                    .price(activity.getPrice())
                    .currency(activity.getCurrency())
                    .rating(activity.getRating())
                    .specialOffer(activity.isSpecialOffer())
                    .supplierName(Objects.isNull(activity.getSupplier()) ? "" : activity.getSupplier().getName())
                    .build());
        });
        return result;*/

    }

    @Transactional(readOnly = true)
    public Page<ActivityDto> getActivities(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Page must not be negative");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than zero");
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return activityRepository.findAll(pageable).map(this::toActivityToDto);
    }

    private ActivityDto toActivityToDto(Activity activity) {
        return ActivityDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .price(activity.getPrice())
                .currency(activity.getCurrency())
                .rating(activity.getRating())
                .specialOffer(activity.isSpecialOffer())
                .supplierName(activity.getSupplierName())
                .build();

    }

    /*String getSupplierName(Activity activity) {
        //return activity.getSupplier() != null ? activity.getSupplier().getName() : "";
        return Optional.ofNullable(activity.getSupplier()).map(Supplier::getName).orElse("");

    }*/

    public ActivityDto getActivity(Long activityId) throws ActivityNotFoundException {

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new ActivityNotFoundException("Activity Not Found"));
        return toActivityToDto(activity);

       /* List<Activity> activities = activityRepository.findAll();
        List<ActivityDto> result = new ArrayList<>();
        activities.stream().filter(activity -> activityId.equals(activity.getId())).forEach(activity -> {
            result.add(ActivityDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .price(activity.getPrice())
                .currency(activity.getCurrency())
                .rating(activity.getRating())
                .specialOffer(activity.isSpecialOffer())
                .supplierName(activity.getSupplier().getName())
                .build());
        });
        return result.get(0);*/
    }

    public List<ActivityDto> getSearchActivities(String search) {

        List<Activity> activities = activityRepository.findTitle(search.trim());
        return activities.stream().map(this::toActivityToDto).toList();
    }
}
