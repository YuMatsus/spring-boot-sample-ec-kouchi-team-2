package com.example.springbootsampleec.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.springbootsampleec.exceptions.ExistOtherStoreCartItemException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ExistOtherStoreCartItemException.class)
    public String handleExistOtherStoreCartItemException(
        ExistOtherStoreCartItemException exception,
        Model model
    ) {
        model.addAttribute("message", "別の店舗の料理がカートに入っています。");
        return "error.html";
    }
    
}
