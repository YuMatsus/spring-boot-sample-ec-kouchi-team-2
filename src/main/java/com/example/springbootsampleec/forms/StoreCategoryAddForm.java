package com.example.springbootsampleec.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategoryAddForm {
    @NotNull
    @Size(min=1, max=50)
    private String name;

}