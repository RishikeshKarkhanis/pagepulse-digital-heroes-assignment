package com.pagepulse.pagepulse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pagepulse.pagepulse.dto.AuditRequest;
import com.pagepulse.pagepulse.dto.AuditResponse;
import com.pagepulse.pagepulse.service.AuditService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/audit")
    public String audit(@ModelAttribute AuditRequest request, Model model) {

        AuditResponse response = auditService.audit(request);

        model.addAttribute("response", response);

        return "result";
    }
}