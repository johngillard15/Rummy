package com.deck;

import com.card.Card;
import com.utilities.Input;

import java.util.Arrays;

public class TestDeck implements Deck{
    public static final String[] SUITS = {
            "Clubs", "Diamonds", "Hearts", "Spades"
    };
    public static final int[] VALUES = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
    };

    @Override
    public void shuffle(){}

    @Override
    public Card draw() {
        System.out.println(Arrays.toString(VALUES));
        System.out.println("Which value? (1-13)");
        int valueIndex = Input.getInt(1, VALUES.length) - 1;

        System.out.println(Arrays.toString(SUITS));
        System.out.println("Which suit? (1-4)");
        int suitIndex = Input.getInt(1, SUITS.length) - 1;

        return new Card(SUITS[suitIndex], VALUES[valueIndex]);
    }
}
