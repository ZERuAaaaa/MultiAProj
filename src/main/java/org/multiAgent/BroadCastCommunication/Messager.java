package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Sign;

import java.util.*;

/**
 * The messager is a dialogue tool, the messager could broadcast the move of agents and
 * keep recording moves of agents by maintaining a log. And provide method to perform
 * several form of check for agent.
 */
public class Messager {

    public ArrayList<Move> messageLog = new ArrayList<>();

    /**
     * default constructor
     */
    public Messager(){

    }

    /**
     * broadcast the move of an agent and record the move
     * @param move move
     * @param agent agent who required a broadcast
     * @param agents who broadcast move to
    */
    public void broadCast(Move move, Agent agent, ArrayList<Agent> agents){
        if(move.getType() == MoveType.ASSERT){
            for(Agent age: agents){
                if(age != agent){
                    age.getDvaf().insertArgument((Argument) move.getContent());
                }
            }
        }
        addMessage(move);
    }

    /**
     * print the dialogue log
     */
    public void printLog(){
        for(int i = 0; i < messageLog.size(); i++){
            System.out.println(i + ":" + messageLog.get(i).toString());
        }
    }

    /**
     * add new message to the log
     * @param move new move
     */
    public void addMessage(Move move){
        messageLog.add(move);
    }

    /**
     * get the move of current state of the dialogue
     * @return move
     */
    public Move getLastOne(){
        return messageLog.get(messageLog.size() - 1);
    }

    /**
     * print the new move
     */
    public void printLastOne(){
        System.out.println(messageLog.get(messageLog.size() - 1));
    }

    /**
     * check whether such a move exists in the log.
     * @param type type of move
     * @param argument specific argument
     * @return boolean
     */
    public boolean checkMessageExistence(MoveType type, Argument argument){

        boolean exist = false;
        for (Move move: messageLog){
            if(move.getType() == type && ((Argument) move.getContent()).equals(argument)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * check whether such a move exists in the log.
     * @param type type of move
     * @param action specific argument
     * @param sign the sign of the argument
     * @return boolean
     */
    public boolean checkMessageExistence(MoveType type, String action, Sign sign){
        boolean exist = false;

        for (Move move: messageLog){
            if(move.getType() == MoveType.ASSERT){
                Argument currentArgument = ((Argument) move.getContent());
                if(move.getType() == type && currentArgument.getAct().equals(action) &&
                        currentArgument.getSign() == sign){
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    /**
     * check whether the dialogue could close according to the log, and return
     * the type of close of the dialogue.
     * @return type of close
     */
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

    /**
     * check whether the agent could agree to the action
     * @param action action
     * @param agent agent
     * @return boolean
     */
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

    /**
     * check whether there are some arguments that demote a value, agents will demote the
     * value associate to the argument within their model.
     * @param agent agent
     * @param Magnification to what degree the agent would demote the value
     * @return set of demote information
     */
    public ArrayList<Object[]> checkDemote(Agent agent, float Magnification){
        boolean demote = false;
        ArrayList<Object[]> temp = new ArrayList<>();
        if (messageLog.size() < Agent.AgentCounter){
            return temp;
        }else{
            Integer position = messageLog.size()- Agent.AgentCounter;
            Move previous = messageLog.get(position);

            if (previous.getType() == MoveType.ASSERT){
                Argument pre = (Argument) previous.getContent();
                if (previous.getSender() == agent && pre.getSign() == Sign.POSITIVE){
                    for (int i = position + 1; i < messageLog.size(); i++){
                        Move currentMove = messageLog.get(i);
                        if (currentMove.getSender() != agent && currentMove.getType() == MoveType.ASSERT){
                            Argument currentArgument = (Argument) currentMove.getContent();
                            if (currentArgument.getSign() == Sign.NEGATIVE &&
                                    currentArgument.getAct().equals(pre.getAct())){
                                demote = true;
                                HashMap<String, Integer> audience = currentMove.getSender().getAudiences();
                                Object[] demotes = new Object[3];
                                demotes[0] = pre.getAudience();
                                demotes[1] = currentArgument.getAudience();
                                demotes[2] = Magnification * audience.get(currentArgument.getAudience());
                                temp.add(demotes);
                            }
                        }
                    }
                }
                if (!demote){
                    Object[] promotes = new Object[1];
                    promotes[0] = ((Argument) previous.getContent()).getAct();
                    temp.add(promotes);
                }
            }

        }
        return temp;
    }



}
