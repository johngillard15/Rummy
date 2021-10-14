package com.game;

import com.card.Card;
import com.deck.StandardDeck;
import com.utilities.Input;

import java.util.ArrayList;
import java.util.Comparator;
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
        sortBySuit(); // just to keep same values ordered by suit
        sortByValue();
        for(int i = 0; i < cards.size() - 2; i++){
            tempList = new ArrayList<>(List.of(cards.get(i)));

            while(i + 1 < cards.size() && cards.get(i).rank == cards.get(i + 1).rank){
                tempList.add(cards.get(i + 1));
                ++i;
            }

            if(tempList.size() >= 3)
                tempMelds.add(tempList);
        }

        // Runs
        sortBySuit();
        for(int i = 0; i < cards.size() - 2; i++){
            tempList = new ArrayList<>(List.of(cards.get(i)));

            while(i + 1 < cards.size() && Objects.equals(cards.get(i).suit, cards.get(i + 1).suit)){
                if(cards.get(i).rank + 1 == cards.get(i + 1).rank)
                    tempList.add(cards.get(i + 1));
                else if(cards.get(i).rank != cards.get(i + 1).rank)
                    break;
                ++i;
            }

            if(tempList.size() >= 3)
                tempMelds.add(tempList);
        }

        return tempMelds;
    }

    private boolean checkSet(){
        return true;
    }

    private boolean checkRun(){
        return true;
    }

    public void getPossibleMelds(){
        melds.clear();
        melds = findMelds();

        if(!melds.isEmpty()){
            melds.sort(Comparator.comparingInt(List::size));

            System.out.println("\nPossible melds:");
            int listNum = 0;
            for(List<Card> meld : melds){
                String type = Objects.equals(meld.get(0).suit, meld.get(1).suit)
                        ? meld.get(0).suit + " Run"
                        : "Set of " + meld.get(0).rankName() + "s";
                System.out.printf("%d. %s %s\n", ++listNum, type, meld);
            }
        }

        // TODO: if there are no possible melds left in cards list and the melds list is not empty,
        //  check if cards can be added to a meld
        // TODO: check if cards can be added to an existing meld
        // TODO: check if updated melds can be combined

    }

    private void addToMeld(int cardIndex, int meldIndex){ // TODO: remember to sort!
        addToMeld(cards.get(cardIndex), meldIndex);
    }
    private void addToMeld(Card card, int meldIndex){ // TODO: use checkSet/checkRun
        // TODO: update to check if adding is possible
        melds.get(meldIndex).add(card);
        cards.remove(card);
    }

    public void selectMelds(){
        List<List<Card>> tempMelds = new ArrayList<>();

        do{
            getPossibleMelds();

            if(!melds.isEmpty()){
                System.out.println("Pick a meld to use:");
                List<Card> meld = melds.get(Input.getInt(1, melds.size()) - 1);

                tempMelds.add(meld);
                for(Card card : meld)
                    cards.remove(card);
            }
        }while(!melds.isEmpty());

        melds = new ArrayList<>(tempMelds);
    }

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