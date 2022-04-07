package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import java.util.*;


/**
 * this class implemented a dialogue system, which could handle multiple agents,
 * performing communication
 */
public class DialogueSystem{

    public static ArrayList<Agent> agents = new ArrayList<>();
    // the dialogue maintains a messager recording every move of agents
    public static Messager messager = new Messager();
    // dialogue counter
    public static int counter = 0;
    // the goal for the dialogue
    public static String topic = null;

    /**
     * reset the dialogue
     */
    public void reset(){
        DialogueSystem.topic = null;
        DialogueSystem.messager = new Messager();
        DialogueSystem.agents.clear();
        DialogueSystem.counter = 0;
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
     * use pre created data to initialize the dialogue
     * @param data data to initialize the dialogue
     */
    public void initialize(Pair<Pair<ArrayList<ArrayList<Argument>>,
            ArrayList<HashMap<String,Integer>>>,ArrayList<Model>> data){
        ArrayList<ArrayList<Argument>> agentArguments = data.getKey().getKey();
        ArrayList<HashMap<String,Integer>> agentAudiences = data.getKey().getValue();
        ArrayList<Model> models = data.getValue();
        for (int e = 0; e < agentArguments.size(); e++){
            agents.add(new Agent(agentAudiences.get(e), agentArguments.get(e), models.get(e)));
        }
    }

    /**
     * start the dialogue under a topic and return the result of the dialogue
     * @param topic topic
     * @return the result of the dialogue of fail
     */
    public String run(String topic) throws Exception {
        checkCompleteness();
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
                // an agent updates it model first.
                currentAgent.updateModel(getDialoguePossibility());
                // then pick a move
                Move currentMove = currentAgent.Act(messager);
                // agent broadcast the move via messager and the messager record the move
                messager.broadCast(currentMove, currentAgent, agents);
                // check whether the dialgoeu should close
                close = messager.checkClose();
                if(close != 0){
                    break;
                }

                counter++;
            }

        }
        String output;
        if (messager.getLastOne().getType() == MoveType.AGREE){
            output =(String) messager.getLastOne().getContent();
        }else{
            output = "fail";
        }
        return output;
    }

    /**
     * start the dialogue under a topic, and display the process of the dialogue
     * @param topic topic
     * @return 1 if is an agreed close, -1 if it's a match close
     */
    public Integer runAndDisplay(String topic) throws Exception {
        checkCompleteness();
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
                // an agent updates it model first.
                currentAgent.updateModel(getDialoguePossibility());
                // then pick a move
                Move currentMove = currentAgent.Act(messager);
                // agent broadcast the move via messager and the messager record the move
                messager.broadCast(currentMove, currentAgent, agents);
                // check whether the dialgoeu should close
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
     * run the dialogue under a topic and save log and result to csv file
     * @param topic the goal of the dilaogue
     * @param resultUrl path to save the result
     * @param logUrl path to save the log
     * @return 1 if is an agreed close, -1 if it's a match close
     */
    public Integer runAndSave(String topic, String resultUrl,String logUrl) throws Exception {
        checkCompleteness();
        this.topic = topic;
        // the dialogue opened by the last agent in order
        Agent lastAgent = agents.get(agents.size() - 1);
        messager.broadCast(new Move(lastAgent,MoveType.OPEN,topic), lastAgent, agents);
        ArrayList<String[]> log = new ArrayList<>();
        log.add(new String[]{"ROUND","AGENTS","AGENTS AGRUMENTS","AGENTS AUDIENCES","MOVE"});
        counter++;
        log.add(new String[]{"0",lastAgent.toString(), "", "","", new Move(lastAgent,MoveType.OPEN,topic).toString()});
        // initialize the model and dvaf of each agent
        for (Agent age: agents){
            age.initializeByTopic(topic, getDialogueInfo());
            //agreeable.add(age.getAgreeableAction());
        }
        Integer close = 0;
        while(close == 0){
            for(Agent currentAgent: agents){
                String[] save = new String[5];
                // an agent updates it model first.
                currentAgent.updateModel(getDialoguePossibility());
                // then pick a move
                Move currentMove = currentAgent.Act(messager);
                save[0] = counter + "";
                save[1] = currentAgent.toString();
                save[2] = currentAgent.getArguments().toString();
                save[3] = currentAgent.getAudiences().toString();
                save[4] = currentMove.toString();
                log.add(save);
                // agent broadcast the move via messager and the messager record the move
                messager.broadCast(currentMove, currentAgent, agents);
                // check whether the dialgoeu should close
                close = messager.checkClose();
                if(close != 0){
                    break;
                }

                counter++;
            }

        }
        String output;
        if (messager.getLastOne().getType() == MoveType.AGREE){
            output =(String) messager.getLastOne().getContent();
        }else{
            output = "fail";
        }
        if (resultUrl != null){
            CsvTool.write(output, resultUrl);
        }

        if (logUrl != null){
            CsvTool.write(log,logUrl);
        }
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
     * this function check whether a dialogue can be formed
     * according to dialogue information
     */
    public void checkCompleteness() throws Exception{
        if (agents.size() < 2) {
            throw new Exception("dialogue need more than two agents to construct");
        }
        ArrayList<Set<String>> audiences = new ArrayList<>();
        for (Agent age: agents){
            audiences.add(age.getAudiences().keySet());
        }
        boolean same = true;
        for(int i = 0; i < audiences.size(); i++){
            for (int e = i; e < audiences.size(); e++){
                if (!audiences.get(i).equals(audiences.get(e))){
                    same = false;
                }
            }
        }
        for (Agent age: agents){
            if (age.getArguments().size() == 0){
                throw new Exception("Each agents should hold at least one argument");
            }
        }
        if (!same){
            throw new Exception("All agents should have audiences over all values involved in this dialogue, " +
                    "for example Agent X have audiences{A=1,B=2,C=3}," +
                    " \n Agent Y should also have audiences over A B and C");
        }
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

    public ArrayList<Move> getLog() {
        return messager.messageLog;
    }
}
