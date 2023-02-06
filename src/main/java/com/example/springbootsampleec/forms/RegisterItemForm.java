package com.example.springbootsampleec.forms;
 
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterItemForm {
    @NotNull
    @Size(min=1, max=200)
    private String name;
    
    @NotNull
    private int price;
    
    @NotNull
    private int calorie;

    @NotNull
	private Set<Long> itemCategoryIds;

    @NotNull
	private Set<Long> nutrientIds;
    
    @NotNull
    @Size(min=1, max=1000)
    private String description;
    
    @NotNull
    private  MultipartFile image;
}