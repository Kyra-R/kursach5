package com.example.security.service;

import com.example.security.models.Player;
import com.example.security.repositories.PlayersRepository;
import com.example.security.util.LoginAlreadyDefinedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PlayersService(PlayersRepository playersRepository, PasswordEncoder passwordEncoder) {
        this.playersRepository = playersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Player> findAllPlayerId() {
        return (List<Player>) playersRepository.findAll();
    }

    @Transactional
    public void register(Player player) {
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        player.setRole("ROLE_USER");
        player.setPlayerId(playersRepository.count());
        if (playersRepository.findById(player.getLogin()).isPresent()) {
            throw new LoginAlreadyDefinedException();
        }
        playersRepository.save(player);
    }

    @Transactional(readOnly = true)
    public Player readByLogin(String login) {
        return playersRepository.findById(login).orElseThrow(() -> new UsernameNotFoundException("Player not found"));
    }

    @Transactional
    public Player updateToModer(String login) {
        Player player = playersRepository.findById(login).orElseThrow(() -> new UsernameNotFoundException("Player not found"));
        player.setRole("ROLE_MODER");
        return playersRepository.save(player);
    }
}
