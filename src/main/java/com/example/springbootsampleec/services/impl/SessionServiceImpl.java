package com.example.springbootsampleec.services.impl;
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.security.SimpleUserDetailsService;
import com.example.springbootsampleec.services.SessionService;
 
@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SimpleUserDetailsService simpleUserDetailsService;

    // セッションにURIを埋め込む
    @Transactional
    @Override
    public void setUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        request.getSession().setAttribute("uri", uri);
    }

    @Transactional(readOnly = true)
    @Override
    public String getUri(HttpServletRequest request) {
        String uri = (String)request.getSession().getAttribute("uri");
        return uri;
    }

    // SecurityContext の更新
    @Override
    @Transactional
    public void updateSecurityContext(User user) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(user.getEmail());
    context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }
    
}