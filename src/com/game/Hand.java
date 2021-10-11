package com.game;

import com.card.Card;
import com.deck.StandardDeck;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards = new ArrayList<>();
    private List<List<Card>> melds = new ArrayList<>();
    private final Actor holder;

    public Hand(Actor actor){
        holder = actor;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public Card removeCard(int index){
        return cards.remove(index);
    }

    public void clear(){
        cards.clear();
    }

    public void sortBySuit(){
        cards.sort(new StandardDeck.SortBySuit());
    }

    public void sortByValue(){
        cards.sort(new StandardDeck.SortByValue());
    }

    public List<Card> getCards(){
        return List.copyOf(cards);
    }

    public String getName(){
        return holder.getName();
    }

    public int getScore(){
        return holder.getScore();
    }

    public void addScore(int points) {
        holder.addScore(points);
    }

    public byte getAction(Hand hand, Card faceUpCard){
        return holder.getAction(hand, faceUpCard);
    }

    private List<List<Card>> findMelds(){
        List<List<Card>> tempMelds = new ArrayList<>();

        for(Card card : cards){
            // TODO: find all possible sets and runs
        }

        return tempMelds;
    }

    public void selectMelds(){
        List<List<Card>> tempMelds = findMelds();
        System.out.println("\nSelect a meld to save:");


    }

    public int getDeadwood(){
        return cards.stream()
                .mapToInt(card -> switch(card.value){
                    case 11, 12, 13 -> 10;
                    default -> card.value;
                })
                .sum()
                ;
    }
}