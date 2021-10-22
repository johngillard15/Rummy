package com.deck;

import com.card.Card;

public interface Deck {

    void shuffle();
    Card draw();

    int size();
}
