package com.kyouhei.mathapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sessionproblems")
@Data
public class SessionProblem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id")
	private TestSession testSession;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id")
	private Problem problem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selected_choice_id")
	private Choice selectedChoice;
	
	@Column(name = "is_correct")
	private Boolean isCorrect;
	
	
}
