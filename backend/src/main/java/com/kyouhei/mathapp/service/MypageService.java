package com.kyouhei.mathapp.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kyouhei.mathapp.dao.MypageDao;
import com.kyouhei.mathapp.dto.CategoryDto;
import com.kyouhei.mathapp.dto.ProblemCategoryDto;
import com.kyouhei.mathapp.dto.TestSessionDto;
import com.kyouhei.mathapp.dto.UserDto;
import com.kyouhei.mathapp.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private final MypageDao mypageDao;
	
	public UserDto getUserData(User user) {
		Long userId = user.getId();
		List<Object[]> sessionData = mypageDao.getSessionProblems(userId);
		
		Map<Long,TestSessionDto> sessionMap = new LinkedHashMap<>();
		
		for(Object[] row : sessionData) {
								//LongでもIntegerでもいいように
			Long sessionId = ((Number) row[0]).longValue();
			String startTime = row[1].toString();
			Boolean isCorrect = row[2] != null ? (Boolean) row[2] : false;
			String categoryName = (String) row[3];
			
			//1対多データを受け取るときの定番コード
			TestSessionDto session = 
					sessionMap.computeIfAbsent(sessionId, id ->{
				TestSessionDto dto = new TestSessionDto();
		        dto.setSessionId(id);
		        dto.setStartTime(startTime);
		        dto.setProbCategoryDtos(new ArrayList<>());
		        return dto;
						});
			
			ProblemCategoryDto probDto = new ProblemCategoryDto();
			
			probDto.setIsCorrect(isCorrect);
            probDto.setCategoryName(categoryName);
            session.getProbCategoryDtos().add(probDto);	
		}
		//N+1問題　クエリ改善余地あり
		for (TestSessionDto session : sessionMap.values()) {
            int total = session.getProbCategoryDtos().size();
            int correct = 0;
            for(ProblemCategoryDto dto : session.getProbCategoryDtos()) {
            	if(dto.getIsCorrect()) correct++;
            }
            session.setTotal(total);
            session.setCorrectCount(correct);
		
            List<Object[]> categoryStats = mypageDao.getCategories(session.getSessionId());
            List<CategoryDto> categoryDtos = new ArrayList<>();
            for(Object[] row : categoryStats) {
            	CategoryDto dto = new CategoryDto();
            	dto.setCategoryName((String) row[0]);
                dto.setTotal(((Number) row[1]).intValue());
                dto.setCorrectCount(((Number) row[2]).intValue());
                categoryDtos.add(dto);
            }
            session.setCategoryDtos(categoryDtos);
            
            List<String> weak = mypageDao.getWeakCategories(session.getSessionId());
            session.setWeakCategories(weak);
		}
		
		UserDto userDto = new UserDto();
        userDto.setTestSessDtos(new ArrayList<>(sessionMap.values()));
        userDto.setUserName(user.getUserName());
        return userDto;
		
	}

}
