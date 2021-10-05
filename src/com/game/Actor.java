package com.game;

public interface Actor {
    Hand hand = new Hand();
    byte PASS = 0;
    byte DRAW_STOCK = 1;
    byte DRAW_DISCARD = 2;
    byte KNOCK = 3;

    String getName();
    byte getAction();
    int getScore();
    void addScore();
}
