package com.kyouhei.mathapp.dao;

import java.util.List;

import com.kyouhei.mathapp.entity.Problem;

public interface ProblemDao {

	List<Problem> findProblemsPerCategory(List<Integer> categoryIds,int countCategory);
}
