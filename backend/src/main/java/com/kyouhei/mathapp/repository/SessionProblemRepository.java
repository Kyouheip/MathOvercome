package com.kyouhei.mathapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.SessionProblem;

@Repository
public interface SessionProblemRepository 
					extends JpaRepository<SessionProblem,Long> {
	
	List<SessionProblem> findByTestSessionId(Long testSessionId);

}
