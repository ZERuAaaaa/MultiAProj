package org.multiAgent;

import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This class implements value-based transition system(VATS) for an single agent
 */


public class Agent {

    private IVAF dvaf;
    private HashMap<String, Integer> audiences;
    private ArrayList<Argument> arguments;
    private final int AgentId;
    public static int AgentCounter = 0;
    /**
     * default constructor for an Agent
     */
    public Agent(){
        this.AgentId = AgentCounter++;
        audiences = new HashMap<>();
        arguments = new ArrayList<>();
    }

    public Agent(HashMap<String, Integer> audiences, ArrayList<Argument> arguments){
        this.AgentId = AgentCounter++;
        this.audiences = audiences;
        this.arguments = arguments;
    }
    /**
     * when dialogue is open, generate set of all arguments construct from S under the Goal,
     * and generate IVAF of a single agent
     * @param topic topic select by the dialogue
     */
    public void initializeByTopic(String topic){
        arguments = arguments.stream()
                        .filter(argument -> argument.getGoal().equals(topic))
                        .collect(Collectors
                                .toCollection(ArrayList::new));
        dvaf = new IVAF(arguments, audiences);
    }

    public <T> void newMessage(Move<T> move){

    }

    public void Act(){

    }

    public ArrayList<Move>[] protocol(){
        Messager messager = DialogueSystem.messager;
        ArrayList<Move>[] availableMoves = new ArrayList[3];
        availableMoves[2].add(new Move(this, MoveType.CLOSE, DialogueSystem.topic));
        for (Argument arg: arguments){
            // check whether the agent could assert the argument
            if (!messager.checkMessageExistence(MoveType.ASSERT, arg)){
                availableMoves[0].add(new Move(this,MoveType.ASSERT,arg));
            }
            // check whether the agent could agree with an action
            Move lastMessage = messager.getLastOne();
            if(lastMessage.getSender() != this && arg.getAct() == lastMessage.getContent()){
                availableMoves[1].add(new Move(this, MoveType.AGREE, lastMessage.getContent()));
            }else{

            }

        }
        return availableMoves;
    }

    public ArrayList<Argument> getAgreeable(){
        return dvaf.getPreferredExtension();
    }

    public void addArgument(String action, String goal, String sign, String audience){
        arguments.add(new Argument(action, goal, sign, audience));
    }

    public void addAudience(String audience, Integer val){
        audiences.put(audience, val);
    }

    public void setArguments(ArrayList<Argument> arguments){
        this.arguments = arguments;
    }

    public void setAudiences(HashMap<String,Integer> audiences){
        this.audiences = audiences;
    }

    public int getAgentId(){
        return AgentId;
    }

    public IVAF getDvaf(){
        return dvaf;
    }

    public void reset(){
        dvaf = null;
        audiences.clear();
        arguments.clear();
    }
}
