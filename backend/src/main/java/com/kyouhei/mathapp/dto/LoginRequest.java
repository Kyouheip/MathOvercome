package com.kyouhei.mathapp.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

	@NotBlank(message = "IDを入力してください")
	private String userId;
	
	@NotBlank(message = "パスワードを入力してください")
	private String password;
}
