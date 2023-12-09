package com.rschir.prac.controllers;

import com.rschir.prac.DTO.GameSessionDTO;

import com.rschir.prac.model.GameSession;

import com.rschir.prac.services.GameSessionService;

import com.rschir.prac.util.exceptions.ForbiddenActionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/game_sessions")
@CrossOrigin("*")
public class GameSessionController {
    GameSessionService gameSessionService;

    @Autowired
    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }


    @GetMapping("/info")
    public List<GameSession> getGameSession() {
        return gameSessionService.readAll();
    }

    @GetMapping("/{id}/info")
    public GameSession getGameSessionById(@PathVariable long id) {
        return gameSessionService.read(id);
    }

    @GetMapping("/{id}")
    public String getGameSessionTable(@PathVariable long id) {
        return gameSessionService.get_game_field(id);
    }

    @GetMapping("/help")
    public String getGameSessionTableHelp() {
        return gameSessionService.get_help();
    }


    @PostMapping
    public ResponseEntity<GameSessionDTO> postGameSession(@RequestBody GameSessionDTO newGameSession) {
        gameSessionService.create(newGameSession);
        return new ResponseEntity<>(newGameSession, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/x_player/{x_id}")
    @PreAuthorize("hasRole('MODER') or hasRole('ADMIN')")
    public GameSession patchGameSessionX(@PathVariable long id, @PathVariable long x_id) {
        return gameSessionService.update_player_x(id, x_id);
    }

    @PutMapping("/{id}/o_player/{o_id}")
    @PreAuthorize("hasRole('MODER') or hasRole('ADMIN')")
    public GameSession patchGameSessionO(@PathVariable long id, @PathVariable long o_id) {
        return gameSessionService.update_player_o(id, o_id);
    }

    @PutMapping("/{id}/{cell}")
    public String nextTurn(@PathVariable long cell, @PathVariable long id) {
        Long playerId = (Long)SecurityContextHolder.getContext().getAuthentication().getDetails();
        gameSessionService.next_turn((int) cell, id, playerId);
        return gameSessionService.get_game_field(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteGameSession(@PathVariable long id) {
        gameSessionService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    private ResponseEntity<String> handleForbiddenException(ForbiddenActionException response) {
        return new ResponseEntity<>(response.getMessage(), HttpStatus.FORBIDDEN);
    }

}
