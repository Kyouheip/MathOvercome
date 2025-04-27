package com.kyouhei.mathapp.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.form.LoginData;
import com.kyouhei.mathapp.form.RegistData;
import com.kyouhei.mathapp.repository.UserRepository;
import com.kyouhei.mathapp.service.LoginService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {

	private UserRepository userRepository;
	private LoginService loginService;
	private HttpSession session;
	
	//ログイン画面表示
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginData",new LoginData());
		
		return "loginForm";
	}
	
	//ログインボタン押下
	@PostMapping("/login/do")
	public String login(@ModelAttribute @Validated
						LoginData loginData,
						BindingResult result,
						Model model) {
		//バリデーション、サービスチェック
		if(result.hasErrors()|| !loginService.isValid(loginData,result)) 
			return "loginForm";
		
		//セッションクリア
		session.invalidate();
		
		//userIdをセッションへ格納
		User user=userRepository.findByUserId(loginData.getLoginId()).get();
		session.setAttribute("userId",user.getId());
		
		return "mypage";
	}
	
	//ログアウト
	@GetMapping("logout")
	public String logout() {
		session.invalidate();
		return "redirect:/login";
	}
	
	//ユーザー新規登録画面
	@GetMapping("/regist")
	public String showRegist(Model model) {
		model.addAttribute("registData",new RegistData());
		
		return "registForm";
	}
	
	//ユーザー新規登録・登録ボタンが押されたとき
	public String registNewUser(@ModelAttribute @Validated
								RegistData registData,
								BindingResult result,
								Model model) {
		//バリデーション、サービスチェック
		if(result.hasErrors() || loginService.isValid(registData,result))
			return "registForm";
			
		//ユーザー登録
		User user=registData.toEntity();
		userRepository.saveAndFlush(user);
		
		return "redirect:/login";
		
	}
	
	//ユーザー新規登録・戻るボタン
	public String registCancel() {
		return "redirect:/login";
	}
}
