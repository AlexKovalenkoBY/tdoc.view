package com.example.tecdoc.service;

import com.example.tecdoc.model.AuditLog;
import com.example.tecdoc.model.Product;
import com.example.tecdoc.model.User;
import com.example.tecdoc.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    
    public void logProductAction(AuditLog.ActionType actionType, Product product) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setEntityType("Product");
        log.setEntityId(product.getId());
        log.setUser(currentUser);
        
        try {
            if (actionType == AuditLog.ActionType.CREATE) {
                log.setNewValue(objectMapper.writeValueAsString(product));
            } else if (actionType == AuditLog.ActionType.DELETE) {
                log.setOldValue(objectMapper.writeValueAsString(product));
            }
        } catch (JsonProcessingException e) {
            log.setNewValue("Error serializing product");
        }
        
        auditLogRepository.save(log);
    }
    
    public void logProductUpdate(Product oldProduct, Product newProduct) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        AuditLog log = new AuditLog();
        log.setActionType(AuditLog.ActionType.UPDATE);
        log.setEntityType("Product");
        log.setEntityId(newProduct.getId());
        log.setUser(currentUser);
        
        try {
            log.setOldValue(objectMapper.writeValueAsString(oldProduct));
            log.setNewValue(objectMapper.writeValueAsString(newProduct));
        } catch (JsonProcessingException e) {
            log.setOldValue("Error serializing old product");
            log.setNewValue("Error serializing new product");
        }
        
        auditLogRepository.save(log);
    }
}