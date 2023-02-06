package com.example.springbootsampleec.forms;
 
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreEditForm {

    @NotNull
    @Size(min=1, max=200)
    private String name; // 名前

    @NotNull
    @Size(min=1, max=200)
    private String address; // 住所

    @Size(min=0, max=1000)
    private String description; // 説明

    @NotNull
	private Set<Long> storeCategoryIds; // カテゴリー
}