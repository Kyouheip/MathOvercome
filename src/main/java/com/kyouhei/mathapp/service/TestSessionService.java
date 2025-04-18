package com.kyouhei.mathapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kyouhei.mathapp.entity.Problem;
import com.kyouhei.mathapp.entity.SessionProblem;
import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.repository.ProblemRepository;
import com.kyouhei.mathapp.repository.SessionProblemRepository;
import com.kyouhei.mathapp.repository.TestSessionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TestSessionService {

	private TestSessionRepository testSessionRepository;
	private ProblemRepository problemRepository;
	private SessionProblemRepository sessionProblemRepository;
	
	public TestSession createTestSession(Long userId,boolean includeIntegers) {
		
		//TestSessionを作成しDBに保存
		TestSession session=new TestSession();
		session.setUserId(userId);
		session.setIncludeIntegers(includeIntegers);
		testSessionRepository.save(session);
		
		/*出題数を決定
			整数分野を含む:カテゴリ1～７から出題
			整数分野を含まない:カテゴリ1～6から出題*/
		List<Integer> categories=new ArrayList<>();
		int j=6;
		if(includeIntegers) j++;
		for(int i=1;i<=j;i++)categories.add(i);
		
		//出題する全ての問題数分のSessionProblem作成
		List<SessionProblem> sessionProblems=new ArrayList<>();
		for(Integer cateId:categories) {
//全てのカテゴリの問題をもってくるため改善余地あり
			List<Problem> problems=problemRepository.findByCategoryId(cateId);
			Collections.shuffle(problems);
			for(int i=0;i<2;i++) {
				SessionProblem sp=new SessionProblem();
				sp.setTestSession(session);
                sp.setProblem(problems.get(i));
                sessionProblems.add(sp);
            }
        }
        //SessionProblemをDBに保存、TestSessionのフィールドに代入
        sessionProblemRepository.saveAll(sessionProblems);
        session.setSessionProblems(sessionProblems);
        return session;
    }
}
			