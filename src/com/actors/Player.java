package com.actors;

import com.card.Card;
import com.deck.StandardDeck;
import com.game.Actor;
import com.game.Hand;
import com.utilities.CLI;
import com.utilities.Input;
import com.utilities.UI;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Player implements Actor {
    private final String name;
    private int score;

    public Player(String name){
        this.name = name;
        score = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getAction(Hand hand, Card faceUpCard){
        hand.sortByValue();
        hand.sortBySuit();

        List<List<Card>> possibleMelds = hand.findMelds();

        boolean drawing = false;
        do{
            System.out.println("Face up card:");
            UI.showSideBySide(StandardDeck.cardBack, StandardDeck.getCardGUI(faceUpCard));

            if(!possibleMelds.isEmpty()){
                possibleMelds.sort(Comparator.comparingInt(List::size));

                System.out.println("Current possible melds:");
                int listNum = 0;
                for(List<Card> meld : possibleMelds){
                    String type = Objects.equals(meld.get(0).suit, meld.get(1).suit)
                            ? meld.get(0).suit + " Run"
                            : "Set of " + meld.get(0).rankName() + "s";
                    System.out.printf("%d. %s %s\n", ++listNum, type, meld);
                }
            }

            System.out.println("Your cards:");
            StandardDeck.showHand(hand.getCards());

            System.out.println("Would you like to sort your cards or draw?");
            System.out.println("1. Sort by Value | 2. Sort by Suit | 3. Draw");
            int choice = Input.getInt(1, 3);
            switch(choice){
                case 1 -> {
                    hand.sortByValue();
                    CLI.cls();
                    System.out.println("Sorting by value...");
                }
                case 2 -> {
                    hand.sortBySuit();
                    CLI.cls();
                    System.out.println("Sorting by suit...");
                }
                case 3 -> drawing = true;
            }
        }while(!drawing);

        System.out.println("Draw from stock or discard pile, or knock");
        System.out.println("1. Draw from Stock | 2. Draw from Discard Pile | 3. Knock"); // 0 for cheatDraw
        System.out.print("Action: ");
        return (byte)Input.getInt(0, 3);
    }

    @Override
    public int pickCard(List<Card> cards) {
        System.out.println("Your Cards");
        StandardDeck.showHand(cards);

        StringBuilder cardNumbers = new StringBuilder();
        for (int i = 1; i <= cards.size(); i++)
            cardNumbers.append(String.format("     %s     ", i < 10 ? i + " " : i));
        System.out.println(cardNumbers);
        if(cards.size() == 11)
            System.out.println(" ".repeat(123) + "???Drawn");

        System.out.println("Choose a card:");
        return Input.getInt(1, cards.size()) - 1;
    }

    @Override
    public int pickMeld(List<List<Card>> melds){
        melds.sort(Comparator.comparingInt(List::size));

        System.out.println("Possible melds:");
        int listNum = 0;
        for(List<Card> meld : melds){
            String type = Objects.equals(meld.get(0).suit, meld.get(1).suit)
                    ? meld.get(0).suit + " Run"
                    : "Set of " + meld.get(0).rankName() + "s";
            System.out.printf("%d. %s %s\n", ++listNum, type, meld);
        }

        System.out.println("Pick a meld to use:");
        return Input.getInt(0, melds.size()) - 1;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addScore(int points) {
        score += points;
    }
}
