package com.example.security.controller;

import com.example.security.dto.AuthenticationDTO;
import com.example.security.dto.PlayerDTO;
import com.example.security.models.Player;
import com.example.security.security.JWTUtil;
import com.example.security.service.PlayersService;
import com.example.security.util.ErrorResponse;
import com.example.security.util.LoginAlreadyDefinedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PlayersService playersService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PlayersService playersService, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.playersService = playersService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody PlayerDTO playerDTO) {
        Player player = convertToPlayer(playerDTO);
        playersService.register(player);
        String token = jwtUtil.generateToken(player.getLogin(), player.getRole(), player.getPlayerId());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        Player player = playersService.readByLogin(authenticationDTO.getLogin());
        String token = jwtUtil.generateToken(authenticationDTO.getLogin(), player.getRole(), player.getPlayerId());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/moderator")
    public Map<String, String> setModerRole(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        Player player = playersService.updateToModer(authenticationDTO.getLogin());
        String token = jwtUtil.generateToken(authenticationDTO.getLogin(), player.getRole(), player.getPlayerId());
        return Map.of("jwt-token", token);
    }

    @GetMapping
    public List<Player> GetPlayers() {
        return playersService.findAllPlayerId();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(LoginAlreadyDefinedException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public Player convertToPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setLogin(playerDTO.getLogin());
        player.setName(playerDTO.getName());
        player.setEmail(playerDTO.getEmail());
        player.setPassword(playerDTO.getPassword());
        return player;
    }
}
