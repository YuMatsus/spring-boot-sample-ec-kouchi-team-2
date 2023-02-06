package com.example.springbootsampleec.entities;
 
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    private List<Item> items;
    
    @Column(name = "name", length = 200, nullable = false)
    private String name; // 店舗名
    
    @Column(name = "address",  length = 200, nullable = false)
    private String address; // 住所

    @Column(name = "lat")
    private double lat = 0.0; // 緯度

    @Column(name = "lng")
    private double lng = 0.0; // 経度
    
    @Column(name = "description", length = 1000, nullable = true)
    private String description; // 店舗説明

    @Column(name = "image", length = 100, nullable = true)
    private String image;  // 画像

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="stores_categories",
        joinColumns = @JoinColumn(name="store_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="store_category_id", referencedColumnName="id"))
    private Set<StoreCategory> storeCategories = new HashSet<StoreCategory>(); // 店舗の属するカテゴリ

    // 作成日時
    @Column(name="createdAt",nullable = false, updatable = false, insertable = false, 
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private ZonedDateTime createdAt;

    // 更新日時
    @Column(name="updatedAt",nullable = false, updatable = false, insertable = false, 
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt;

}