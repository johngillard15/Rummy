package com.game;

import com.actors.Player;
import com.card.Card;
import com.deck.CheaterStandardDeck;
import com.deck.Deck;
import com.deck.StandardDeck;
import com.utilities.CLI;
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
 * @version 0.12.7
 */

/*
 * Setting up rounds
 * - first action - non-dealer decides if they want the face up card. If they pass, the dealer has the option to take
 *  it. If the dealer passes as well, player 2 will draw from the deck.
 */

public class Rummy {
    private Deck deck;
    private final List<Card> discardPile = new ArrayList<>();
    Hand player1, player2;
    public static final int UNDERCUT = 10;
    public static final int GIN = 20;

    public Rummy(){
        player1 = new Hand(new Player("P1"));
        player2 = new Hand(new Player("P2"));

        System.out.println("\n\n\n--- Welcome to Gin Rummy! ---");
        CLI.pause();
    }

    public void play(){
        do{
            while(round());

            System.out.printf("Player %d wins!\n", player1.getScore() >= 100 ? 1 : 2);

            System.out.println("Would you like to play again? (y/n)");
        }while(Input.getBoolean("y", "n"));
    }

    private void setup(){
        player1.clear();
        player1.clearMelds();
        player2.clear();
        player2.clearMelds();

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

        while(turn(player1) && turn(player2));

        if(deck.size() == 2)
            System.out.println("\nThe deck only has 2 cards left. Round void.");

        return player1.getScore() < 100 && player2.getScore() < 100;
    }

    private Card getFaceUpCard(){
        return discardPile.get(discardPile.size() - 1);
    }

    private boolean turn(Hand activePlayer){
        CLI.cls();
        System.out.printf("\n- Player %d's turn -\n", activePlayer == player1 ? 1 : 2);
        CLI.pause();

        byte action;
        boolean validAction;
        do{
            action = activePlayer.getAction(activePlayer, getFaceUpCard());

            validAction = action >= 0 && action <= 3;

            if(!validAction)
                System.out.printf("Invalid action \"%d\"", action);
        }while(!validAction);

        boolean knocked = switch(action){
            case 0, Actor.DRAW_STOCK -> {
                System.out.println("Drawing from stock...");

                if(deck instanceof CheaterStandardDeck && action == 0)
                    activePlayer.addCard(((CheaterStandardDeck) deck).cheatDraw());
                else
                    activePlayer.addCard(deck.draw());

                yield false;
            }
            case Actor.DRAW_DISCARD -> {
                System.out.println("Drawing from discard pile...");
                activePlayer.addCard(getFaceUpCard());
                discardPile.remove(discardPile.size() - 1);

                yield false;
            }
            case Actor.KNOCK -> {
                System.out.println("Knock!\nChoose which melds to use:");
                knock(activePlayer);

                yield true;
            }
            default -> throw new IllegalStateException("Invalid action value: " + action);
        };

        if(!knocked){
            System.out.println("\nDiscarding...");

            Card toBeDiscard = activePlayer.removeCard(activePlayer.pickCard());
            discardPile.add(toBeDiscard);
            System.out.printf("Discarded the %s.\n", toBeDiscard);

            CLI.pause();
        }

        if(deck.size() == 2)
            return false;

        return !knocked;
    }

    private void knock(Hand knocker){
        if(knocker == player1){
            System.out.println("\n\nPlayer 1");
            player1.selectMelds();
            System.out.println("\nPlayer 2");
            player2.selectMelds();

            player2.layoff(player1.getMelds());
        }
        else{
            System.out.println("\n\nPlayer 2");
            player2.selectMelds();
            System.out.println("\nPlayer 1");
            player1.selectMelds();

            player1.layoff(player2.getMelds());
        }

        System.out.println("\n--- Round Results ---\n");

        System.out.printf("Player 1%s deadwood: %d\n", knocker == player1 ? " (knocked)" : "", player1.getDeadwood());
        StandardDeck.showHand(player1.getCards());
        System.out.printf("Current score: %d\n", player1.getScore());

        System.out.printf("\nPlayer 2%s deadwood: %d\n", knocker == player2 ? " (knocked)" : "", player2.getDeadwood());
        StandardDeck.showHand(player2.getCards());
        System.out.printf("Current score: %d\n", player2.getScore());

        Hand winningHand = player1.getDeadwood() < player2.getDeadwood() ? player1 : player2;
        System.out.printf("\nPlayer %d wins the round!\n", winningHand == player1 ? 1 : 2);
        if(winningHand == player1)
            player1.addScore(player2.getDeadwood() - player1.getDeadwood());
        else
            player2.addScore(player1.getDeadwood() - player2.getDeadwood());

        if(winningHand != knocker){
            System.out.printf("+%d points for undercut\n", UNDERCUT);
            winningHand.addScore(UNDERCUT);
        }
        if(winningHand.size() == 0){
            System.out.printf("+%d points for gin\n", GIN);
            winningHand.addScore(GIN);
        }

        System.out.println("\nNew scores:");
        System.out.printf("Player 1 score: %d\n", player1.getScore());
        System.out.printf("Player 2 score: %d\n", player2.getScore());
        CLI.pause();
    }
}
