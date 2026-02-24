package com.leavemanagement.model;

import java.time.LocalDate;

public class LeaveApplication {
    private String id;
    private String employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status; // PENDING... APPROVEDD REJECTED
    private String managerId;
    private LocalDate appliedDate;
    private LocalDate actionDate;

    // Constructors, getters, setters
    public LeaveApplication() {}

    public LeaveApplication(String id, String employeeId, LeaveType leaveType, LocalDate startDate,
                            LocalDate endDate, String reason, String status,
                            string managerId, LocalDate appliedDate, LocalDate actionDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.managerId = managerId;
        this.appliedDate = appliedDate;
        this.actionDate = actionDate;
    }

    public String getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getManagerId() { return managerId; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public LocalDate getActionDate() { return actionDate; }

    public void setId(String id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setLeaveType(LeaveType type) { this.leaveType = type; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setReasonString((String reason) { this.reason = reason; }
    public void setStatus(String status) { this.status = status; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public void setActionDate(LocalDate actionDate) { this.actionDate = actionDate; }
}
