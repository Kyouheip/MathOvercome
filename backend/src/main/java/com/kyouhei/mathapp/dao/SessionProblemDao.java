package com.kyouhei.mathapp.dao;

import java.util.Optional;

import com.kyouhei.mathapp.entity.SessionProblem;

public interface SessionProblemDao {
	
	  Optional<SessionProblem> findByIdx(Long sessionId, int idx);
	  
	  public int countBySessionId(Long sessionId);
	
}
