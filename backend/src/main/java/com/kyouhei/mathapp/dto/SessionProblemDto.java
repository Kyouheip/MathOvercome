package com.kyouhei.mathapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionProblemDto {

	private Long id;
	private String question;
	private List<ChoiceDto> choices;
	private String hint;
	private Long selectedId;
	private int total;
}
