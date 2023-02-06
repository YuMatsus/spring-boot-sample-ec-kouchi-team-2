package com.example.springbootsampleec.forms;
 
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditImageForm {
    
    private  MultipartFile image;
    
}