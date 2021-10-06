package com.actors;

import com.card.Card;
import com.game.Actor;
import com.game.Hand;

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
    public byte getAction(Hand hand, Card faceUpCard) {
        return PASS;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addScore(int points) {
        score += points;
    }
}
