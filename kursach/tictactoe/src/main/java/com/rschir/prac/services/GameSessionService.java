package com.rschir.prac.services;

import com.rschir.prac.DTO.GameSessionDTO;
import com.rschir.prac.model.GameSession;

import com.rschir.prac.repositories.GameSessionRepository;

import com.rschir.prac.util.exceptions.ForbiddenActionException;

import com.rschir.prac.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Transactional(readOnly = true)
    public GameSession read(long id) {
        return gameSessionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<GameSession> readAll() {
        return gameSessionRepository.findAll();
    }


    @Transactional
    public GameSession create(GameSessionDTO givenGameSession) {

        if (givenGameSession.getX_id() == givenGameSession.getO_id())
            throw new ForbiddenActionException("Can't play with yourself");
        GameSession newGameSession = new GameSession();
        newGameSession.setX_id(givenGameSession.getX_id());
        newGameSession.setO_id(givenGameSession.getO_id());
        newGameSession.setGameStage(new StringBuilder("         "));
        newGameSession.setGame_stats(new int[8]);
        newGameSession.setWon((long) -2);
        newGameSession.setTurn(givenGameSession.getX_id());

        return gameSessionRepository.save(newGameSession);
    }

    @Transactional
    public GameSession update_player_x(long id, long x_id) {
        GameSession updatedGameSession = gameSessionRepository.findById(id).orElseThrow(NotFoundException::new);
        if (updatedGameSession.getO_id() == x_id)
            throw new ForbiddenActionException("Can't play with yourself");
        updatedGameSession.setSessionId(id);
        if(updatedGameSession.getTurn() == updatedGameSession.getX_id())
            updatedGameSession.setTurn(x_id);
        updatedGameSession.setX_id(x_id);

        return gameSessionRepository.save(updatedGameSession);
    }

    @Transactional
    public GameSession update_player_o(long id, long o_id) {
        GameSession updatedGameSession = gameSessionRepository.findById(id).orElseThrow(NotFoundException::new);
        if (updatedGameSession.getX_id() == o_id)
            throw new ForbiddenActionException("Can't play with yourself");
        updatedGameSession.setSessionId(id);
        if(updatedGameSession.getTurn() == updatedGameSession.getO_id())
            updatedGameSession.setTurn(o_id);
        updatedGameSession.setO_id(o_id);

        return gameSessionRepository.save(updatedGameSession);
    }

    @Transactional
    public void next_turn(int cell, long id, Long playerId) {
        GameSession gameSession = gameSessionRepository.findById(id).orElseThrow(NotFoundException::new);
        if (gameSession.getWon() > -2)
            throw new ForbiddenActionException("Game has already ended");
        if (cell < 1 || cell > 9)
            throw new ForbiddenActionException("Wrong coordinates for writing");
        if (gameSession.getGameStage().charAt(cell-1) != ' ')
            throw new ForbiddenActionException("Can't write here");
        if (gameSession.getTurn() != playerId)
           throw new ForbiddenActionException("It's not your turn");

        if(gameSession.getTurn() == gameSession.getX_id()) {
            gameSession.getGameStage().setCharAt(cell-1,'x');
            gameSession.setTurn(gameSession.getO_id());
            gameSession.setGame_stats(add_stats(gameSession.getGame_stats(), cell, true));
            if(check_won(gameSession.getGame_stats())){
                gameSession.setWon(gameSession.getX_id());
            };
        } else {
            gameSession.getGameStage().setCharAt(cell-1,'o');
            gameSession.setTurn(gameSession.getX_id());
            gameSession.setGame_stats(add_stats(gameSession.getGame_stats(), cell, false));
            if(check_won(gameSession.getGame_stats())){
                gameSession.setWon(gameSession.getO_id());
            };
        }
        if(none_won(gameSession.getGameStage())){
            gameSession.setWon(-1L);
        }
        gameSessionRepository.save(gameSession);

    }

    @Transactional
    public String get_game_field(long id) {
        GameSession gameSession = gameSessionRepository.findById(id).orElseThrow(NotFoundException::new);
        return give_field(gameSession.getGameStage(), gameSession.getWon() != -2);
    }


    public String get_help() {
        return give_field(new StringBuilder("123456789"), false);
    }


    private int[] add_stats(int[] stats, int cell, boolean x_player){

            if(x_player){
                stats[(cell-1) % 3]++; //column 0-2 starts with 0
                stats[(cell-1) / 3 + 3]++; //rows 3-5 starts with 0
                if(cell % 2 == 1) {
                    if (cell == 5) {
                        stats[7]++;
                        stats[6]++;
                    } else if (cell == 1 || cell == 9) {
                        stats[7]++;
                    } else if (cell == 7 || cell == 3) {
                        stats[6]++;
                    }
                }
            } else {
                stats[(cell-1) % 3]--;
                stats[(cell-1) / 3 + 3]--;
                if(cell % 2 == 1) {
                    if (cell == 5) {
                        stats[7]--;
                        stats[6]--;
                    } else if (cell == 1 || cell == 9) {
                        stats[7]--;
                    } else if (cell == 7 || cell == 3) {
                        stats[6]--;
                    }
                }
            }

        return stats;
    }

    private boolean check_won(int[] stats){
        for(int i : stats) {
            if (i == 3 || i == -3) {
                return true;
            }
        }
        return false;
    }

    private boolean none_won(StringBuilder str){
        if (str.indexOf(" ") == -1){
            return true;
        }
        return false;
    }

    private String give_field(StringBuilder str, boolean ended){
        StringBuilder field = new StringBuilder("");
        field.append("_______\n");
        field.append("|" + str.charAt(0) + "|" + str.charAt(1) + "|" + str.charAt(2) + "|\n");
        field.append("_______\n");
        field.append("|" + str.charAt(3) + "|" + str.charAt(4) + "|" + str.charAt(5) + "|\n");
        field.append("_______\n");
        field.append("|" + str.charAt(6) + "|" + str.charAt(7) + "|" + str.charAt(8) + "|\n");
        field.append("_______\n");
        if(ended){
            field.append("Game has ended.");
        }
        return field.toString();
    }


    @Transactional
    public void delete(long id) {
        gameSessionRepository.deleteById(id);
    }



}
