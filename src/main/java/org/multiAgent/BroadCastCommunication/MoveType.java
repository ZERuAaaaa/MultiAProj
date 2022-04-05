package org.multiAgent.BroadCastCommunication;

/**
 * This enum class restricted all moves appear in the dialogue
 */
public enum MoveType {

    OPEN("OPEN"),ASSERT("ASSERT"),AGREE("AGREE"),CLOSE("CLOSE");
    String move;

    MoveType(String name){
        this.move = name;
    }

    /**
     *
     * @return move
     */
    public String getMove(){
        return move;
    }

    public Boolean isOPEN(){
        return this == MoveType.OPEN;
    }

    public Boolean isCLOSE(){
        return this == MoveType.CLOSE;
    }

    public Boolean isASSERT(){
        return this == MoveType.ASSERT;
    }

    public Boolean isAGREE(){
        return this == MoveType.AGREE;
    }
}
