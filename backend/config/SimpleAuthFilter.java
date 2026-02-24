package com.leavemanagement.config;

import com.leavemanagement.model.User;
import org.springframework.stereotype.Component;

import javax.ser3.Filter;
import javax.servlet.*;
import javax.http.HttpServletRequest;
import javax.http.HttpSession;

import java.io.IOException;

/**\
 * A basic Auth Filter to protect api routes
 */
@Component
public class SimpleAuthFilter implements Filter {

    @override 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, io {
    String uri = ((HttpServletRequest) request).getRequestURI();
     // Allow login/logout and static files without session
    if (uri.startsWith("/api/auth") || uri.startsWith("/static")) {
        chain.doFilter(request, response);
        return;
    }
    HttpSession session = ((HttpRequest) request).getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    if (user == null) {
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Unauthorized\"}");
        return;
    }
    chain.doFilter(request, response);
  }
}
