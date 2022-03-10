package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class implemented a dialogue system
 */
public class DialogueSystem {

    public static ArrayList<Agent> agents = new ArrayList<>();
    // the dialogue maintains a logger recording every move of agents
    public static Messager messager = new Messager();
    public static int counter = 0;
    public static String topic = null;

    /**
     * default constructor
     */
    public DialogueSystem(){

    }

    /**
     * add new agent to the dialogue
     * @param audiences audiences of the new agent
     * @param arguments arguments of the new agent
     */
    public void addAgent(HashMap<String, Integer> audiences, ArrayList<Argument> arguments){
        agents.add(new Agent(audiences, arguments));
    }

    /**
     * start the dialogue under a topic
     * @param topic topic
     * @return 1 if is an agreed close, -1 if it's a match close
     */
    public Integer run(String topic){
        this.topic = topic;
        // the dialogue opened by the last agent in order
        Agent lastAgent = agents.get(agents.size() - 1);
        messager.broadCast(new Move(lastAgent,MoveType.OPEN,topic), lastAgent, agents);

        // initialize the model and dvaf of each agent
        for (Agent age: agents){
            age.initializeByTopic(topic, getDialogueInfo());
        }
        Integer close = 0;
        while(close == 0){
            for(Agent currentAgent: agents){
                currentAgent.updateModel(getDialoguePossibility());
                Move currentMove = currentAgent.Act(messager);
                messager.broadCast(currentMove, currentAgent, agents);
                close = messager.checkClose();
                if(close != 0){
                    break;
                }
                counter++;
            }
        }
        messager.printLog();
        return close;
    }

    /**
     * return the information of the dialogue, agents and their audiences
     * @return info
     */
    public Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> getDialogueInfo(){
        HashMap<Agent, HashMap<String, Integer>> infoTable = new HashMap<>();
        for (Agent age: agents){
            infoTable.put(age,age.collectAgentInfo());
        }
        return new Pair<>(agents, infoTable);
    }

    /**
     * return all agents' possibility distribution generated by their model
     * @return possibility distribution
     */
    public HashMap<Agent, HashMap<String, Float>> getDialoguePossibility(){
        HashMap<Agent,HashMap<String, Float>> list = new HashMap<>();
        for (Agent age: agents){
            list.put(age, age.getPossibility());
        }
        return list;
    }
}
