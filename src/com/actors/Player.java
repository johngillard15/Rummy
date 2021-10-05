package com.actors;

import com.game.Actor;

public class Player implements Actor {
    private final String name;
    private int score;

    public Player(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getAction() {
        return PASS;
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public void addScore() {

    }
}
