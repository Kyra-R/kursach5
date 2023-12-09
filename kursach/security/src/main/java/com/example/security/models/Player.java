package com.example.security.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@RedisHash("Player")
public class Player {
    @Id
    private String login;
    private String name;
    private String email;
    private String password;
    private String role;
    private long playerId;
}
