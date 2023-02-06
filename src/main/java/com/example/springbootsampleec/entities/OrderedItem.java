package com.example.springbootsampleec.entities;

import java.beans.Transient;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordered_item") 
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order; // 注文

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item; // 料理

    @Column(name = "quantity", nullable = false)
    private int quantity; // 料理の数

    // 料理の数 × 価格 の合計金額
    @Transient
    public int getPrice() {
        return item.getPrice() * quantity;
    }
    
    // 料理の数 × カロリー の合計
    @Transient
    public int getCalorie() {
        return item.getCalorie() * quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderedItem other = (OrderedItem) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
