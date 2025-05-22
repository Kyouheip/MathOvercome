package com.kyouhei.mathapp.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MypageDao {

	private final EntityManager em;
	
	public List<Object[]> getSessionProblems(Long userId){
		String sql = """
				SELECT ts.id AS session_id,
					   ts.start_time,
					   sp.is_correct,
					   c.name AS category_name
				FROM testsessions AS ts
				INNER JOIN sessionproblems AS sp ON ts.id = sp.session_id
				INNER JOIN problems AS p ON sp.problem_id = p.id
				INNER JOIN categories AS c ON p.category_id = c.id
				WHERE ts.user_id = :userId
				ORDER BY ts.start_time DESC
					 """;
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> response = query.getResultList();
		return response;
	}
	
	//あるtestsessionのカテゴリ統計
	public List<Object[]> getCategories(Long sessionId){
		String sql = """
				SELECT c.name,
                       COUNT(sp.id) AS total_count,
                       SUM(CASE WHEN sp.is_correct = true 
				                THEN 1 ELSE 0 END) AS correct_count
                FROM sessionproblems AS sp
                INNER JOIN problems AS p ON sp.problem_id = p.id
                INNER JOIN categories AS c ON p.category_id = c.id
                WHERE sp.session_id = :sessionId
                GROUP BY c.name
				     """;
		
		Query query = em.createNativeQuery(sql);
		query.setParameter("sessionId",sessionId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> response = query.getResultList();
		return response;
	}
	
	public List<String> getWeakCategories(Long sessionId){
		String sql = """
				SELECT c.name
				FROM sessionproblems AS sp
                INNER JOIN problems AS p ON sp.problem_id = p.id
                INNER JOIN categories AS c ON p.category_id = c.id
                WHERE sp.session_id = :sessionId
                GROUP BY c.name
                ORDER BY SUM(CASE WHEN sp.is_correct = true THEN 1 ELSE 0 END) * 1.0 / COUNT(*) ASC
                LIMIT 2
			    	""";
		
		Query query = em.createNativeQuery(sql);
		query.setParameter("sessionId", sessionId);
		
		@SuppressWarnings("unchecked")
		List<String> response = query.getResultList();
		return response;
	}
}
