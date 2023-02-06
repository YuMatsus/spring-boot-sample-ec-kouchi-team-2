package com.example.springbootsampleec.entities;
 
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Store> stores; // ユーザーが登録した店舗

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private Set<CartItem> cartItems; // カートのアイテム

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private Set<Order> orders; // 注文

    @Column(name = "name", length = 60, nullable = false)
    private String name; // ユーザー名

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email; // メールアドレス

    @Column(name = "password", length = 100, nullable = false)
    private String password;  // パスワード

    @Column(name = "address", length = 500, nullable = false)
    private String address; // 住所

    @Column(name = "lat")
    private double lat = 0.0; // 緯度

    @Column(name = "lng")
    private double lng = 0.0; // 経度

    @Column(name = "roles", length = 120)
    private String roles; // ロール（役割）

    @Column(name = "enable_flag", nullable = false)
    private Boolean enable; // 有効フラグ

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}