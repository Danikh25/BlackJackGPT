package org.example.model;

public enum Rank {

    ACE(11),
    KING(10),
    QUEEN(10),
    JACK(10),
    TEN(10),
    NINE(9),
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2);

    private final int value;

    private Rank(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
