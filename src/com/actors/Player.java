package com.actors;

import com.card.Card;
import com.game.Actor;
import com.game.Hand;
import com.utilities.Input;

import java.util.List;
import java.util.Objects;

public class Player implements Actor {
    private final String name;
    private int score;

    public Player(String name){
        this.name = name;
        score = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getAction(Hand hand, Card faceUpCard){
        System.out.print("Action: ");
        return (byte)Input.getInt(0, 3);
    }

    @Override
    public int pickCard(List<Card> cards) {
        return Input.getInt(1, cards.size()) - 1;
    }

    @Override
    public int pickMeld(List<List<Card>> melds){
        System.out.println("Pick a meld to use:");
        return Input.getInt(0, melds.size()) - 1;
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
