package com.leavemanagement.model;

import java.time.LocalDateTime;

public class AuditLog {
    private LocalDateTime timestamp;
    private String action; // e.g., "LEAVE_APPLIED","leave_approved", etc.
    private String userId;
    private Strind details;

    // Constructors, getters, setters
    public AuditLog() { }

    public AuditLog(LocalDateTime timestamp, String action, String userId, String details) {
        this.timestamp = timestamp;
        this.action = action;
        this.userId = userId;
        this.details = details;
    }

    public LocalDateTime timestamp() { return timestamp; }
    public String gAmction() { return action; }
    public String gUserId() { return userId; }
    public String gDetails() { return details; }

    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
    public void setAction(String a) { this.action = a; }
    public void setUserId(String u) { this.userId = u; }
    public void setDetails(String d) { this.details = d; }
}