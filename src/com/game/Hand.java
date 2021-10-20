package com.game;

import com.card.Card;
import com.deck.StandardDeck;
import com.utilities.CLI;
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

    public int size(){
        return cards.size();
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

    public List<List<Card>> getMelds(){
        return new ArrayList<>(melds);
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

    public int pickCard(){
        return holder.pickCard(List.copyOf(cards));
    }

    public void clearMelds(){
        melds.clear();
    }

    public List<List<Card>> findMelds(){
        List<List<Card>> tempMelds = new ArrayList<>();
        List<Card> tempList;

        // Sets
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

    private boolean isMeld(List<Card> meld, Card card){
        return isSet(meld, card) || isRun(meld, card);
    }

    private boolean isSet(List<Card> set, Card card){
        return card.rank == set.get(0).rank;
    }

    private boolean isRun(List<Card> run, Card card){
        return run.stream().allMatch(runCard -> Objects.equals(runCard.suit, card.suit))
                && Objects.equals(card.suit, run.get(0).suit)
                && card.rank + 1 == run.get(0).rank || card.rank - 1 == run.get(run.size() - 1).rank;
    }

    public void layoff(List<List<Card>> opponentMelds){
        System.out.println("Layoff");

        do{
            int meldIndex = holder.pickMeld(opponentMelds); // TODO: make sure this does not run if list is empty
            if(meldIndex == -1)
                break;

            int cardIndex = pickCard();

            if(isMeld(opponentMelds.get(meldIndex), cards.get(cardIndex))){
                System.out.printf("Added %s to opponent meld.\n", cards.get(cardIndex));
                opponentMelds.get(meldIndex).add(cards.remove(cardIndex));
            }
            else{ // TODO: improve this, maybe automate
                System.out.println("That card cannot be applied to this meld.\n" +
                        "Pick another card/meld or enter '0' to end layoff");
            }

            CLI.pause();
        }while(true);
    }

    public void selectMelds(){
        List<List<Card>> tempMelds = findMelds();

        while(!tempMelds.isEmpty()){
            List<Card> meld = tempMelds.get(holder.pickMeld(tempMelds));

            melds.add(meld);
            for(Card card : meld)
                cards.remove(card);

            tempMelds = findMelds();
        }
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