package com.gyg.controller;

import com.gyg.dto.ActivityDto;
import com.gyg.error.ActivityNotFoundException;
import com.gyg.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    /*@GetMapping("/activities")
    public ResponseEntity<List<ActivityDto>> getAllActivities() {
        return ResponseEntity.ok(activityService.getActivities());
    }*/

    @GetMapping("/activities")
    public ResponseEntity<Page<ActivityDto>> getActivities(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "1") int size) {
        return ResponseEntity.ok(activityService.getActivities(page, Math.min(size, 5)));
    }

    @GetMapping("/activities/{id}")
    public ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id) throws ActivityNotFoundException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id cant be negative");
        }
        return ResponseEntity.ok(activityService.getActivity(id));
    }

    @GetMapping("/activities/search/{search}")
    public ResponseEntity<List<ActivityDto>> getSearchActivities(@PathVariable String search) {
        return ResponseEntity.ok(activityService.getSearchActivities(search));
    }


}
