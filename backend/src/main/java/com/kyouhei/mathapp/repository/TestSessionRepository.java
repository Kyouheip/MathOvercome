package com.kyouhei.mathapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.TestSession;
import com.kyouhei.mathapp.entity.User;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession,Long>{

	List<TestSession> findByUser(User user);
	
}
