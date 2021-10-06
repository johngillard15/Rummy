package com.game;

import com.card.Card;

public interface Actor {
    byte PASS = 0;
    byte DRAW_STOCK = 1;
    byte DRAW_DISCARD = 2;
    byte KNOCK = 3;

    String getName();
    byte getAction(Hand hand, Card faceUpCard);
    int getScore();
    void addScore(int points);
}
