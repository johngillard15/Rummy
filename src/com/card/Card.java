package com.card;

public class Card {
    public final String suit;
    public final int value;

    public Card(String suit, int value){
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%d of %s", value, suit);
    }
}