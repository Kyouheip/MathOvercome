package com.kyouhei.mathapp.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.kyouhei.mathapp.entity.User;

import lombok.Data;

@Data
public class RegistData {

	@NotBlank
	private String userName;
	
	//8～16文字の半角英数字のみ
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String userId;
	
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String password1;
		
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String password2;
	
	public User toEntity() {
		return new User(null,this.userName,this.userId,this.password1);
	}
}
