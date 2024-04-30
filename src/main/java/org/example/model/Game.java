package org.example.model;

public class Game {
        private Hand dealerHand;
        private Hand playerHand;
        private boolean dealerInitialState;
        private String result;


    public Game(Hand dealerHand, Hand playerHand, boolean dealerInitialState, String result) {
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
        this.dealerInitialState = dealerInitialState;
        this.result = result;
    }

    public Game(Hand dealerHand, Hand playerHand, String result) {
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
        this.result = result;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(Hand dealerHand) {
        this.dealerHand = dealerHand;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public boolean isDealerInitialState() {
        return dealerInitialState;
    }

    public void setDealerInitialState(boolean dealerInitialState) {
        this.dealerInitialState = dealerInitialState;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

