package com.example.asm_java5.service;

import com.example.asm_java5.entities.GameAccount;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartService {
    private Map<Integer, GameAccount> items = new HashMap<>();

    public void add(GameAccount game) {
        items.put(game.getGameAccId(), game);
    }

    public Collection<GameAccount> getItems() {
        return items.values();
    }

    public double getTotal() {
        return items.values().stream().mapToDouble(GameAccount::getPrice).sum();
    }

    public void clear() {
        items.clear();
    }
    public void remove(Integer gameAccId) {
        items.remove(gameAccId);
    }
}

