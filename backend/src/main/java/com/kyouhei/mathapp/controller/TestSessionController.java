package com.kyouhei.mathapp.controller;

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
import com.kyouhei.mathapp.dto.ProblemDto;
import com.kyouhei.mathapp.dto.SessionProblemDto;
import com.kyouhei.mathapp.dto.TestSessionDto;
import com.kyouhei.mathapp.entity.Choice;
import com.kyouhei.mathapp.entity.Problem;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.ChoiceRepository;
import com.kyouhei.mathapp.repository.ProblemRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.repository.TestSessionRepository;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
public class TestSessionController {

	private final TestSessionService testSessService;
	private final ChoiceRepository choiceRepo;
	private final SessionProblemRepository sessProbRepo;
	private final TestSessionRepository testSessRepo;
	private final ProblemRepository probRepo;

	//セッション作成
	@PostMapping("/test")
	public ResponseEntity<TestSessionDto> createTestSess(
				@RequestParam(defaultValue = "false") boolean includeIntegers,
				HttpSession session) {
		
	  User user = (User)session.getAttribute("user");
	  TestSession testSess =  testSessService.createTestSess(user, includeIntegers);
	  //DTOマッピング
	  TestSessionDto dto = new TestSessionDto();
	  dto.setId(testSess.getId());
	    
	  return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	//1問ずつ問題表示
	@GetMapping("/{sessionId}/problems/{idx}")
	public ResponseEntity<SessionProblemDto> viewOneProblem(
			  				@PathVariable Long sessionId,
			  				@PathVariable int idx,
			  				HttpSession session){
	
		User user = (User)session.getAttribute("user");
		Optional<TestSession> someTestSess = 
				testSessRepo.findById(sessionId);
		
		//テストセッションが存在しない → 404
		if (someTestSess.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		TestSession ts = someTestSess.get();
		//ログインIDと一致しない → 403
		if(!ts.getUser().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<SessionProblem> sps = testSessService.getSessionProblems(sessionId);
		
		if (idx < 0 || idx >=  sps.size()) {
	        //idxが範囲外 → 400
	        return ResponseEntity.badRequest().build();
	    }
		
		//ログインIDと一致。通常処理 →200
		//DTOマッピング
		SessionProblem sp = sps.get(idx);
		List<ChoiceDto> choiceDtos = new ArrayList<>();
	  
		List<Choice> choices = sp.getProblem().getChoices();
	
		for(Choice choice : choices) {
			ChoiceDto choiceDto = new ChoiceDto();
			choiceDto.setId(choice.getId());
			choiceDto.setChoiceText(choice.getChoiceText());
			choiceDtos.add(choiceDto);
		}
	  SessionProblemDto spDto = new SessionProblemDto(
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
			  @RequestBody AnswerRequest req,
			  HttpSession session){
		  
		User user = (User)session.getAttribute("user");
		
		Optional<TestSession> someTestSess = 
				testSessRepo.findById(sessionId);
		
		//テストセッションが存在しない
		if (someTestSess.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		TestSession ts = someTestSess.get();
		//ログインIDと一致しない
		if(!ts.getUser().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<SessionProblem> sps = testSessService.getSessionProblems(sessionId);
		
		if (idx < 0 || idx >=  sps.size()) {
	        //idxが範囲外
	        return ResponseEntity.badRequest().build();
	    }
		
		//ログインIDと一致。通常処理 
		SessionProblem sp = sps.get(idx);
		Choice selectedChoice = choiceRepo.findById(req.getSelectedChoiceId()).orElse(null);
		
		sp.setSelectedChoice(selectedChoice);
		sp.setIsCorrect(selectedChoice != null && selectedChoice.isCorrect());
		sessProbRepo.save(sp);
		  
		return ResponseEntity.noContent().build();
	}
	  
	//テスト
	@GetMapping("/st")
	public ResponseEntity<List<ProblemDto>> test(){
		List<Problem> probs = probRepo.findAll();
		List<ProblemDto> probDtos = new ArrayList<>();
		for(Problem prob : probs) {
			ProblemDto probDto = new ProblemDto();
			
			List<ChoiceDto> choiceDtos = new ArrayList<>();
			for(Choice choice : prob.getChoices()) {
				ChoiceDto choiceDto = new ChoiceDto();
				choiceDto.setId(choice.getId());
				choiceDto.setChoiceText(choice.getChoiceText());
				choiceDtos.add(choiceDto);
			}
			
			probDto.setId(prob.getId());
			probDto.setQuestion(prob.getQuestion());
			probDto.setHint(prob.getHint());
			probDto.setChoices(choiceDtos);
			
			
			probDtos.add(probDto);
		}
		return ResponseEntity.ok(probDtos);
	}
}
