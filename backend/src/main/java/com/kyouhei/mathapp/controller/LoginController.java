package com.kyouhei.mathapp.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyouhei.mathapp.dto.LoginRequest;
import com.kyouhei.mathapp.dto.RegisterRequest;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.UserRepository;
import com.kyouhei.mathapp.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
//クロスオリジン設定 Cookieが送信される
@CrossOrigin(
	    origins = {
	        "http://localhost:3000",
	        "https://math-overcome.vercel.app"
	    },
	    allowCredentials = "true"
	)
@RequiredArgsConstructor
public class LoginController {
	
	private final HttpSession session;
	private final LoginService loginService;
	private final UserRepository userRepo;

	//ログイン
	@PostMapping("/login")
	public ResponseEntity<?> login(
				@RequestBody @Validated LoginRequest req,
				BindingResult result){
		
		//バリデーション & 認証チェック
		if(result.hasErrors()|| !loginService.isValid(req,result)) {
			List<String> errors = new ArrayList<>();
			
			for(FieldError error : result.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
			}
			
			return ResponseEntity.badRequest().body(String.join("\n", errors));
			
		}
		
		//userIdをセッションへ格納
		User user = userRepo.findByUserId(req.getUserId()).get();
		session.setAttribute("user",user);
		System.out.println("ログインセッションID: " + session.getId());

		
		return ResponseEntity.ok().build();
	}
	
	//ログアウト
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(){
		session.invalidate();
		return ResponseEntity.noContent().build();
	}
	
	//新規登録
	@PostMapping("/register")
	public ResponseEntity<?> register(
			@RequestBody @Validated RegisterRequest req,
			BindingResult result){
		
		//バリデーション、サービスチェック
		if(result.hasErrors() || !loginService.isValid(req,result)) {
			List<String> errors = new ArrayList<>();
			
			for(FieldError error : result.getFieldErrors()) {
				String field = error.getField();
				//フィールド名を日本語に変換
				String japanField = switch (field) {
		        case "password2" -> "パスワード確認";
		        case "password1" -> "パスワード";
		        case "userName"  -> "名前";
		        case "userId"    -> "ID";
		        default          -> field;
				};
				
				String message = japanField +":"+error.getDefaultMessage();
				errors.add(message);
			}
			
			return ResponseEntity.badRequest().body(String.join("\n", errors));
		}
		
		//ユーザー登録
		User user = req.toEntity();
		userRepo.saveAndFlush(user);
				
		return ResponseEntity.status(201).build();
				
	}
}
