package org.multiAgent;

import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.IVAFramework.Argument;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogueSystem {

    public static ArrayList<Agent> agents = new ArrayList<>();
    public static Messager messager = new Messager();
    public static int counter = 0;
    public static String topic = null;
    public DialogueSystem(){

    }

    public void setTopic(String topic){
        DialogueSystem.topic = topic;
    }
    public void addAgent(HashMap<String, Integer> audiences, ArrayList<Argument> arguments){
        agents.add(new Agent(audiences, arguments));
    }

    public boolean run(){

        while(messager.checkClose() == 0){
            for(Agent currentAgent: agents){

                currentAgent.Act();
                counter++;
            }
        }

        return true;
    }
}
