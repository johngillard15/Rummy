package com.game;

import com.card.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hand {
    private List<Card> cards = new ArrayList<>();

    public Hand(){

    }

    public int getDeadwood() {
        return cards.stream().mapToInt(card -> card.value);
    }
}
