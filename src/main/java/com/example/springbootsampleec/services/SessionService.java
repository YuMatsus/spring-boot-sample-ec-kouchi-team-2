package com.example.springbootsampleec.services;

import javax.servlet.http.HttpServletRequest;

import com.example.springbootsampleec.entities.User;

public interface SessionService {
    // セッションにURIを埋め込む
    void setUri(HttpServletRequest request);
    // セッションからURIを取得
    String getUri(HttpServletRequest request);
    // SecurityContext の更新
    void updateSecurityContext(User user);
}