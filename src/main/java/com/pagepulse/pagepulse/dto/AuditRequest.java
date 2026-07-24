package com.pagepulse.pagepulse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuditRequest {

    @NotBlank(message = "URL cannot be empty")
    private String url;

}