package com.example.springbootsampleec.forms;
 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.springbootsampleec.validators.MatchFields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@MatchFields(fields = {"password","passwordConfirm"}, message = "確認用のパスワードが一致しません")
public class SignUpForm {
    
    // ユーザー名
    @NotNull
    @Size(min=1, message = "ユーザー名を入力して下さい")
    @Size(max=60, message = "ユーザー名は60文字以内で入力して下さい")
    private String name;
    
    // メールアドレス
    @NotNull
    @Size(min=1, message = "メールアドレスを入力して下さい")
    @Size(max=100, message = "メールアドレスは100文字以内で入力して下さい")
    private String email;

    // パスワード
    @NotNull
    @Size(min=8, message = "パスワードは8文字以上で入力して下さい")
    @Size(max=100, message = "パスワードは100文字以内で入力して下さい")
    private String password;

    // 確認用パスワード
    @NotNull
    @Size(min=1, message = "確認用のパスワードを入力して下さい")
    private String passwordConfirm;

    // 住所
    @NotNull
    @Size(min=1, message = "住所を入力して下さい")
    @Size(max=500, message = "住所は500文字以内で入力して下さい")
    private String address;
}