package com.kyouhei.mathapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.kyouhei.mathapp.entity.User;

import lombok.Data;

@Data
public class RegisterRequest {

	@NotBlank(message = "入力してください")
	private String userName;
	
	//8～16文字の半角英数字のみ
	@NotBlank(message = "入力してください")
	@Length(min = 6,max = 16,message = "6文字以上16文字以下で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "半角英数字のみで入力してください")
	private String userId;
	
	@NotBlank(message = "入力してください")
	@Length(min = 6,max = 16,message = "6文字以上16文字以下で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "半角英数字のみで入力してください")
	private String password1;
		
	@NotBlank(message = "入力してください")
	@Length(min = 6,max = 16,message = "6文字以上16文字以下で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "半角英数字のみで入力してください")
	private String password2;
	
	public User toEntity() {
		
		return new User(null,this.userName,this.userId,this.password1,null);
	}
}
