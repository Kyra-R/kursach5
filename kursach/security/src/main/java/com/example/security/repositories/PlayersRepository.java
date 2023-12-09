package com.example.security.repositories;


import com.example.security.models.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayersRepository extends CrudRepository<Player, String> {
}
