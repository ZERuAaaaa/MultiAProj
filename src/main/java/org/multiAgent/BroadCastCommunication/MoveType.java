package org.multiAgent.BroadCastCommunication;

public enum MoveType {

    OPEN("OPEN"),ASSERT("ASSERT"),AGREE("AGREE"),CLOSE("CLOSE");
    String move;
    MoveType(String name){
        this.move = name;
    }

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
