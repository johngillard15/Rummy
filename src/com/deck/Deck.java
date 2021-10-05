package com.deck;

import com.card.Card;

public interface Deck {

    void shuffle();
    Card draw();
    void discard(Card card);
}
