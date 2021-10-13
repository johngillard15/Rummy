package com.card;

public class Card {
    public final String suit;
    public final int rank;

    public Card(String suit, int rank){
        this.suit = suit;
        this.rank = rank;
    }

    public String rankName(){
        return switch(rank){
            case 1 -> "Ace";
            case 11 -> "Jack";
            case 12 -> "Queen";
            case 13 -> "King";
            default -> String.valueOf(rank);
        };
    }

    @Override
    public String toString() {
        return String.format("%s of %ss", rankName(), suit);
    }
}