package com.kyouhei.mathapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TestSessionController {

	private TestSessionService testSessionService;
	
	//テスト開始フォーム
	@GetMapping("/start")
	public String startTestForm() {
		return "startTest";
	}
	
	//テストセッション作成
	@PostMapping("/start")
	public String createTestSession(@RequestParam Long userId,
				@RequestParam(defaultValue="false")boolean includeIntegers) {
		
		TestSession session=
				testSessionService.createTestSession(userId,includeIntegers);
		
		return "redirect:/session/"+session.getId();
	}
	
	//テストセッションの問題表示
	@GetMapping("/session/{sessionId}")
	public ModelAndView viewTestSession(@PathVariable Long sessionId,
											ModelAndView mv) {
		mv.setViewName("testSession");
		mv.addObject("sessionId",sessionId);
		
		return mv;
	}
	
}
