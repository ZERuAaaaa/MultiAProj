package org.multiAgent;

import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements value-based transition system(VATS) for an single agent
 */


public class Agent {

    private IVAF ivaf;
    private HashMap<String, Float> audience;
    private ArrayList<Argument> arguments;
    private int AgentId;
    public static int AgentCounter = 0;
    /**
     * default constructor for an Agent
     */
    public Agent(HashMap<String, Float> audience){
        this.AgentId = AgentCounter++;
        this.audience = audience;
    }

    /**
     * when dialogue is open, generate set of all arguments construct from S under the Goal,
     * and generate IVAF of a single agent
     * @param Goal
     */
    public void initializeByGoal(String Goal){

    }

    public void setArguments(){

    }

    public void setAudience(){

    }

    public int getAgentId(){
        return AgentId;
    }
}
