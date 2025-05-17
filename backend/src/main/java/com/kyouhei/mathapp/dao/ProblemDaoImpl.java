package com.kyouhei.mathapp.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.Problem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemDaoImpl implements ProblemDao {

	private final EntityManager em;
	
	@Override
	public List<Problem> findProblemsPerCategory(List<Integer> categoryIds, int countPerCategory){
		StringBuilder sql = new StringBuilder();
		//sqlインジェクションの可能性なし
		//今後クエリが増えるならバインド変数とプレースホルダを使いキャッシュを効かせる
		for (int i : categoryIds) {
			if (i > 1) sql.append(" UNION ALL ");
			sql.append("(SELECT id FROM problems WHERE category_id = ")
			.append(i)
            .append(" ORDER BY RAND() LIMIT ")
            .append(countPerCategory)
            .append(")");
		}
		
		@SuppressWarnings("unchecked")
		 List<Number> problemIds = em.createNativeQuery(sql.toString()).getResultList();
		 
		 List<Problem> problems = new ArrayList<>();
		 for (Number id : problemIds) {
			 								//第１引数はentity,第２は主キーの値
	            problems.add(em.getReference(Problem.class, id.longValue()));
	        }
		 
		 return problems;
	}
	
	
}
