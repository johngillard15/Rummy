package com.actors;

import com.card.Card;
import com.game.Actor;
import com.game.Hand;
import com.utilities.Input;

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
    public byte getAction(Hand hand, Card faceUpCard){
        System.out.print("Action: ");
        return (byte)Input.getInt(1, 3);
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
