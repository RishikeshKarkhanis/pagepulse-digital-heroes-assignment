package com.pagepulse.pagepulse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pagepulse.pagepulse.dto.AuditRequest;
import com.pagepulse.pagepulse.dto.AuditResponse;

class AuditServiceTest {

    private final AuditService auditService = new AuditService();

    @Test
    void shouldAuditValidWebsite() {

        AuditRequest request = new AuditRequest();

        request.setUrl("https://example.com");

        AuditResponse response = auditService.audit(request);

        assertEquals(200, response.getHttpStatus());

        assertNotNull(response.getPageTitle());

        assertTrue(response.getApproximateWordCount() > 0);

    }

    @Test
    void shouldThrowExceptionForInvalidUrl() {

        AuditRequest request = new AuditRequest();

        request.setUrl("abc");

        IllegalArgumentException exception
                = assertThrows(
                        IllegalArgumentException.class,
                        () -> auditService.audit(request));

        assertEquals(
                "Invalid URL. Please enter a valid URL.",
                exception.getMessage());

    }

    @Test
    void shouldThrowExceptionForNonHtmlResponse() {

        AuditRequest request = new AuditRequest();

        request.setUrl(
                "https://irp-cdn.multiscreensite.com/cb9165b2/files/uploaded/The+48+Laws+Of+Power.pdf");

        IllegalArgumentException exception
                = assertThrows(
                        IllegalArgumentException.class,
                        () -> auditService.audit(request));

        assertEquals(
                "The provided URL does not point to an HTML webpage.",
                exception.getMessage());

    }

}
