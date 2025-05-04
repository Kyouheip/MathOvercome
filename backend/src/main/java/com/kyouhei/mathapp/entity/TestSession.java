package com.kyouhei.mathapp.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="testsessions")
@Data
@ToString(exclude="sessionProblems")
public class TestSession {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="include_integers")
	private boolean includeIntegers;
	
	@Column(name="start_time")
	private LocalDateTime startTime=LocalDateTime.now();
	
	@OneToMany(mappedBy="testSession",cascade=CascadeType.ALL)
	@OrderBy("id ASC")
	private List<SessionProblem> sessionProblems;
	
}
