package com.pagepulse.pagepulse.dto;

import lombok.Data;

@Data
public class AuditResponse {

    private String url;

    private int httpStatus;

    private long responseTime;

    private String pageTitle;

    private String metaDescription;

    private int h1Count;

    private int missingAltImages;

    private int approximateWordCount;

}