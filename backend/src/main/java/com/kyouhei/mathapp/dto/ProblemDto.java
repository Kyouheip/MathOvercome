package com.kyouhei.mathapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProblemDto {
	
	private Long id;
	private String question;
	private String hint;
	private List<ChoiceDto> choices;

}
