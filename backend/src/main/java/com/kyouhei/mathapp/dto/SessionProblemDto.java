package com.kyouhei.mathapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class SessionProblemDto {

	private Long id;
	private String question;
	private List<String> choiceTexts;
}
