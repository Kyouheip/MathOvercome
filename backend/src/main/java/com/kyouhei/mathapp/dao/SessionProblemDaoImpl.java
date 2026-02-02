package com.kyouhei.mathapp.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.SessionProblem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SessionProblemDaoImpl implements SessionProblemDao {
	
	private final EntityManager em;
	
	 @Override
	 public Optional<SessionProblem> findByIdx(Long sessionId,int idx){
		 
		String sql = """
					 SELECT * FROM sessionproblems
				     WHERE session_id = :sessionId
				     ORDER BY id
				     LIMIT 1 OFFSET :idx
					 """;
		
		Query query = em.createNativeQuery(sql,SessionProblem.class);
		query.setParameter("sessionId", sessionId)
        	 .setParameter("idx", idx);
		
		@SuppressWarnings("unchecked")			//getSingleResult()は例外処理必要
		List<SessionProblem> someSessProb = query.getResultList();
		
		return someSessProb.isEmpty() ? Optional.empty() : Optional.of(someSessProb.get(0));
		
	 }
	 
	// 問題数取得
	 @Override
	 public int countBySessionId(Long sessionId) {
	     String sql = "SELECT COUNT(*) FROM sessionproblems WHERE session_id = :sessionId";
	     
	     Query query = em.createNativeQuery(sql)
	    		 .setParameter("sessionId", sessionId);
	     
	     
	     Number number = (Number)query.getSingleResult();
	     int count = number.intValue();
	     
	     return count;
	 }


}
