package com.game;

import com.card.Card;

import java.util.List;

public interface Actor {
    byte DRAW_STOCK = 1;
    byte DRAW_DISCARD = 2;
    byte KNOCK = 3;

    String getName();
    byte getAction(Hand hand, Card faceUpCard);
    int pickCard(List<Card> cards);
    int pickMeld(List<List<Card>> melds);
    int getScore();
    void addScore(int points);
}
