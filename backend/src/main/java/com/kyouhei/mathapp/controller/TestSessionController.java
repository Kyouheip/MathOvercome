package com.kyouhei.mathapp.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyouhei.mathapp.entity.Choice;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.ChoiceRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.repository.TestSessionRepository;
import com.kyouhei.mathapp.repository.UserRepository;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/session")
public class TestSessionController {

	private TestSessionService testSessService;
	private SessionProblemRepository sessProbRepo;
	private ChoiceRepository choiceRepo;
	private HttpSession session;
	private UserRepository userRepo;
	private TestSessionRepository testSessRepo;
	
	//テストセッション作成
	@PostMapping("/test")
	public String createTestSess(
				@RequestParam(defaultValue="false")boolean includeIntegers) {
		
		User user=(User)session.getAttribute("user");
		TestSession session=
				testSessService.createTestSess(user,includeIntegers);
		
		return "redirect:/session/"+session.getId()+"/problem";
	}
	
	//テストセッションの問題を1問ずつ表示
	@GetMapping("/{sessionId}/problem")
	public ModelAndView viewOneProblem(
				@PathVariable Long sessionId,
				@RequestParam(defaultValue="0") int idx,
				ModelAndView mv,
				RedirectAttributes redirectAttributes){
		
		User user=(User)session.getAttribute("user");
		
		Optional<TestSession> someTestSess=
						testSessRepo.findById(sessionId);
		
		someTestSess.ifPresentOrElse(ts->{
		
		 if(ts.getUser().getId().equals(user.getId())){
				//ログインIDと一致。通常処理
			List<SessionProblem> sps=
					testSessService.getSessionProblems(sessionId);

			//問題終了時、完了ページへ
			if(idx>=sps.size()) {
			redirectAttributes.addFlashAttribute("sps",sps);
			mv.setViewName("redirect:/session/mypage");
			}else {
			SessionProblem sp=sps.get(idx);
			
			mv.addObject("sp",sp);
			mv.addObject("idx",idx);
			mv.addObject("max",sps.size());
			mv.setViewName("problem");
			}
		 }else{
			 //ログインIDと一致しない
			 testSessService.registError(mv,"ログインIDと一致しません。");
		 }
		},()->{
		 	//テストセッションが存在しない
			testSessService.registError(mv, "テストセッションが存在しません");
		 });
			
		return mv;
	}
	
	//解答を受け取り次の問題へ
	@PostMapping("/{sessionId}/problem/{sessionProblemId}/answer")
//値の受け取り方再度確認
	public ModelAndView submitAndNext(
				@PathVariable Long sessionId,
				@PathVariable Long sessionProblemId,
				@RequestParam Long selectedChoiceId,
				@RequestParam int idx,
				ModelAndView mv) {
		
		User user=(User)session.getAttribute("user");
		
		Optional<SessionProblem> someSessProb=
					sessProbRepo.findById(sessionProblemId);
		
		someSessProb.ifPresentOrElse(sp->{
			if(sp.getTestSession().getId().equals(sessionId)&&
				sp.getTestSession().getUser().getId().equals(user.getId())){
				//自分のsessionであり、sessionIdが一致。通常処理
				
				//解答を保存
				Choice userChoice=choiceRepo.findById(selectedChoiceId).orElse(null);
				sp.setSelectedChoice(userChoice);
				sp.setIsCorrect(userChoice.isCorrect());
				
				sessProbRepo.save(sp);
				
				//次の問題へリダイレクト
				mv.setViewName("redirect:/session/"+sessionId+"/problem?idx="+(idx+1));
			}else {
				//ログインID不一致
				testSessService.registError(mv,"ログインIDと不一致または"
						+ "問題がテストセッションのものではありません。");
			}
		},()->{
			//セッション問題が存在しない
			testSessService.registError(mv,"問題が存在しません。");
			
			});
		
		return mv;
	}
	
	@GetMapping("/mypage")
	public String viewMypage() {
		User user=(User)session.getAttribute("user");
		user=userRepo.findByUserId(user.getUserId()).get();
		session.setAttribute("user",user);
		return "mypage";
	}
	
}
