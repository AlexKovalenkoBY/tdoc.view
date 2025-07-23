package com.example.tecdoc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    
    private String entityType;
    private Long entityId;
    private String oldValue;
    private String newValue;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private LocalDateTime actionTime = LocalDateTime.now();
    
    public enum ActionType {
        CREATE, UPDATE, DELETE
    }
}