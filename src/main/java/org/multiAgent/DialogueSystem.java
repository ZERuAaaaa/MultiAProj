package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;
import org.multiAgent.IVAFramework.Sign;
import org.multiAgent.Models.Model;

import java.util.*;
import java.util.stream.Collectors;

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
        //messager.printLog();
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
        ArrayList<Argument> globalArgument = new ArrayList<>();
        for (Agent age: agents){
            age.initializeByTopic(topic, getDialogueInfo());
            agreeable.add(age.getAgreeableAction());
            globalArgument.addAll(age.getArguments());
        }

        HashSet<String> consensus = formConsensus(agreeable);
        if (!consensus.isEmpty()){
            hasConsensus = true;
        }

        IVAF OAF1 = new IVAF(globalArgument,agents.get(0).getAudiences());
        IVAF OAF2 = new IVAF(globalArgument,agents.get(1).getAudiences());

        ArrayList<Argument> globallyAgreeable1 = OAF1.getPreferredExtension().stream()
                .filter(argument -> argument.getSign() == Sign.POSITIVE)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Argument> globallyAgreeable2 = OAF2.getPreferredExtension().stream()
                .filter(argument -> argument.getSign() == Sign.POSITIVE)
                .collect(Collectors.toCollection(ArrayList::new));

        HashSet<String> agreeableAction1 = new HashSet<>();
        for(Argument arg: globallyAgreeable1){
            agreeableAction1.add(arg.getAct());
        }

        HashSet<String> agreeableAction2 = new HashSet<>();
        for(Argument arg: globallyAgreeable2){
            agreeableAction2.add(arg.getAct());
        }

        ArrayList<HashSet<String>> oafAgreeable = new ArrayList<>();
        oafAgreeable.add(agreeableAction1);
        oafAgreeable.add(agreeableAction2);

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
             dialogueScore = calculateScore(oafAgreeable, temp);
        }
        // consensusScore
        int consensusScore = calculateScore(oafAgreeable, consensus);
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

    public float[] evaluationRun2(String topic){
        DialogueSystem.topic = topic;
        // the dialogue opened by the last agent in order
        Agent lastAgent = agents.get(agents.size() - 1);
        messager.broadCast(new Move(lastAgent,MoveType.OPEN,topic), lastAgent, agents);

       // initialize the model and dvaf of each agent
        ArrayList<HashSet<String>> agreeable = new ArrayList<>();
        ArrayList<ArrayList<Argument>> agreeableArguments = new ArrayList<>();
        ArrayList<HashMap<String, Integer>> audiences = new ArrayList<>();
        for (Agent age: agents){
            age.initializeByTopic(topic, getDialogueInfo());
            agreeable.add(age.getAgreeableAction());

            agreeableArguments.add(age.getAgreeable());
            audiences.add(age.getAudiences());
        }
        ArrayList<HashMap<String, Integer>> combine = new ArrayList<>();
        for (int i = 0; i < agreeable.size(); i++){
            ArrayList<Argument> agentArg = agreeableArguments.get(i);
            HashMap<String, Integer> agentAud = audiences.get(i);
            ArrayList<Map.Entry<String,Integer>> list = new ArrayList(agentAud.entrySet());
            Collections.sort(list, new Comparator<>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return -o1.getValue().compareTo(o2.getValue());
                }
            });
            int rank = list.size();
            HashMap<String, Integer> temp = new HashMap<>();
            for (Map.Entry<String, Integer> entry: list){
                for (Argument arg: agentArg){
                    if (arg.getAudience().equals(entry.getKey())){
                        if (temp.containsKey(arg.getAct())){
                            temp.put(arg.getAct(), Math.max(temp.get(arg.getAct()),  rank));
                        }else{
                            temp.put(arg.getAct(), rank);
                        }
                    }
                }
                rank--;
            }
            combine.add(temp);
        }
        int close = 0;
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
        String output = messager.getLastOne().getContent().toString();
        float[] data = new float[5];
        if (close == 1){
            boolean allAgree = true;
            boolean someAgree = false;

            for (HashSet<String> out: agreeable){
                if (!out.contains(output)){
                    allAgree = false;
                }
                if (out.contains(output)){
                    someAgree = true;
                }
            }
            if (allAgree){
                // number of success
                data[0] = 1;
                // number of covience
                data[1] = 0;
                // calculate rank
                float sum = 0;
                for (HashMap<String, Integer> map: combine){
                    sum += map.get(output);
                }
                data[2] = sum / (float) combine.size();

                data[3] = counter;
                // have rank
                data[4] = 1;
            }else if (someAgree){
                // number of success
                data[0] = 1;
                // number of covience
                data[1] = 1;
                // calculate rank
                data[2] = 0;
                //
                data[3] = counter;

                data[4] = 0;
            }
        }else{
            // number of success
            data[0] = 0;
            // number of covience
            data[1] = 0;
            // calculate rank
            data[2] = 0;
            //
            data[3] = counter;

            data[4] = 0;
        }

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
        int min = Integer.MAX_VALUE;
        if (dialogueOutput.isEmpty()){
            return 0;
        }
        HashSet<String> agreeable1 = agreeable.get(0);
        HashSet<String> agreeable2 = agreeable.get(1);

        for (String str: dialogueOutput){
            if (agreeable1.contains(str) && agreeable2.contains(str)){
                min = Math.min(min,3);
            }
            if ((agreeable1.contains(str) && !agreeable2.contains(str)) || (!agreeable1.contains(str) && agreeable2.contains(str))){
                min = Math.min(min,2);
            }
            if (!agreeable1.contains(str) && !agreeable2.contains(str)){
                min = Math.min(min,1);
            }
        }
        return min;
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
