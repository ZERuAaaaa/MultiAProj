package org.multiAgent.IVAFramework;

/**
 * this class is to represent signs using within the system
 */
public enum Sign {
    POSITIVE,NEGATIVE;

    public boolean isPositive() {
        return this == Sign.POSITIVE;
    }

    public boolean isNegative() {
        return this == Sign.NEGATIVE;
    }


}

