package com.gyg.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String name;
    private String error;
    private long timestamp;
}
