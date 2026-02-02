package com.kyouhei.mathapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {

	private String userName;
	private List<TestSessionDto> testSessDtos;
}
