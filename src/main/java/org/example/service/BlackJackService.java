package org.example.service;

import org.example.model.Hand;

public interface BlackJackService {

    void startGame();

    void playerHit();

    void playerStand();

    boolean isBust(Hand hand);

    String checkResult();

    Hand getHand(String type);

    boolean checkIfBj(Hand hand);
}
