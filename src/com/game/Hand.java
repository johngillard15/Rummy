package com.game;

import com.card.Card;
import com.deck.StandardDeck;

import java.util.ArrayList;
import java.util.Arrays;
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

        sortByValue();
        for(int i = 0; i < cards.size() - 2; i++){
            // minimum of 3 for set
            if(cards.get(i).value == cards.get(i + 1).value && cards.get(i).value == cards.get(i + 2).value){
                List<Card> tempList = new ArrayList<>(Arrays.asList(cards.get(i), cards.get(i + 1), cards.get(i + 2)));

                // maximum of 4 for set
                if(i < cards.size() - 3 && cards.get(i).value == cards.get(i + 3).value){
                    tempList.add(cards.get(i + 3));
                    i += 3;
                }
                else
                    i += 2;

                tempMelds.add(tempList);
            }

        }

        for(int i = 0; i < cards.size() - 2; i++){
            // TODO: add run detection
        }

        return tempMelds;
    }

    public void selectMelds(){
        List<List<Card>> tempMelds = findMelds();

//        System.out.println("\nSelect a meld to use:");
        System.out.println("\n Possible melds:");
        for(List<Card> meld : tempMelds){
            System.out.println(meld);
        }

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