package com.kyouhei.mathapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class TestSessionDto {

	 private Long sessionId;
	 private String startTime;
	 private int total; //問題数
	 private int correctCount; //正解数
	 private List<ProblemCategoryDto> probCategoryDtos;
	 private List<CategoryDto> categoryDtos;
	 private List<String> weakCategories;
	
}
