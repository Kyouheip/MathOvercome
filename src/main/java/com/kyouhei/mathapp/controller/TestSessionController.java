package com.kyouhei.mathapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kyouhei.mathapp.entity.Choice;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.repository.ChoiceRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TestSessionController {

	private TestSessionService testSessionService;
//Serviceクラス検討
	private SessionProblemRepository sessionProblemRepository;
	private ChoiceRepository choiceRepository;
	
	//テスト開始フォーム
	@GetMapping("/start")
	public String startTestForm() {
		return "startTest";
	}
	
	//テストセッション作成
	@PostMapping("/start")
	public String createTestSession(
				@RequestParam Long userId,
				@RequestParam(defaultValue="false")boolean includeIntegers) {
		
		TestSession session=
				testSessionService.createTestSession(userId,includeIntegers);
		
		return "redirect:/session/"+session.getId();
	}
	
	//テストセッションのID表示
	@GetMapping("/session/{sessionId}")
	public ModelAndView viewTestSession(
				@PathVariable Long sessionId,
				ModelAndView mv) {
		mv.setViewName("testSession");
		mv.addObject("sessionId",sessionId);
		
		return mv;
	}
	
	//テストセッションの全ての問題表示
	@GetMapping("/session/{sessionId}/problems")
	public ModelAndView viewSessionProblems(
				@PathVariable Long sessionId,
				ModelAndView mv) {
		List<SessionProblem> sessionProblems=
								testSessionService.getSessionProblems(sessionId);
		
		mv.setViewName("problems");
		mv.addObject("sessionProblems",sessionProblems);
		
		return mv;
	}
	
	//ある問題に対して選択肢を選んだ場合
	@PostMapping("/session/{sessionId}/problem/{sessionProblemId}/answer")
//値の受け取り方再度確認
	public String submitAnswer(
				@PathVariable Long sessionId,
				@PathVariable Long sessionProblemId,
				@RequestParam Long selectedChoiceId) {
		
		SessionProblem sessionProblem=
				sessionProblemRepository.findById(sessionProblemId).orElse(null);
//nullcheck必要か確認
		if(sessionProblem==null)
			return "redirect:/session/"+sessionId+"/problems";
		
		Choice userChoice=choiceRepository.findById(selectedChoiceId).orElse(null);
		
		sessionProblem.setSelectedChoiceId(userChoice);
		sessionProblem.setIsCorrect(userChoice.isCorrect());
		
		sessionProblemRepository.save(sessionProblem);
		
		return "redirect:/session/"+sessionId+"/problems";
		
	}
}
