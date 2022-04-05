package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;

/**
 * This class represent move agents are sending within the dialogue
 * @param <T> type of content of a message
 */
public class Move<T> {


    Agent sender;
    MoveType type;
    T content;
    Integer timeStamp;

    /**
     * construct a new move
     * @param sender agent who send the move
     * @param type type of the move
     * @param content the content of a move
     */
    public Move(Agent sender, MoveType type, T content){
        this.sender = sender;
        this.type = type ;
        this.content = content;
        timeStamp = DialogueSystem.counter;
    }

    /**
     * @return sender
     */
    public Agent getSender(){
        return sender;
    }

    /**
     * @return type
     */
    public MoveType getType(){
        return type;
    }

    /**
     * @return content
     */
    public T getContent(){
        return content;
    }

    /**
     * @return string format of a move
     */
    public String toString(){
        return "<" + this.sender.getAgentId() +
                "," + this.type.getMove() +
                "," + this.content.toString() + ">";
    }
}
