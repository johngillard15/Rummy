package com.deck;

import com.card.Card;
import com.utilities.ANSI;
import com.utilities.Input;
import com.utilities.UI;

import java.util.*;

public class StandardDeck implements Deck {
    public static final String[] SUITS = {
            "Diamond", "Club", "Heart", "Spade"
    };
    public static final int[] VALUES = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
    };
    public static final String cardBack = """
                ╭─────────╮
                │╠╬╬╬╬╬╬╬╣│
                │╠╬╬╬╬╬╬╬╣│
                │╠╬╬╬╬╬╬╬╣│
                │╠╬╬╬╬╬╬╬╣│
                │╠╬╬╬╬╬╬╬╣│
                ╰─────────╯""";
    protected List<Card> pile = new ArrayList<>();
    public final int decks;

    public StandardDeck(){
        System.out.println("How many decks will be used?");
        System.out.print("decks ");

        fillDeck(decks = Input.getInt(1, 8));
    }

    public StandardDeck(int decks){
        fillDeck(this.decks = decks);
    }

    protected void fillDeck(int decks){
        for(int i = 0; i < decks; i++){
            for (String suit : SUITS) {
                for (int value : VALUES)
                    pile.add(new Card(suit, value));
            }
        }
    }

    public int size(){
        return pile.size();
    }

    public void shuffle(){
        Collections.shuffle(pile);
    }

    public Card draw(){
        return pile.remove(pile.size() - 1);
    }

    public void discard(Card card){
        pile.add(card);
    }

    public static class SortBySuit implements Comparator<Card> {
        @Override
        public int compare(Card cardA, Card cardB){
            return Arrays.asList(SUITS).indexOf(cardA.suit) - Arrays.asList(SUITS).indexOf(cardB.suit);
        }
    }
    public static class SortByValue implements Comparator<Card> {
        @Override
        public int compare(Card cardA, Card cardB){
            return cardA.rank - cardB.rank;
        }
    }

    public static void showCard(Card card){
        System.out.println(getCardGUI(card));
    }

    public static void showHand(List<Card> cards){
        UI.showSideBySide(cards.stream()
                .map(StandardDeck::getCardGUI)
                .toArray(String[]::new)
        );
    }

    public static String getCardGUI(Card card){
        String formattedCard =
                """
                ╭─────────╮
                │%s       │
                │         │
                │    %s   │
                │         │
                │       %s│
                ╰─────────╯""";

        String suitFace = switch(card.suit){
            case "Diamond" -> ANSI.RED + "♦" + ANSI.RESET;
            case "Club" -> "♣";
            case "Heart" -> ANSI.RED + "♥" + ANSI.RESET;
            case "Spade" -> "♠";
            default -> throw new IllegalStateException("Unexpected suit: " + card.suit);
        } + "   ";

        String valueFace = switch(card.rank){
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(card.rank);
        };

        String top = valueFace, bot = valueFace;

        if(!(card.rank == 10)){
            top = valueFace + " ";
            bot = " " + valueFace;
        }

        return String.format(formattedCard, top, suitFace, bot);
    }

    @Override
    public String toString() {
        return String.format("Deck: %s", pile);
    }
}
