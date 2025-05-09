package com.kyouhei.mathapp.controller.restApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyouhei.mathapp.dto.AnswerRequest;
import com.kyouhei.mathapp.dto.ChoiceDto;
import com.kyouhei.mathapp.dto.SessionProblemDto;
import com.kyouhei.mathapp.dto.TestSessionDto;
import com.kyouhei.mathapp.entity.Choice;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.ChoiceRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.repository.TestSessionRepository;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/session")
@AllArgsConstructor
@CrossOrigin(origins="http://localhost:3000",allowCredentials="true")
public class TestSessionApiController {

	private final TestSessionService testSessService;
	private final HttpSession session;
	private final ChoiceRepository choiceRepo;
	private final SessionProblemRepository sessProbRepo;
	private final TestSessionRepository testSessRepo;

	//セッション作成
	@PostMapping("/test")
	public ResponseEntity<TestSessionDto> createTestSess(
			@RequestParam(defaultValue="false") boolean includeIntegers) {
		
	  User user=(User)session.getAttribute("user");
	    
	  TestSession testSess= testSessService.createTestSess(user, includeIntegers);
	  //DTOマッピング
	  TestSessionDto dto=new TestSessionDto();
	  dto.setId(testSess.getId());
	    
	  return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

    /*問題表示
	@GetMapping("/{sessionId}/problems")
	public ResponseEntity<List<SessionProblemDto>> vieｗProblems(
	    @PathVariable Long sessionId) {
		
		User user=(User)session.getAttribute("user");
		
		
	  List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
	  //DTOにマッピング
	  List<SessionProblemDto> spDtos=new ArrayList<>();
	  
	  for(SessionProblem sp : sps) {
	   SessionProblemDto spDto=new SessionProblemDto();
	   spDto.setId(sp.getId());
	   spDto.setQuestion(sp.getProblem().getQuestion());
	   spDtos.add(spDto);
	  }
	    
	  return ResponseEntity.ok(spDtos);
	}*/
	
	//1問ずつ問題表示
	@GetMapping("/{sessionId}/problems/{idx}")
	public ResponseEntity<SessionProblemDto> viewOneProblem(
			  				@PathVariable Long sessionId,
			  				@PathVariable int idx){
		  
		User user=(User)session.getAttribute("user");
		
		Optional<TestSession> someTestSess=
				testSessRepo.findById(sessionId);
		
		//テストセッションが存在しない → 404
		if (someTestSess.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		TestSession ts=someTestSess.get();
		//ログインIDと一致しない → 403
		if(!ts.getUser().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
		
		if (idx < 0 || idx >= sps.size()) {
	        //idxが範囲外 → 400
	        return ResponseEntity.badRequest().build();
	    }
		
		//ログインIDと一致。通常処理 →200
		//DTOマッピング
		SessionProblem sp=sps.get(idx);
		List<ChoiceDto> choiceDtos=new ArrayList<>();
	  
		List<Choice> choices=sp.getProblem().getChoices();
	
		for(Choice choice : choices) {
			ChoiceDto choiceDto=new ChoiceDto();
			choiceDto.setId(choice.getId());
			choiceDto.setChoiceText(choice.getChoiceText());
			choiceDtos.add(choiceDto);
		}
	  SessionProblemDto spDto=new SessionProblemDto(
			  					sp.getId(),
			  					sp.getProblem().getQuestion(),
			  					choiceDtos,
			  					sps.size());
			  
		return ResponseEntity.ok(spDto);
	  }
		  
	//解答を受けとり次の問題へ
	@PostMapping("/{sessionId}/problems/{idx}/answer")
	public ResponseEntity<Void> submitAndNext(
			  @PathVariable Long sessionId,
			  @PathVariable int idx,
			  @RequestBody AnswerRequest req){
		  
		User user=(User)session.getAttribute("user");
		
		Optional<TestSession> someTestSess=
				testSessRepo.findById(sessionId);
		
		//テストセッションが存在しない
		if (someTestSess.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		TestSession ts=someTestSess.get();
		//ログインIDと一致しない
		if(!ts.getUser().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
		
		if (idx < 0 || idx >= sps.size()) {
	        //idxが範囲外
	        return ResponseEntity.badRequest().build();
	    }
		
		//ログインIDと一致。通常処理 
		SessionProblem sp=sps.get(idx);
		Choice selectedChoice=choiceRepo.findById(req.getSelectedChoiceId()).orElse(null);
		
		sp.setSelectedChoice(selectedChoice);
		sp.setIsCorrect(selectedChoice!=null && selectedChoice.isCorrect());
		sessProbRepo.save(sp);
		  
		return ResponseEntity.noContent().build();
	}
	  
}
