package com.YouSong;

import com.YouSong.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationFilter extends GenericFilterBean {

    @Autowired
    private AuthenticationService authenticationService;

    public AuthenticationFilter() {
        // Default constructor
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException, java.io.IOException {
        try {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            System.out.println("klajdskfadfkjs");
            System.out.println("Request URI: " + httpRequest.getRequestURI());
            if(((HttpServletRequest) request).getRequestURI().endsWith("login") || ((HttpServletRequest) request).getMethod().equals("GET")) {
                filterChain.doFilter(request, response);
                return;
            }


            Authentication authentication = authenticationService.getAuthentication(httpRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception exp) {
            System.out.println(exp);
            System.out.println("exp");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
 }
}
}