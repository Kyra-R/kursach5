package com.rschir.prac.repositories;

import com.rschir.prac.model.GameSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

}