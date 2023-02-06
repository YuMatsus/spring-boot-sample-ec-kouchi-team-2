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
public class RegisterStoreForm {
    @NotNull
    @Size(min=1, max=200)
    private String name;
    
    @NotNull
    @Size(min=1, max=200)
    private String address;
    
    @Size(min=0, max=1000)
    private String description;

    @NotNull
	private Set<Long> storeCategoryIds;
    
    private  MultipartFile image;
}