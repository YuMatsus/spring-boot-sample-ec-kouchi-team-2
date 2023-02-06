package com.example.springbootsampleec.entities;
 
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @ManyToOne(fetch = FetchType.EAGER)
    private Store store; // 店舗

    @OneToMany(mappedBy = "item", fetch=FetchType.EAGER)
    private Set<CartItem> cartItems; // カートのアイテム

    @OneToMany(mappedBy = "item", fetch=FetchType.EAGER)
    private Set<OrderedItem> orderedItems; // 注文されたアイテム
    
    @Column(name = "name", length = 200, nullable = false)
    private String name; // 商品名
    
    @Column(name = "price", nullable = false)
    private int price; // 金額
    
    @Column(name = "calorie", nullable = false)
    private int calorie; // カロリー

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="items_categories",
        joinColumns = @JoinColumn(name="item_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="item_category_id", referencedColumnName="id"))
    private Set<ItemCategory> itemCategories = new HashSet<ItemCategory>(); // 料理の属するカテゴリ

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="items_nutrients",
        joinColumns = @JoinColumn(name="item_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="nutrient_id", referencedColumnName="id"))
    private Set<Nutrient> nutrients = new HashSet<Nutrient>(); // 料理の属する栄養カテゴリ
    
    @Column(name = "description", length = 1000, nullable = true)
    private String description; // 商品説明

    @Column(name = "image", length = 100, nullable = false)
    private String image;  // 画像

    @Column(name = "status", nullable = false)
    private int status; // 公開フラグ(0:非公開, 1:公開)
    
    // 作成日時
    @Column(name="createdAt",nullable = false, updatable = false, insertable = false, 
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private ZonedDateTime createdAt;

    // 更新日時
    @Column(name="updatedAt",nullable = false, updatable = false, insertable = false, 
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    

}