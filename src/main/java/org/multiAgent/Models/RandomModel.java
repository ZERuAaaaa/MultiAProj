package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RandomModel implements Model{

    private HashMap<String, Integer> selfPayoff = new HashMap<>();
    private HashMap<String, Float> selfPossibility = new HashMap<>();
    public RandomModel(){}

    public void initialize(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self){
        selfPayoff = dialogueInfo.getValue().get(self);
        for (Map.Entry<String, Integer> entry: selfPayoff.entrySet()){
            selfPossibility.put(entry.getKey(),  (float) 1 / selfPayoff.size());
        }
    }

    public void update(HashMap<Agent, HashMap<String, Float>> possibility){

    };

    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }

    public HashMap<String, Float> getDistribution(){
        return selfPossibility;
    }

}
