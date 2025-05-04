package com.kyouhei.mathapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.Choice;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice,Long>{

	List<Choice> findByProblemId(Long problemId);
	
}
