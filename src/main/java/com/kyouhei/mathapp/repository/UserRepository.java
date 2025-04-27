package com.kyouhei.mathapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyouhei.mathapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByUserId(String userId);
}
