package org.multiAgent.IVAFramework;

public enum Sign {
    POSITIVE,NEGATIVE;

    public boolean differentFrom(Sign sign) {
        return this.getClass() != sign.getClass();
    }

    public boolean isPositive() {
        return this == Sign.POSITIVE;
    }

    public boolean isNegative() {
        return this == Sign.NEGATIVE;
    }


}

