package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Sign;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Messager {

    ArrayList<Move> messageLog = new ArrayList<>();

    public Messager(){

    }

    public <T> void broadCast(Move move, Agent agent, ArrayList<Agent> agents){
        if(move.getType() == MoveType.ASSERT){
            for(Agent age: agents){
                if(age != agent){
                    age.getDvaf().insertArgument((Argument) move.getContent());
                }
            }
        }
        addMessage(move);
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
            if(move.getType() == type && ((Argument) move.getContent()).getAct().equals(argument.getAct())){
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean checkMessageExistence(MoveType type, String action, Sign sign){
        boolean exist = false;

        for (Move move: messageLog){
            if(move.getType() == MoveType.ASSERT){
                Argument currentArgument = ((Argument) move.getContent());
                if(move.getType() == type && currentArgument.getAct().equals(action) && currentArgument.getSign() == sign){
                    exist = true;
                    break;
                }
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
                if(move.type == MoveType.AGREE && move.getContent().equals(agreement)){
                    agreeCloseAgents.add(move.getSender());
                }
            }
            if(agreeCloseAgents.size() == threshold){
                return 1;
            }
        }
        return 0;
    }

    public boolean checkAgreed(String action, Agent agent){
        boolean canAgree = false;
        Move lastOne = getLastOne();
        if(lastOne.getSender() != agent &&
           lastOne.getType() == MoveType.AGREE &&
           lastOne.getContent().equals(action)){
            canAgree = true;
        }else{
            for(int i = 0; i < messageLog.size(); i++){
                Move move0 = messageLog.get(i);
                if(move0.getSender() != agent &&
                        move0.getType() == MoveType.ASSERT &&
                        ((Argument) move0.getContent()).getAct().equals(action) &&
                        ((Argument) move0.getContent()).getSign() == Sign.POSITIVE){
                    canAgree = true;
                    for(int j = i + 1; j < messageLog.size(); j++){
                        Move move1 = messageLog.get(j);
                        if(move1.getSender() == agent &&
                                move1.getType() == MoveType.AGREE &&
                                move1.getContent().equals(action)){
                            canAgree = false;
                            for(int k = j + 1; k < messageLog.size(); k++){
                                Move move2 = messageLog.get(k);
                                if(move2.getSender() == agent && move2.getType() == MoveType.ASSERT){
                                    canAgree = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return canAgree;
    }



}
