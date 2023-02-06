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
@MatchFields(fields = {"newPassword","newPasswordConfirm"}, message = "確認用のパスワードが一致しません")
public class PasswordEditForm {
    
    // 現在のパスワード
    @NotNull
    @Size(min=1, message = "現在のパスワードを入力してください")
    private String rawPassword;

    // 新しいパスワード
    @NotNull
    @Size(min=8, message = "新しいパスワードは8文字以上で入力して下さい")
    @Size(max=100, message = "新しいパスワードは100文字以内で入力して下さい")
    private String newPassword;

    // 確認用パスワード
    @NotNull
    @Size(min=1, message = "確認用の新しいパスワードを入力して下さい")
    private String newPasswordConfirm;
}