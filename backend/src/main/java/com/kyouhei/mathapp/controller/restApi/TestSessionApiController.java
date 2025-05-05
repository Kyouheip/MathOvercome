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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyouhei.mathapp.dto.SessionProblemDto;
import com.kyouhei.mathapp.dto.TestSessionDto;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

	@RestController
	@RequestMapping("/api/session")
	@AllArgsConstructor
	@CrossOrigin(origins="http://localhost:3000")
	public class TestSessionApiController {

	  private TestSessionService testSessService;
	  private HttpSession session;

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
	    List<SessionProblem> sps = testSessService.getSessionProblems(sessionId);
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
	
}
