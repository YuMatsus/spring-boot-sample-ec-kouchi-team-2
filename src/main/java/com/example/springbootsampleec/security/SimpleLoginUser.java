package com.example.springbootsampleec.security;
 
import com.example.springbootsampleec.entities.User;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
 
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
 
public class SimpleLoginUser extends org.springframework.security.core.userdetails.User {
 
    private User user;
 
    public SimpleLoginUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getEnable(), true, true,
            true, convertGrantedAuthorities(user.getRoles()));
        this.user = user;
    }
 
    public User getUser() {
        return user;
    }
 
    // 権限管理用のメソッド
    static Set<GrantedAuthority> convertGrantedAuthorities(String roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        Set<GrantedAuthority> authorities = Stream.of(roles.split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
        return authorities;
    }
 
}