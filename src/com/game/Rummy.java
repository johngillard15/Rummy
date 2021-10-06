package com.game;

import com.actors.Player;
import com.card.Card;
import com.deck.Deck;
import com.deck.StandardDeck;
import com.utilities.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Rummy</h1>
 *
 * <p>Play Gin Rummy!</p>
 *
 * @author John Gillard
 * @since 4/10/2021
 * @version 0.1.0
 */

/*
 * Run - three or more cards in a row of the same suit (straight flush). Not limited to 5 cards.
 * Set - three or four cards of the same rank (three/four of a kind)
 * Meld - term for sets and runs (ex. 3 melds, made up of 2 sets and 1 run)
 *
 * - each player is dealt 10 cards (2 players, more players may mean fewer cards); remaining deck is placed in the
 * center, and one card is drawn to make the discard pile
 * - first action - non-dealer decides if they want the face up card. If they pass, the dealer has the option to take
 *  it. If the dealer passes as well, player 2 will draw from the deck.
 * - each turn - player can draw from either the face-up or face-down pile. If drawing from face-down, player can
 * decide to discard to face-up or their hand. If a player takes a card for their hand, they must also discard
 * something.
 * - round ends when a player knocks, and can only knock when their unmatched cards (deadwood) add up to 10 or less
 * - scores are based on unmatched player cards
 * - if the knocking player has a lower deadwood score than their opponent, they are awarded the difference to their
 * score.
 * - When a player knocks, the other player may play their unmatched cards against the knocker's matched cards (the
 * layoff). This occurs before dealing score. Knocking player cannot lay off cards. Layoffs cannot be applied to
 * unmatched cards.
 * - if the knocking player has an equal or higher deadwood score than their opponent, the opponent gets the difference
 *  as well as a 25(10? 20? idk) point bonus (undercut)
 * - in short, the player with the lower(or equal if they didn't knock) deadwood is awarded the difference. If the
 * winning player didn't knock, they also get a 25(10?) point bonus.
 *  also get
 * - round can also end by going gin
 * - going gin means having every card in hand as a part of a meld. No layoffs allowed.
 * - player with gin gets the value of opponents unmatched cards in addition to a 25(20?) point bonus
 * - if the draw deck gets down to 2 cards without a knock, the round is void
 * - first player to 100 points wins
 */

public class Rummy {
    private Deck deck;
    private final List<Card> discardPile = new ArrayList<>();
    Hand player1, player2;
    public static final int UNDERCUT = 10;
    public static final int GIN = 20;
    public static final int BIG_GIN = 10;

    public Rummy(){

    }

    public void play(){
        do{
            setup();
            while(round());

            System.out.println("Would you like to play again? (y/n)");
        }while(Input.getBoolean("y", "n"));
    }

    private void setup(){
        player1.clear();
        player2.clear();

        discardPile.clear();
        deck = new StandardDeck();
        deck.shuffle();

        for(int i = 0; i < 10; i++){
            player1.addCard(deck.draw());
            player2.addCard(deck.draw());
        }

        discardPile.add(deck.draw());
    }

    private boolean round(){

        while(turn(player1) && turn(player2));

        return true;
    }

    private boolean turn(Hand activePlayer){
        System.out.printf("\n- %s turn -\n", activePlayer.getName());

        byte action = activePlayer.getAction(activePlayer, discardPile.get(discardPile.size() - 1));
        return switch(action){
            case Actor.PASS -> true;
            case Actor.DRAW_STOCK -> true;
            case Actor.DRAW_DISCARD -> true;
            case Actor.KNOCK -> false;
            default -> throw new IllegalStateException("Invalid action: " + action);
        };
    }

    private void displayResults(){

    }
}
