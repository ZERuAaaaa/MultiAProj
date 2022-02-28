package org.multiAgent.BroadCastCommunication;

public enum Move {

    OPEN("OPEN"),ASSERT("ASSERT"),AGREE("AGREE"),CLOSE("CLOSE");
    String move;
    Move(String name){
        this.move = name;
    }

    public String getMove(){
        return move;
    }

    public Boolean isOPEN(){
        return this == Move.OPEN;
    }

    public Boolean isCLOSE(){
        return this == Move.CLOSE;
    }

    public Boolean isASSERT(){
        return this == Move.ASSERT;
    }

    public Boolean isAGREE(){
        return this == Move.AGREE;
    }
}
