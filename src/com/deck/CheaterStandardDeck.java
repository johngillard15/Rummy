package com.deck;

import com.card.Card;
import com.utilities.CLI;
import com.utilities.Input;
import com.utilities.UI;

import java.util.stream.Collectors;

public class CheaterStandardDeck extends StandardDeck {

    public CheaterStandardDeck(){
        super();
    }

    public CheaterStandardDeck(int decks) {
        super(decks);
    }

    @Override
    public Card draw(){
        return super.draw();
    }

    public Card cheatDraw(){
        Card cheatCard;

        do{
            System.out.println("Ace -> 1\n2 - 10\nJack -> 11\nQueen -> 12\nKing -> 13");
            System.out.println("Value:");
            int valueIndex = Input.getInt(1, VALUES.length) - 1;

            System.out.println("\nof...\n");

            UI.listerator(SUITS); // TODO: fix spacing in output
            System.out.print("Suit:\n"); // 0 for random suit
            int suitIndex = Input.getInt(0, SUITS.length) - 1;

            boolean inDeck = false;
            if(suitIndex == -1){
                while(!inDeck && ++suitIndex < SUITS.length)
                    inDeck = cardInDeck(suitIndex, valueIndex);
            }
            else
                inDeck = cardInDeck(suitIndex, valueIndex);

            if(inDeck){
                cheatCard = new Card(SUITS[suitIndex], VALUES[valueIndex]);

                System.out.printf("The dealer slides you a%s %s...\n",
                        valueIndex == 0 || valueIndex == 7 ? "n" : "", cheatCard);

                break;
            }
            else
                System.out.println("We don't have that card, pick another one...");

            CLI.pause();
        }while(true);

        return cheatCard;
    }

    private boolean cardInDeck(int suitIndex, int valueIndex){
        // ∨∨∨ for one standard deck ∨∨∨
        if(decks == 1){
            return pile.removeIf(thisCard -> thisCard.suit.equals(SUITS[suitIndex])
                    && thisCard.rank == VALUES[valueIndex]);
        }
        // ∨∨∨ for multiple decks ∨∨∨
        else{
            try{
                Card cheatCard = pile.stream()
                        .filter(card -> card.suit.equals(SUITS[suitIndex]) && card.rank == VALUES[valueIndex])
                        .collect(Collectors.toList())
                        .get(0);
                pile.remove(cheatCard);

                return true;
            }
            catch(IndexOutOfBoundsException e){
                return false;
            }
        }
    }
}
