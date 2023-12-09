package com.example.security.security;

import com.example.security.models.Player;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PlayerDetails implements UserDetails {
    private final Player player;

    public PlayerDetails(Player player) {
        this.player = player;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(player.getRole()));
    }

    @Override
    public String getPassword() {
        return this.player.getPassword();
    }

    @Override
    public String getUsername() {
        return this.player.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
