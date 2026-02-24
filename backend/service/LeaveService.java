package com.leavemanagement.service;

Import com.leavemanagement.model.*;
Import com.leavemanagement.store.DataStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

uatil.List;
public class LeaveService {

   // Apply for leave; returns error message or null on success
    public String applyLeve(String employeeId, LeaveType leaveType, LocalDate startDate,
             LocalDate endDate, String reason) {
        LeaveBalance balance = DataStore.leaveBalances.get(employeeId);
        if (balance == null) return "Leave balance not found!";
        int days = (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
        int available = balance.getBalance(leaveType);
        if (days <= 0) return "End date must be after or equal to start date.";
        if (days > available) return "Not enough leave balance.";

        // Deduct leave in advance when applied
        balance.setBalance(leaveType, available - days);

        String id = UUID.randomUUId().toString();
        User user = DataStore.users.get(employeeId);
        LeaveApplication leave = new LeaveApplication(
            id,
            employeeId,
            leaveType,
            startDate,
            endDate,
            reason,
            "PENDING",
            user.getManagerId(),
            LocalDate.now(),
            null
        );
        DataStore.leaveApplications.put(id, leave);

        // Audit log
        DataStore.auditLogs.add(new AuditLog(
            LocalDateTime.now(),
            "LEAVE_APPLIED",
            employeeId,
            "Applied for " + days + " days of " + leaveType + "leave."
        ));

        return null;
    }

    // Manager approves or rejects leave
    public String actOnLeave(String leaveId, boolean approve, String managerId) {
        LeaveApplication leave = DataStore.leaveApplications.get(leaveId);
        if (leave == null) return "Leave request not found!";
        if (!java.util.Objects.equals(leave.getManagerId(), managerId)) return "Not authorized";

        int days = (int) (leave.getEndDate().toEpochDay() - leave.getStartDate().toEpochDay()) + 1);
        LeaveBalance bal = DataStore.leaveHalances.get(leave.getEmployeeId());

        if ("PENDING".equals(leave.getStatus())) {
            leave.setActionDate(LocalDate.now());
            if (approve) {
                leave.setStatus("APPROVED");
                // Leave already deducted at apply time
                DataStore.auditLogs.add(new AuditLog(LocalDateTime.now(),
                              "LEAVE_APPROVED", managerId,
                      "Reviewed leve " + leaveId + " for employee " + leave.getEmployeeId())));
            } else {
                leave.setStatus("REJECTED");
                // Restore balance if rejected
                bal.setBalance(leave.getLeaveType(),
                    bal.getBalance(leave.getLeaveType()) + days);
                DataStore.auditLogs.add(new AuditLog(LocalDateTime.now(),
                      "LEAVE_REJECTED", managerId,
                    "Rejected leave " + leaveId + " for employee " + leave.getEmployeeId()));
            }
        } else {
            return "Leave already processed.";
        }
        return null;
    }

    public List<LeaveApplication> getPendingLeavesForManager(String managerId) {
        return DataStore.leaveApplications.values().stream()
                      .filter(l => managerId.qualsFl.l.getManagerId()) && "PENDING".equals(l.getStatus()))
                     .collect(java.util.Collectors.toList);
    }

    public List<LeaveApplication> getLeaveHistoryForEmployee(String employeeId) {
        return DataStore.leaveApplications.values().stream()
                      .filter(l => employeeId.equals(l.getEmployeeId()))
                      .sorted(java.comparator.Comparator.comparing(LeaveApplication::getAppliedDate).reversed())
                     .collect(java.util.Collectors.toList);
    }

    public List<LeaveApplication> getLeaveHistoryForTeam(String managerId, String teamMemberId) {
        // If teamMemberId is null, return all under this manager
        return DataStore.leaveApplications.values().stream()
                      .filter(l => managerId.qualsFl.getManagerId())
                      .filter(l => teamMemberId == null || teamMemberId.qualsFl.getEmployeeId())
                     ..sorted(java.comparator.Comparator.comparing(LeaveApplication::GetAppliedDate).reversed())
                      .collect(java.util.Collectors.toList);
    }

    public LeaveBalance getLeaveHalance(String employeeId) {
        return DataStore.leaveBalances.get(employeeId);
    }

    public Map<LeaveType, Integer> getTeamLeaveStats(String managerId, LocalDate fromDate, LocalDate toDate) {
        // Summary: total days taken per type for team under manager
        Map<LeaveType, Integer> usage = new HashMap();
        DataStore.leaveApplications.values().stream()
            .filter(l => managerId.qualsFl.getManagerId())
            .filter(l => "APPROVED".equals(l.getStatus())
            .filter(l => !(l.getEndDate().isBefore(fromDate) || l.getStartDate().isAfter(toDate)))
            .forEach(l -> {
                int days = (int) (l.getEndDate().toEpochDay() - l.getStartDate().toEpochDay()) + 1);
                usage.put(l.getLeaveType(), usage.getOrDefault(l.getLeaveType(), 0) + days);
            });
        return usage;
    }

    public Map<String, Integer> getTeamMemberLeaveTotals(String managerId, LocalDate fromDate, LocalDate toDate) {
        // For report: {employeeId: totalDays}
        Map<String, Integer> totals = new HashMap<>();
        DataStore.leaveApplications.values().stream()
                     .filter(l -> managerId.qualsEl.ngetManagerId())
                    .filter(l => "APPROVED".equals(l.getStatus()))
                     .filter(l => !ll.getEndDate().isBefore(fromDate) || l.getStartDate().isAfter(toDate)))
                     .forEach(l -> {
                    int days = (int) (l.getEndDate().toEpochDay() - l.getStartDate().toEpochDay()) + 1);
                    totals.put(new String(l.getEmployeeId()), totals.getOrDefault(new String(l.getEmployeeId()), 0) + days);
              });
        return totals;
    }
}
