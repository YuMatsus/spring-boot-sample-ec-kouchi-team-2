package com.example.springbootsampleec.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.repositories.UserRepository;
import com.example.springbootsampleec.security.SimpleLoginUser;
import com.example.springbootsampleec.services.GeoApiService;
import com.example.springbootsampleec.services.UserService;
import com.google.maps.model.LatLng;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GeoApiService geoApiService;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        // userRepository の findAll メソッドを呼び出す。
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    // ユーザーの登録
    @Transactional
    @Override
    public void register(String name, String email, String  password, String address, String[] roles) {
        // 該当のメールアドレスが登録されているかどうかをチェック
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("該当のメールアドレスは登録済みです。");
        }
        // パスワードを暗号化
        String encodedPassword = passwordEncode(password);
        // ユーザー権限の配列を文字列にコンバート
        String joinedRoles = joinRoles(roles);

        // 座標の取得
        LatLng geocode = geoApiService.getLatLng(address);
        double lat = 0;
        double lng = 0;
        if(geocode != null){
            lat = geocode.lat;
            lng = geocode.lng;
        }

        // User エンティティの生成
        User user = new User(null, null, null, null, name, email, encodedPassword, address, lat, lng, joinedRoles, Boolean.TRUE);

        // ユーザー登録
        userRepository.saveAndFlush(user);
    }

    // ユーザー情報の更新
    @Transactional
    @Override
    public void update(long id, String name, String email, String address) {
        User user =  findById(id).orElseThrow();
        LatLng geocode = geoApiService.getLatLng(address);
        double lat = 0;
        double lng = 0;
        if(geocode != null){
            lat = geocode.lat;
            lng = geocode.lng;
        }
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);
        user.setLat(lat);
        user.setLng(lng);
        userRepository.saveAndFlush(user);
    }

    // パスワードの更新
    @Transactional
    @Override
    public void updatePassword(User user, String password) {
        String encodedPassword = passwordEncode(password);
        user.setPassword(encodedPassword);
        userRepository.saveAndFlush(user);
    }

    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        User user =  findById(id).orElseThrow();
        userRepository.delete(user);
    }

    // 更新されたログインユーザー情報の取得
    @Transactional
    @Override
    public User getRefreshedLoginUser(User user) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        SimpleLoginUser simpleLoginUser = (SimpleLoginUser)authentication.getPrincipal();
        User loginUser = simpleLoginUser.getUser();
        return loginUser;
    }

    // パスワードを暗号化
    private String passwordEncode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // ユーザー権限の配列を文字列にコンバート
    private String joinRoles(String[] roles) {
        if (roles == null || roles.length == 0) {
            return "";
        }
        return Stream.of(roles)
            .map(String::trim)
            .map(String::toUpperCase)
            .collect(Collectors.joining(","));
    }
}