package com.kyouhei.mathapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem,Long> {

	/*リレーションあり:findBy[関連エンティティ名][そのエンティティのプロパティ名]
	  リレーションなし:findBy[problemエンティティのプロパティ名]*/
	List<Problem> findByCategoryId(int categoryId);
	
}
