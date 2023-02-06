package com.example.springbootsampleec.services;
 
import com.example.springbootsampleec.entities.User;
 
import java.util.List;
import java.util.Optional;
 
public interface UserService {
    // ユーザー一覧の取得
    List<User> findAll();
    // ユーザーの取得
    Optional<User> findById(long id);
    // ユーザーの登録
    void register(String name, String email, String password, String address, String[] roles);
    // ユーザー情報の更新
    void update(long id, String name, String email, String address);
    // パスワードの更新
    void updatePassword(User user, String password);
    // 削除
    void delete(long id);
    // 更新されたログインユーザー情報の取得
    User getRefreshedLoginUser(User user);
}