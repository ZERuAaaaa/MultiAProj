package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * this class implemented a dialogue system
 */
public class DialogueSystem {

    public static ArrayList<Agent> agents = new ArrayList<>();
    // the dialogue maintains a logger recording every move of agents
    public static Messager messager = new Messager();
    public static int counter = 0;
    public static String topic = null;

    public void reset(){
        topic = null;
        messager = new Messager();
        agents.clear();
        counter = 0;
        Agent.AgentCounter = 0;
    }
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
    public void addAgent(HashMap<String, Integer> audiences, ArrayList<Argument> arguments, Model model){
        agents.add(new Agent(audiences, arguments, model));
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
            //agreeable.add(age.getAgreeableAction());
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

    public int[] evaluationRun(String topic){
        DialogueSystem.topic = topic;
        // the dialogue opened by the last agent in order
        Agent lastAgent = agents.get(agents.size() - 1);
        messager.broadCast(new Move(lastAgent,MoveType.OPEN,topic), lastAgent, agents);

        boolean hasConsensus = false;
        // initialize the model and dvaf of each agent
        ArrayList<HashSet<String>> agreeable = new ArrayList<>();
        for (Agent age: agents){
            age.initializeByTopic(topic, getDialogueInfo());
            agreeable.add(age.getAgreeableAction());
        }
        HashSet<String> consensus = formConsensus(agreeable);
        if (!consensus.isEmpty()){
            hasConsensus = true;
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
        String output = ((String) messager.getLastOne().getContent());
        boolean hasSucessOutput = close != -1;
        HashSet<String> temp = new HashSet<>();
        temp.add(output);
        // dialogueScore
        int dialogueScore;
        if (!hasSucessOutput){
            dialogueScore = 0;
        }else{
             dialogueScore = calculateScore(agreeable, temp);
        }
        // consensusScore
        int consensusScore = calculateScore(agreeable, consensus);
        // length of dialogue
        int dialogueLength = counter;

        int[] data = new int[5];
        data[0] = dialogueLength;
        data[1] = hasSucessOutput ? 1 : 0;
        data[2] = hasConsensus ? 1 : 0;
        data[3] = dialogueScore;
        data[4] = consensusScore;
        return data;
    }

    public HashSet<String> formConsensus(ArrayList<HashSet<String>> agreeable){
        HashSet<String> consensus = agreeable.get(0);
        for (int i = 1 ; i < agreeable.size(); i++){
            consensus.retainAll(agreeable.get(i));
        }
        return consensus;
    }

    public Integer calculateScore(ArrayList<HashSet<String>> agreeable, HashSet<String> dialogueOutput){
        int max = Integer.MIN_VALUE;
        if (dialogueOutput.isEmpty()){
            return 0;
        }
        for (String output : dialogueOutput){
            for (HashSet<String> agr: agreeable){
                boolean allAgree = true;
                boolean someAgree = false;
                if(!agr.contains(output)){
                    allAgree = false;
                }
                if (agr.contains(output)){
                    someAgree = true;
                }
                if (allAgree){
                    max = Math.max(max, 3);
                }else if (someAgree){
                    max = Math.max(max, 2);
                }else{
                    max = Math.max(max , 1);
                }
            }
        }
        return max;
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
