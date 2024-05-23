package org.example.service;

import org.example.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
@Service
public class BlackJackServiceImpl implements BlackJackService{

    //These are the initial hands of the player and dealer
    private Hand playerHand;
    private Hand dealerHand;
    private static final int noOfDecks = 4;
    private Stack<Card> finalPlayingDeck;

    //Since these methods are going be part of the transactions
    //it is better to keep them in the service layer
    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(Hand dealerHand) {
        this.dealerHand = dealerHand;
    }

    public Stack<Card> getFinalPlayingDeck() {
        return finalPlayingDeck;
    }

    public void setFinalPlayingDeck(Stack<Card> finalPlayingDeck) {
        this.finalPlayingDeck = finalPlayingDeck;
    }


    @Override
    public void startGame() {
        List<Card> cardsToShuffle = new ArrayList<>();

        for( int deckNo = 0 ; deckNo < noOfDecks ; deckNo++){
            Deck deck = new Deck();
            //Get all the card from the 4 decks and add them to shuffle them
            cardsToShuffle.addAll(deck.getCardDeck());
        }

        finalPlayingDeck = new Stack<Card>();

        shuffle(cardsToShuffle, finalPlayingDeck);

        dealInitialCards();
    }

    @Override
    public void playerHit() {
        populateHand(this.playerHand);
    }

    @Override
    public void playerStand() {
        //Wait until the player stands to add the value of the other dealer card
        Card faceDownCard = this.dealerHand.getCurrentHand().get(1);

        if(checkIfAce(faceDownCard)){
            if(this.dealerHand.isSoftHand()){
                this.dealerHand.setScore(this.dealerHand.getScore() - 10); //set ace as 1
            }
            this.dealerHand.setSoftHand(true);
        }
        this.dealerHand.setScore(this.dealerHand.getScore() + faceDownCard.getRank().getValue());
        //Dealer must stand on 17 and will continue to draw if not
        while (!checkSoftSeventeen()){
            populateHand(this.dealerHand);
        }
    }

    @Override
    public boolean isBust(Hand hand) {

        return hand.getScore() > 21;
    }

    @Override
    public String checkResult() {

        String message = null;

        if(isBust(this.dealerHand)) {
            message = "=== Dealer bust. Player wins !!! ===";
        } else if(checkIfBj(this.dealerHand)) {
            message = "=== Bj !!! Dealer wins ===";
        } else if (checkIfBj(this.playerHand)) {
            message = "=== BJ !!! Player wins ===";
        } else if (this.dealerHand.getScore() > this.playerHand.getScore()) {
            message = "=== Dealer wins :( ===";
        } else if (this.dealerHand.getScore() < this.playerHand.getScore()) {
            message = "=== Player wins !!! ===";
        } else {
            message = "=== Push ===";
        }

        return message;
    }

    //Return subclass object based on type
    @Override
    public Hand getHand(String type) {

        if(type.equals("player"))
            return this.playerHand;
        else if (type.equals("dealer"))
            return this.dealerHand;
        return null; //Add exception
    }

    @Override
    public boolean checkIfBj(Hand hand) {
        return hand.getScore() == 21;
    }

    //Shuffling the decks by getting random cards and adding them to a final deck
    public void shuffle(List<Card> cardsToShuffle,Stack<Card> finalPlayingDeck) {

        int cardCount = cardsToShuffle.size();

        for(int i=0;i<cardCount;i++) {
            int randomCardNumber = (int)(Math.random()*cardsToShuffle.size());
            finalPlayingDeck.push(cardsToShuffle.get(randomCardNumber));
            cardsToShuffle.remove(randomCardNumber);
        }
    }
    private void dealInitialCards() {

        playerHand = new Player();
        dealerHand = new Dealer();

        populatePlayerHand();
        populateDealerHand();
    }
    //Initial card dealing logic
    private void populatePlayerHand(){

        List<Card> playerCards = new ArrayList<Card>();
        int playerScore = 0;

        Card firstCard = finalPlayingDeck.pop();
        playerCards.add(firstCard);

        if(checkIfAce(firstCard)){
            this.playerHand.setSoftHand(true); //Set ace as 11
        }
        playerScore += firstCard.getRank().getValue();

        Card secondCard = finalPlayingDeck.pop();
        playerCards.add(secondCard);

        if(checkIfAce(secondCard)){
            if(checkIfAce(firstCard)){
                playerScore -= 10; //if first and second cards are ace set ace as 1(reduce score by 10)
            }
            this.playerHand.setSoftHand(true); //otherwise set ace as 11
        }
        playerScore += secondCard.getRank().getValue();

        //Using this.playerhand so the state of the playerHand is always updated correctly
        //since we are using the instance variable and not the local variable
        this.playerHand.setCurrentHand(playerCards);
        this.playerHand.setScore(playerScore);
    }
    //Initial dealer card dealing logic
    private void populateDealerHand() {

        List<Card> dealerCards = new ArrayList<Card>();
        int dealerScore=0;

        Card firstCard = finalPlayingDeck.pop();
        dealerCards.add(firstCard);

        if(checkIfAce(firstCard)) {
            this.dealerHand.setSoftHand(true);
        }

        dealerScore+=firstCard.getRank().getValue();

        // Since the second hand is face down, the second card score is not added to the total
        Card secondCard = finalPlayingDeck.pop();
        dealerCards.add(secondCard);


        this.dealerHand.setCurrentHand(dealerCards);
        this.dealerHand.setScore(dealerScore);

    }
    //Will be used to determine if ACE is 1 or 11
    private boolean checkIfAce(Card card) {
        return card.getRank() == Rank.ACE;
    }
    //When player hits or dealer need to draw cards in the middle of the game
    private void populateHand(Hand hand){
        //Draw a card from the final deck
        Card card = finalPlayingDeck.pop();
        int cardValue = card.getRank().getValue();
        hand.getCurrentHand().add(card); //add the card to the current hand

        if(checkIfAce((card))) {
            //If hand already had an ace subtract 10 so it doesn't bust( add the value of Ace = 1)
            if (hand.isSoftHand()) {
                hand.setScore(hand.getScore() - 10);
            }
            //if not consider it as 11 and add the value
            hand.setScore(hand.getScore() + cardValue);
            hand.setSoftHand(true);

            if(isBust(hand)){
                hand.setScore(hand.getScore() - 10);//If over 21 set ace = 1
                hand.setSoftHand(false); //the hand is now a hard hand( Ace = 1 always)
            }
        } else { //If hand doesnt have an ace already
            //Now that the current card is not ace we update the total score
            hand.setScore(hand.getScore() + cardValue);
            //If the newly drawn card is ace and make the player bust consider it as 1
            if(hand.isSoftHand() && isBust(hand)){
                hand.setScore(hand.getScore() - 10);
                hand.setSoftHand(false); // Now ace = 1 always so hard hand
            }
        }
    }
    //Dealer has to stand on 17
    private boolean checkSoftSeventeen(){
        return this.dealerHand.getScore() >= 17;
    }
}
