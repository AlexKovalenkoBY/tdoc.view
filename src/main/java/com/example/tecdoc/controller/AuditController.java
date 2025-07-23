package com.example.tecdoc.controller;

import com.example.tecdoc.model.AuditLog;
import com.example.tecdoc.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditLogRepository auditLogRepository;
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuditLog>> getProductAuditLogs(@PathVariable Long productId) {
        List<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityId("Product", productId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLog>> getUserAuditLogs(@PathVariable String username) {
        List<AuditLog> logs = auditLogRepository.findByUserUsername(username);
        return ResponseEntity.ok(logs);
    }
}