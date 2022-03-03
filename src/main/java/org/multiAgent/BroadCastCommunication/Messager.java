package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;
import org.multiAgent.IVAFramework.Argument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Messager {

    ArrayList<Move> messageLog = new ArrayList<>();

    public Messager(){

    }

    public <T> void broadCast(Agent proposer, MoveType type, T content){

    }

    public void printLog(){
        for(int i = 0; i < messageLog.size(); i++){
            System.out.println(i + ":" + messageLog.get(i).toString());
        }
    }

    public void addMessage(Move move){
        messageLog.add(move);
    }

    public Move getLastOne(){
        return messageLog.get(messageLog.size() - 1);
    }

    public void printLastOne(){
        System.out.println(messageLog.get(messageLog.size() - 1));
    }

    public boolean checkMessageExistence(MoveType type, Argument argument){
        boolean exist = false;
        for (Move move: messageLog){
            if(move.getType() == type && move.getContent() == argument){
                exist = true;
                break;
            }
        }
        return exist;
    }

    public Integer checkClose(){
        int threshold = Agent.AgentCounter;
        if(messageLog.size() < threshold){
            return 0;
        }
        // check the last N messages when there are N agents
        List<Move> lastMessage = messageLog.subList(messageLog.size() - threshold, messageLog.size());
        if(lastMessage.get(0).getType() == MoveType.CLOSE){
            //check whether there are all agents close the dialogue continuously
            HashSet<Agent> matchCloseAgents = new HashSet<>();
            for (Move move: lastMessage){
                if(move.type == MoveType.CLOSE && move.getContent() == DialogueSystem.topic){
                    matchCloseAgents.add(move.getSender());
                }
            }
            if(matchCloseAgents.size() == threshold){
                return -1;
            }
        }else if(lastMessage.get(0).getType() == MoveType.AGREE){
            // check whether there are all agents agree with the same topic continuously
            HashSet<Agent> agreeCloseAgents = new HashSet<>();
            String agreement = (String) lastMessage.get(0).getContent();
            for (Move move: lastMessage){
                if(move.type == MoveType.AGREE && move.getContent() == agreement){
                    agreeCloseAgents.add(move.getSender());
                }
            }
            if(agreeCloseAgents.size() == threshold){
                return 1;
            }
        }
        return 0;
    }
}
