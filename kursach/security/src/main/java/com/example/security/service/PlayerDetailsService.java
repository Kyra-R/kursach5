package com.example.security.service;

import com.example.security.models.Player;
import com.example.security.repositories.PlayersRepository;
import com.example.security.security.PlayerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PlayerDetailsService implements UserDetailsService {

    private final PlayersRepository playersRepository;

    @Autowired
    public PlayerDetailsService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Player> person = playersRepository.findById(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PlayerDetails(person.get());
    }

}
