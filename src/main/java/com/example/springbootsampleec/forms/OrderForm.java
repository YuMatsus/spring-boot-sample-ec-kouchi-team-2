package com.example.springbootsampleec.forms;
 
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderForm {
    @NotNull
    private int paymentMethod; // 支払方法
    
    @NotNull
    private int deliveryPreservation; // 時間指定の有無: 0=指定なし, 1=指定あり
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveryTime; // 配達日時
    
}