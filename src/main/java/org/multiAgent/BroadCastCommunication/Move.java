package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;

public class Move<T> {


    Agent sender;
    MoveType type;
    T content;
    Integer timeStamp;

    public Move(Agent sender, MoveType type, T content){
        this.sender = sender;
        this.type = type ;
        this.content = content;
        timeStamp = DialogueSystem.counter;
    }

    public Agent getSender(){
        return sender;
    }

    public MoveType getType(){
        return type;
    }

    public T getContent(){
        return content;
    }

    public String toString(){
        return "<" + this.sender.getAgentId() +
                "," + this.type.getMove() +
                "," + this.content.toString() + ">";
    }
}
