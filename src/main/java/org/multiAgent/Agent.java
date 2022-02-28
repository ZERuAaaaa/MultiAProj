package org.multiAgent;

import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Audience;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This class implements value-based transition system(VATS) for an single agent
 */


public class Agent {

    private IVAF dvaf;
    private ArrayList<Audience> audiences;
    private ArrayList<Argument> arguments;
    private final int AgentId;
    public static int AgentCounter = 0;
    /**
     * default constructor for an Agent
     */
    public Agent(){
        this.AgentId = AgentCounter++;
        audiences = new ArrayList<>();
        arguments = new ArrayList<>();
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

    public void addArgument(String action, String goal){
        arguments.add(new Argument(action, goal));
    }

    public void addAudience(String action, String audience, String sign, Integer val){
        audiences.add(new Audience(action, audience, sign, val));
    }

    public int getAgentId(){
        return AgentId;
    }

    public void initialize(){
        dvaf = new IVAF(arguments, audiences);
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
