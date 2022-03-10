package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NashDynamicModel {

    ArrayList<Agent> agents;
    HashMap<Agent, HashMap<String, Integer>> audiences;

    HashMap<String, Float> selfPayoff = new HashMap<>();
    HashMap<String, Float> dialoguePayoff = new HashMap<>();

    HashMap<String, Float> selfPossibility = new HashMap<>();
    HashMap<String, Float> dialoguePossibility = new HashMap<>();
    Agent self;

    public NashDynamicModel(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self) {
        this.agents = dialogueInfo.getKey();
        this.audiences = dialogueInfo.getValue();
        this.self = self;
        buildPayoffMatrix();
        initialize();

    }

    public void initialize(){
        for (Map.Entry<String, Float> entry: selfPayoff.entrySet()){
           selfPossibility.put(entry.getKey(),  (float) 1 / selfPayoff.size());
        }
    }

    public void update(HashMap<Agent, HashMap<String, Float>> possibility){
        dialoguePossibility.clear();
        for (Map.Entry<Agent, HashMap<String, Float>> obj: possibility.entrySet()){
            if(obj.getKey() != self){
                for (Map.Entry<String, Float> entry: obj.getValue().entrySet()){
                    if (dialoguePossibility.containsKey(entry.getKey())){
                        dialoguePossibility.put(entry.getKey(), dialoguePossibility.get(entry.getKey()) + entry.getValue());
                    }else{
                        dialoguePossibility.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        dialoguePossibility.replaceAll((k, v) -> v / (agents.size() - 1));

        HashMap<String, Float> expectedUtilities = new HashMap<>();
        for (Map.Entry<String, Float> selfP: selfPayoff.entrySet()){
            float expect = 0;
            for (Map.Entry<String, Float> DP: dialoguePossibility.entrySet()){
                expect += selfP.getValue() * DP.getValue();
            }
            expectedUtilities.put(selfP.getKey(), expect);
        }
        float SQ = 0;
        for (Map.Entry<String, Float> SP: selfPossibility.entrySet()){
            for (Map.Entry<String, Float> EU: expectedUtilities.entrySet()){
                if (SP.getKey().equals(EU.getKey())){
                    SQ += SP.getValue() * EU.getValue();
                }
            }
        }
        HashMap<String, Float> cov = new HashMap<>();
        float covSum = 0;
        for (Map.Entry<String, Float> EU : expectedUtilities.entrySet()){
            float temp = Math.max(EU.getValue() - SQ, 0);
            covSum += temp;
            cov.put(EU.getKey(), temp);
        }
        float finalCovSum = covSum;
        selfPossibility.replaceAll((k , v) -> (float) Math.round((selfPossibility.get(k) + cov.get(k)) / (1 + finalCovSum) * 1000) / 1000);
    }

    public void buildPayoffMatrix(){
        for (Map.Entry<String, Integer> entry : audiences.get(self).entrySet()){
            selfPayoff.put(entry.getKey(), (float) entry.getValue());
        }
        for(Agent age : agents){
            if(age != self){
                HashMap<String, Integer> map = audiences.get(age);
                for(Map.Entry<String, Integer> entry: map.entrySet()){
                    if (dialoguePayoff.containsKey(entry.getKey())){
                        dialoguePayoff.put(entry.getKey(), dialoguePayoff.get(entry.getKey()) + entry.getValue());
                    }else{
                        dialoguePayoff.put(entry.getKey(),(float) entry.getValue());
                    }
                }
            }
        }
        dialoguePayoff.replaceAll((k, v) -> v / (agents.size() - 1));
    }


    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }

    public HashMap<String, Float> getDistribution(){
        return this.selfPossibility;
    }
}