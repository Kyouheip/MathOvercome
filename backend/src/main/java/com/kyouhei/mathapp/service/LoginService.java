package com.kyouhei.mathapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.kyouhei.mathapp.dto.LoginRequest;
import com.kyouhei.mathapp.dto.RegisterRequest;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {

	private UserRepository userRepo;
	
	//このクラスで使うためだけのエラー追加メソッド
	public void addError(BindingResult result){
		FieldError fieldError = new FieldError(
				result.getObjectName(),
				"password",
				"IDまたはパスワードが間違っています。");
		result.addError(fieldError);
	}
	
	//ログインチェック
	public boolean isValid(LoginRequest loginData,BindingResult result) {
		
		//ログインiDが登録されているか
		Optional<User> user = userRepo.findByUserId(loginData.getUserId());
		if(user.isEmpty()) {
			addError(result);
			return false;
		}
		
		//パスワードチェック
		if(!user.get().getPassword().equals(loginData.getPassword())){
			addError(result);
			return false;
		}
		return true;
	}
	
	//ユーザー登録チェック
	public boolean isValid(RegisterRequest registData,BindingResult result) {
		//パスワード不一致
		if(!registData.getPassword1().equals(registData.getPassword2())) {
			FieldError fieldError = new FieldError(
					result.getObjectName(),
					"password2",
					"パスワードが一致しません。");
			result.addError(fieldError);
			return false;
		}
		
		//ログインIDがすでに登録済かどうか
		Optional<User> user = userRepo.findByUserId(registData.getUserId());
		if(user.isPresent()) {
			FieldError fieldError = new FieldError(
					result.getObjectName(),
					"userId",
					"すでに利用されているIDです。");
			result.addError(fieldError);
			return false;
		}
		return true;
	}
}
