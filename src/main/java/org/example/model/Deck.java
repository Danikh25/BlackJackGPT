package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cardDeck = new ArrayList<Card>();
    //Deck constructor
    //Iterate through the 4 suits and 13 ranks to get a 52 card deck
    public Deck(){
        for(Suit suit : Suit.values()){
                for(Rank rank : Rank.values()){
                    Card card = new Card(suit, rank);
                    cardDeck.add(card);
                }
        }
    }

    public List<Card> getCardDeck(){
        return cardDeck;
    }
}
