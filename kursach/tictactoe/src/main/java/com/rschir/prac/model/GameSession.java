package com.rschir.prac.model;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "game_stage")
    private StringBuilder gameStage; //--- --- ---

    @Column(name = "player_x")
    private Long x_id;

    @Column(name = "player_o")
    private Long o_id;

    @Column(name = "turn")
    private Long turn;

    @Column(name = "won")
    private Long won;

    @Type(IntArrayType.class)
    @Column(
            name = "game_stats",
            columnDefinition = "integer[]"
    )
    private int[] game_stats;

}
