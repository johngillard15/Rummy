package com.game;

import com.card.Card;
import com.deck.StandardDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<Card> tempList;

        // Sets
        sortByValue();
        for(int i = 0; i < cards.size() - 2; i++){
            tempList = new ArrayList<>(List.of(cards.get(i)));

            if(cards.get(i).rank == cards.get(i + 1).rank && cards.get(i).rank == cards.get(i + 2).rank){
                tempList.add(cards.get(i + 1));
                tempList.add(cards.get(i + 2));

                // maximum of 4 for set (because a single deck only has 4 of each card)
                if(i + 3 < cards.size() && cards.get(i).rank == cards.get(i + 3).rank)
                    tempList.add(cards.get(i + 3));
            }

            // minimum of 3 for set
            if(tempList.size() >= 3)
                tempMelds.add(tempList);
            if(tempList.size() > 0)
                i += tempList.size() - 1;
        }

        // Runs
        sortBySuit();
        for(int i = 0; i < cards.size() - 2; i++){
            tempList = new ArrayList<>(List.of(cards.get(i)));

            // add any card that follows the sequence
            while(i + 1 < cards.size() && Objects.equals(cards.get(i).suit, cards.get(i + 1).suit)){
                if(cards.get(i).rank + 1 == cards.get(i + 1).rank)
                    tempList.add(cards.get(i + 1));
                else if(cards.get(i).rank != cards.get(i + 1).rank)
                    break;
                ++i;
            }

            // minimum of 3 for run
            if(tempList.size() >= 3)
                tempMelds.add(tempList);
        }

        return tempMelds;
    }

    public void selectMelds(){
        List<List<Card>> tempMelds = findMelds();

//        System.out.println("\nSelect a meld to use:");
        System.out.println("\nPossible melds:");
        int listNum = 0;
        for(List<Card> meld : tempMelds){
            String type = Objects.equals(meld.get(0).suit, meld.get(1).suit)
                    ? meld.get(0).suit + " Run"
                    : "Set of " + meld.get(0).rankName() + "s";
            System.out.printf("%d. %s %s\n", ++listNum, type, meld);
        }
    }

//    public void addToMeld(int index){ // TODO: remember to sort!
//        addToMeld(cards.get(index));
//    }
//    public void addToMeld(Card card){
//        for(List<Card> meld : melds){
//
//        }
//    }

    public int getDeadwood(){
        return cards.stream()
                .mapToInt(card -> switch(card.rank){
                    case 11, 12, 13 -> 10;
                    default -> card.rank;
                })
                .sum()
                ;
    }
}