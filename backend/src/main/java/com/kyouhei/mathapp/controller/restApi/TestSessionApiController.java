package com.kyouhei.mathapp.controller.restApi;

import java.util.ArrayList;
import java.util.List;

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
import com.kyouhei.mathapp.dto.SessionProblemDto;
import com.kyouhei.mathapp.dto.TestSessionDto;
import com.kyouhei.mathapp.entity.Choice;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.repository.ChoiceRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

	@RestController
	@RequestMapping("/api/session")
	@AllArgsConstructor
	@CrossOrigin(origins="http://localhost:3000")
	public class TestSessionApiController {

	  private final TestSessionService testSessService;
	  private final HttpSession session;
	  private final ChoiceRepository choiceRepo;
	  private final SessionProblemRepository sessProbRepo;

	  //セッション作成
	  @PostMapping("/test")
	  public ResponseEntity<TestSessionDto> createTestSess(
	      @RequestParam(defaultValue="false") boolean includeIntegers) {
	    User user = (User)session.getAttribute("user");
	    
	    TestSession testSess= testSessService.createTestSess(user, includeIntegers);
	    //DTOにマッピング
	    TestSessionDto dto=new TestSessionDto();
	    dto.setId(testSess.getId());
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	  }

	  //問題表示
	  @GetMapping("/{sessionId}/problems")
	  public ResponseEntity<List<SessionProblemDto>> showProblems(
	      @PathVariable Long sessionId) {
	    List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
	    //DTOにマッピング
	    List<SessionProblemDto> dtos=new ArrayList<>();
	    for(SessionProblem sp : sps) {
	    	SessionProblemDto dto=new SessionProblemDto();
	    	dto.setId(sp.getId());
	    	dto.setQuestion(sp.getProblem().getQuestion());
	    	dtos.add(dto);
	    }
	    
	    return ResponseEntity.ok(dtos);
	  }
	
	  //1問ずつ表示
	  @GetMapping("/{sessionId}/problems/{idx}")
	  public ResponseEntity<SessionProblemDto> showOneProblem(
			  						@PathVariable Long sessionId,
			  						@PathVariable int idx){
		  
		  User user=(User)session.getAttribute("user");
		  
		  List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
		  
		  if(idx>=sps.size()) {
			  ResponseEntity.notFound().build();
		  }
		  
		  //DTOマッピング
		  SessionProblem sp=sps.get(idx);
		  SessionProblemDto dto=new SessionProblemDto();
		  
		  dto.setId(sp.getId());
		  dto.setQuestion(sp.getProblem().getQuestion());
		  
		  List<Choice> choice=sp.getProblem().getChoices();
		  List<String> choiceTexts=new ArrayList<>();
		  for(Choice cho : choice) {
			  choiceTexts.add(cho.getChoiceText());
			  }
		  
		  dto.setChoiceTexts(choiceTexts);
		  
		  return ResponseEntity.ok(dto);
		  
	  }
	  
	  //解答を受けとり次の問題へ
	  @PostMapping("/{sessionId}/problems/{idx}/answer")
	  public ResponseEntity<Void> submitAnswer(
			  		@PathVariable Long sessionid,
			  		@PathVariable int idx,
			  		@RequestBody AnswerRequest req){
		  
		  User user=(User)session.getAttribute("user");
		  
		  List<SessionProblem> sps=testSessService.getSessionProblems(sessionid);
		  
		  if(idx>=sps.size()) {
			  return ResponseEntity.notFound();
		  }
		  
		  SessionProblem sp=sps.get(idx);
		  Choice userChoice=choiceRepo.findById(req.getSelectedChoiceId()).orElse(null);
		  sp.setSelectedChoice(userChoice);
		  sp.setIsCorrect(userChoice!=null && userChoice.isCorrect());
		  sessProbRepo.save(sp);
		  
		  return ResponseEntity.noontent().build();
	}
	  
}
