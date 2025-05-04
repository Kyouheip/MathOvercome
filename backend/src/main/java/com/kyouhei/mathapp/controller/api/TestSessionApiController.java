package com.kyouhei.mathapp.controller.api;

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

import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;
import com.kyouhei.mathapp.service.TestSessionService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/session")
@AllArgsConstructor
public class TestSessionApiController {

	private TestSessionService testSessService;
	private HttpSession session;
	
	//テストセッション作成
	@PostMapping("/test")
	public ResponseEntity<TestSession> createTestSess(
					@RequestParam(defaultValue="false") boolean includeIntegers){
		User user=(User)session.getAttribute("user");
		
		TestSession testSess=testSessService.createTestSess(user, includeIntegers);
		return ResponseEntity.status(HttpStatus.CREATED).body(testSess);
	}
	
	//セッションの問題を全問取得
	@GetMapping("/{sessionId}/problems")
	public ResponseEntity<List<SessionProblem>> listProblem(
											@PathVariable Long sessionId){
//認可チェック
		List<SessionProblem> sps=testSessService.getSessionProblems(sessionId);
		
		return ResponseEntity.ok(sps);//status(200)
	}
											
}
