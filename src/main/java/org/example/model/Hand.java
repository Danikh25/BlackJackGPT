package org.example.model;

import java.util.List;

public class Hand {

    private int score;
    private List<Card> currentHand;

    //Soft hand is when an ace can be considered as 11 without going over 21
    private boolean isSoftHand;

    public Hand(int score, List<Card> currentHand, boolean isSoftHand) {
        this.score = score;
        this.currentHand = currentHand;
        this.isSoftHand = isSoftHand;
    }

    public Hand() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Card> getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(List<Card> currentHand) {
        this.currentHand = currentHand;
    }

    public boolean isSoftHand() {
        return isSoftHand;
    }

    public void setSoftHand(boolean softHand) {
        isSoftHand = softHand;
    }
}
