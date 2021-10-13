package com.game;

import com.actors.Player;
import com.card.Card;
import com.deck.CheaterStandardDeck;
import com.deck.Deck;
import com.deck.StandardDeck;
import com.utilities.CLI;
import com.utilities.Input;
import com.utilities.UI;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Rummy</h1>
 *
 * <p>Play Gin Rummy!</p>
 *
 * @author John Gillard
 * @since 4/10/2021
 * @version 0.7.2
 */

/*
 * Run - three or more cards in a row of the same suit (straight flush). Not limited to 5 cards.
 * Set - three or four cards of the same rank (three/four of a kind)
 * Meld - term for sets and runs (ex. 3 melds, made up of 2 sets and 1 run)
 *
 * Setting up rounds
 * - each player is dealt 10 cards (2 players, more players may mean fewer cards); remaining deck is placed in the
 * center, and one card is drawn to make the discard pile
 * - first action - non-dealer decides if they want the face up card. If they pass, the dealer has the option to take
 *  it. If the dealer passes as well, player 2 will draw from the deck.
 *
 * Turn
 * - each turn - player can draw from either the face-up or face-down pile. If drawing from face-down, player can
 * decide to discard to face-up or their hand. If a player takes a card for their hand, they must also discard
 * something.
 *
 * Knocking
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
 *
 * Going Gin
 * - round can also end by going gin
 * - going gin means having every card in hand as a part of a meld. No layoffs allowed.
 * - player with gin gets the value of opponents unmatched cards in addition to a 25(20?) point bonus
 * - if the draw deck gets down to 2 cards without a knock, the round is void
 *
 * if deck gets down to 2 cards, the round is void and ends.
 *
 * Win Conditions
 * - first player to 100 points wins
 */

public class Rummy {
    private Deck deck;
    private final List<Card> discardPile = new ArrayList<>(); // TODO: turn discardPile into a stack?
    Hand player1, player2;
    public static final int UNDERCUT = 10;
    public static final int GIN = 20;
    public static final int BIG_GIN = 30;

    public Rummy(){
        player1 = new Hand(new Player("P1"));
        player2 = new Hand(new Player("P2"));

        System.out.println("--- Gin Rummy ---");
        CLI.pause();
    }

    public void play(){
        do{
            while(round());

            System.out.printf("Player %d wins!\n", player1.getScore() >= 100 ? 1 : 2);

            System.out.println("Would you like to play again? (y/n)");
        }while(Input.getBoolean("y", "n"));
    }

    private void firstDraw(){
        System.out.println(StandardDeck.getCardGUI(discardPile.get(0)));
        System.out.println("\nTake face up card? (y/n)");
        if(Input.getBoolean("y", "n"))
            player1.addCard(discardPile.get(0));
        else
            player2.addCard(discardPile.get(0));
    }

    private void setup(){
        player1.clear();
        player2.clear();

        discardPile.clear();
        deck = new CheaterStandardDeck(1);
        deck.shuffle();

        for(int i = 0; i < 10; i++){
            player1.addCard(deck.draw());
            player2.addCard(deck.draw());
        }

        discardPile.add(deck.draw());
    }

    private boolean round(){
        setup();
//        firstDraw();

        while(turn(player1) && turn(player2));

        return player1.getScore() < 100 && player2.getScore() < 100;
    }

    private Card getFaceUpCard(){
        return discardPile.get(discardPile.size() - 1);
    }

    private boolean turn(Hand activePlayer){
        CLI.cls();
        System.out.printf("\n- Player %d's turn -\n", activePlayer == player1 ? 1 : 2);
        CLI.pause();

        activePlayer.sortByValue();
        activePlayer.sortBySuit();
        boolean drawing = false;
        do{
            UI.showSideBySide(StandardDeck.cardBack, StandardDeck.getCardGUI(getFaceUpCard()));
            StandardDeck.showHand(activePlayer.getCards());

            System.out.println("Would you like to sort your cards or draw?");
            System.out.println("1. Sort by Value | 2. Sort by Suit | 3. Draw");
            int choice = Input.getInt(1, 3);
            switch (choice) {
                case 1 -> activePlayer.sortByValue();
                case 2 -> activePlayer.sortBySuit();
                case 3 -> drawing = true;
            }

        }while(!drawing);

        System.out.println("Draw from stock or discard pile, or knock");
        System.out.println("1. Draw from Stock | 2. Draw from Discard Pile | 3. Knock"); // 0 for cheatDraw
        byte action = activePlayer.getAction(activePlayer, getFaceUpCard());
        boolean knocked = switch(action){
            case 0, Actor.DRAW_STOCK -> {
                System.out.println("Drawing from stock");

                if(deck instanceof CheaterStandardDeck && action == 0)
                    activePlayer.addCard(((CheaterStandardDeck) deck).cheatDraw());
                else
                    activePlayer.addCard(deck.draw());

                yield false;
            }
            case Actor.DRAW_DISCARD -> {
                System.out.println("Drawing from discard pile");
                activePlayer.addCard(getFaceUpCard());
                discardPile.remove(discardPile.size() - 1);
                yield false;
            }
            case Actor.KNOCK -> {
                System.out.println("Knock!");
                yield true;
            }
            default -> throw new IllegalStateException("Invalid action: " + action);
        };

        if(knocked)
            knock(activePlayer == player1 ? 1 : 2);
        else{
            StandardDeck.showHand(activePlayer.getCards());

            StringBuilder cardNumbers = new StringBuilder();
            for(int i = 1; i <= 11; i++)
                cardNumbers.append(String.format("     %s     ", i < 10 ? i + " " : i));
            System.out.println(cardNumbers);
            System.out.println(" ".repeat(123) + " Drawn");

            System.out.println("Select a card to get rid of:");
            discardPile.add(activePlayer.removeCard(Input.getInt(1, 11) - 1));
        }

        activePlayer.selectMelds();
        CLI.pause();

        return !knocked;
    }

    private void knock(int knocker){
        System.out.println("\n--- Round Results ---\n");

        System.out.printf("\nPlayer 1%s deadwood: %d\n", knocker == 1 ? " (knocked)" : "", player1.getDeadwood());
        System.out.printf("score: %d\n", player1.getScore());
        System.out.printf("Player 2%s deadwood: %d\n", knocker == 2 ? " (knocked)" : "", player2.getDeadwood());
        System.out.printf("score: %d\n", player2.getScore());

        int winner = player1.getDeadwood() < player2.getDeadwood() ? 1 : 2;
        System.out.printf("\nPlayer %d wins the round!\n", winner);
        if(winner == 1)
            player1.addScore(player2.getDeadwood() - player1.getDeadwood());
        else
            player2.addScore(player1.getDeadwood() - player2.getDeadwood());

        if(winner != knocker){
            System.out.printf("+%d points for undercut\n", UNDERCUT);
            if(winner == 1)
                player1.addScore(UNDERCUT);
            else
                player2.addScore(UNDERCUT);
        }

        System.out.println("\nNew scores");
        System.out.printf("Player 1 score: %d\n", player1.getScore());
        System.out.printf("Player 2 score: %d\n", player2.getScore());
        CLI.pause();
    }
}
