package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;

public class Message<T> {
    Agent sender;
    Move move;
    T content;
    Message(Agent sender, Move move, T content){
        this.sender = sender;
        this.move = move;
        this.content = content;
    }

    public Agent getSender(){
        return sender;
    }

    public Move getMove(){
        return move;
    }

    public T getContent(){
        return content;
    }

    public String toString(){
        return "<" + this.sender.getAgentId() +
                "," + this.move.getMove() +
                "," + this.content.toString() + ">";
    }
}
