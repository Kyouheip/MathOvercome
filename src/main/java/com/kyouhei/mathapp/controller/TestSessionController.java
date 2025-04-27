package com.kyouhei.mathapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		
		return "redirect:/session/"+session.getId()+"/problem";
	}
	
	//テストセッションの問題を1問ずつ表示
	@GetMapping("/session/{sessionId}/problem")
	public String viewoneProblem(
				@PathVariable Long sessionId,
				@RequestParam(defaultValue="0") int idx,
				Model model,
				RedirectAttributes redirectAttributes){
		
		List<SessionProblem> sps=
								testSessionService.getSessionProblems(sessionId);
		
		//問題終了時、完了ページへ
//sessionIdをattributeしていない
		if(idx>=sps.size()) {
			redirectAttributes.addFlashAttribute("sps",sps);
			return "redirect:/session/"+sessionId+"/summary";
		}
		
		SessionProblem sp=sps.get(idx);
		
		model.addAttribute("sp",sp);
		model.addAttribute("idx",idx);
		model.addAttribute("max",sps.size());
		
		return "problem";
	}
	
	
	
	//解答を受け取り次の問題へ
	@PostMapping("/session/{sessionId}/problem/{sessionProblemId}/answer")
//値の受け取り方再度確認
	public String submitAndNext(
				@PathVariable Long sessionId,
				@PathVariable Long sessionProblemId,
				@RequestParam Long selectedChoiceId,
				@RequestParam int idx) {
		
		//解答を保存
		SessionProblem sp=
				sessionProblemRepository.findById(sessionProblemId).orElse(null);

		Choice userChoice=choiceRepository.findById(selectedChoiceId).orElse(null);
		
		sp.setSelectedChoice(userChoice);
		sp.setIsCorrect(userChoice.isCorrect());
		
		sessionProblemRepository.save(sp);
		
		
		//次の問題へリダイレクト
		return "redirect:/session/"+sessionId+"/problem?idx="+(idx+1);
		
	}
	
	@GetMapping("/session/{sessionId}/summary")
	public String viewSummary(@PathVariable Long sessionId,
							@ModelAttribute List<SessionProblem> sps) {
		
		return "mypage";
	}
	
}
