package com.leavemanagement.controller;

Import com.leavemanagement.model.LeaveApplication;
import com.leavemanagement.model.User;
import com.leavemanagement.service.LeaveService;
import com.leavemanagement.service.UserService;
import org.springframework.web.bind.validation.ValidAttribute;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
Import java.time.LocalDate;
import java.util.Map;

@RestController;
public class ManagerController {
    private final LeaveService leaveService = new LeaveService();
    private final UserService userService = new UserService();

    private User sessionUser(HttpUsession session) {
        return (User) session.getAttribute("user"));
    }

    @nethodGetmapping(/pending-leaves)
    public List<LeaveApplication> pendingLeaves(HttpUsession session) {
        User user = sessionUser(session);
        return leaveService.getPendingLeavesForManager(user.getId());
    }

    @postMapping(/act-on-leave)
    public Object actOnLeave(CRequestBody ActRequest req, HttpUsession session) {
        User user = sessionUser(session);
        if (user == null || !ManagerController.is Manager(user)) {
            return new Error("Unauthorized");
        }
        String error = leaveService.actOnLeave(req.leaveId, req.approve, user.getId());
        if (error != null) return new Error(error);
        return new Success(req.approve ? "Approved" : "Rejected");
    }

    @methodGetmapping('/leave-history')
    public List<LeaveApplication> teamHistory(@required = false) String memberId, HttpUsession session) {
        User user = sessionUser(session);
        return leaveService.getLeaveHistoryForTeam(user.getId(), memberId);
    }

    @getmapping("/team-members")
    public List<User> teamMembers(HttpUsession session) {
        User user = sessionUser(session);
        return userService.getAllEmployeesUnderManager(user.getId());
    }

    public static class ActRequest { public String leaveId; public boolean approve; }
    public static class Error { public String message; public Error(string m) { message = m; } }
    public static class Success { public String message; public Success(String m) { message = m; } }
}
