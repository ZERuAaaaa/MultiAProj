package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;
import org.multiAgent.IVAFramework.Sign;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Strategy.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * This class implements value-based transition system(VATS) for an single agent
 */
public class Agent {
    // dialogue iVAF for each agent
    private IVAF dvaf;

    private HashMap<String, Integer> audiences;
    private ArrayList<Argument> arguments;
    private final int AgentId;
    // counter for agents
    public static int AgentCounter = 0;
    // model of selecting argument with better winning value
    public Model model;
    // strategy of agent during the dialogue
    public Preference strategy = new Preference();

    /**
     * default constructor of agent
     * @param audiences audience of the agent
     * @param arguments arguments of the agent
     */
    public Agent(HashMap<String, Integer> audiences, ArrayList<Argument> arguments, Model model){
        this.model = model;
        this.AgentId = AgentCounter++;
        this.audiences = audiences;
        this.arguments = arguments;
    }
    /**
     * when dialogue is open, generate set of all arguments construct from S under the Goal,
     * and generate IVAF of a single agent
     * @param topic topic select by the dialogue
     * @param dialogueInfo dialogue information to initialize the model
     */
    public void initializeByTopic(String topic, Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo){
        model.initialize(dialogueInfo, this);
        arguments = arguments.stream()
                .filter(argument -> argument.getGoal().equals(topic))
                .collect(Collectors
                        .toCollection(ArrayList::new));
        dvaf = new IVAF(arguments, audiences);
    }

    public ArrayList<Argument> getArguments(){
        return arguments;
    }

    public HashMap<String, Integer> getAudience(){
        return audiences;
    }
    /**
     * the dialogue trigger the agent to act each round during the deliberation
     * @param messager a messager record all logs during the deliberation
     * @return the action an agent choosen
     */
    public Move Act(Messager messager){
        ArrayList<Object[]> demoteList = messager.checkDemote(this, 0.5F);
        if (demoteList.isEmpty()){

        }else if(demoteList.get(0).length == 1){
            model.promote((String) demoteList.get(0)[0]);
        }else{
            for (Object[] obj : demoteList) {
                model.demote((String) obj[0], (String) obj[1],(float) obj[2]);
            }
        }

        ArrayList<Argument> agreeable = getAgreeable();
        ArrayList<Argument> agreeableMoves = getAgreeable1(agreeable);
        HashSet<Move>[] availableMoves = protocol(messager);
        return strategy.pickStrategy(availableMoves, agreeable, agreeableMoves,model, messager);
    }

    /**
     * the agent will update their model every time when other agent make their move
     * @param possibility a new possibility distribution to update the model
     */
    public void updateModel(HashMap<Agent,HashMap<String, Float>> possibility){
        model.update(possibility);
    }

    /**
     * this function returns all moves(agree, assert or close) current available for an agent to take
     * @param messager dialogue log
     * @return set of all moves
     */
    public HashSet<Move>[] protocol(Messager messager){
        //Messager messager = DialogueSystem.messager;
        HashSet<Move>[] availableMoves = new HashSet[3];
        availableMoves[0] = new HashSet<>();
        availableMoves[1] = new HashSet<>();
        availableMoves[2] = new HashSet<>();
        availableMoves[2].add(new Move(this, MoveType.CLOSE, DialogueSystem.topic));
        HashSet<String> actions = new HashSet<>();
        for (Argument arg: arguments){
            actions.add(arg.getAct());
            // check whether the agent could assert the argument
            if (!messager.checkMessageExistence(MoveType.ASSERT, arg)){
                availableMoves[0].add(new Move(this,MoveType.ASSERT,arg));
            }
        }

        for(String act: actions){
            if(messager.checkAgreed(act, this)){
                availableMoves[1].add(new Move(this,MoveType.AGREE, act));
            }
        }
        return availableMoves;
    }

    /**
     * get all agreeable arguments
     * @return arguments
     */
    public ArrayList<Argument> getAgreeable(){

        return dvaf.getPreferredExtension().stream()
                .filter(argument -> argument.getSign() == Sign.POSITIVE)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * get all arguments with agreeable actions
     * @return arguments
     */
    public ArrayList<Argument> getAgreeable1(ArrayList<Argument> agreeable){
        HashSet<String> agreeableAction = new HashSet<>();
        for(Argument arg: agreeable){
            agreeableAction.add(arg.getAct());
        }
        agreeable.clear();
        for (Argument arg: arguments){
            if(agreeableAction.contains(arg.getAct())){
                agreeable.add(arg);
            }
        }
        return agreeable;
    }

    public HashMap<String, Integer> getAudiences(){
        return audiences;
    }

    public HashSet<String> getAgreeableAction(){
        ArrayList<Argument> agreeable = getAgreeable();
        HashSet<String> agreeableAction = new HashSet<>();
        for(Argument arg: agreeable){
            agreeableAction.add(arg.getAct());
        }
        return agreeableAction;
    }
    /**
     * return an agent's audiences
     * @return audience
     */
    public HashMap<String, Integer> collectAgentInfo(){
        return this.audiences;
    }

    /**
     * return this agent's possibility distribution model
     * @return possibility distribution
     */
    public HashMap<String, Float> getPossibility(){
        return model.getPossibility();
    }

    /**
     * get id of this agent
     * @return agentId
     */
    public int getAgentId(){
        return AgentId;
    }

    /**
     * get the agent's dialogue ivaf
     * @return
     */
    public IVAF getDvaf(){
        return dvaf;
    }

    /**
     * reset the agent
     */
    public void reset(){
        dvaf = null;
        audiences.clear();
        arguments.clear();
    }
}
