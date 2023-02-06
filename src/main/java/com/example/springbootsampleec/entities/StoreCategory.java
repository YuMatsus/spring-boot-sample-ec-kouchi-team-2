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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_category")
@Entity
public class StoreCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // カテゴリーID

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="stores_categories",
        joinColumns = @JoinColumn(name="store_category_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="store_id", referencedColumnName="id"))
    private Set<Store> categorizedStores = new HashSet<Store>(); // カテゴリが付与された店舗

    @Column(name = "name", length = 50, nullable = false)
    private String name; // カテゴリー

	@Column(name = "createdAt", nullable = false, updatable = false, insertable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
private ZonedDateTime createdAt;  // 作成日

    @Column(name = "updatedAt", nullable = false, updatable = false, insertable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt; // 更新日

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoreCategory other = (StoreCategory) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
