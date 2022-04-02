package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModestyModel implements Model{
    ArrayList<Agent> agents;
    HashMap<Agent, HashMap<String, Integer>> audiences;

    Matric matric ;

    HashMap<String, Float> selfPossibility = new HashMap<>();
    HashMap<String, Float> dialoguePossibility = new HashMap<>();
    private Agent self;

    @Override
    public void initialize(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self) {
        HashMap<String, Float> selfPayoff = new HashMap<>();
        HashMap<String, Float> dialoguePayoff = new HashMap<>();

        this.agents = dialogueInfo.getKey();
        this.audiences = dialogueInfo.getValue();
        this.self = self;
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

        matric = new Matric(selfPayoff, dialoguePayoff);
        for (Map.Entry<String, Float> entry: selfPayoff.entrySet()){
            selfPossibility.put(entry.getKey(),  (float) 1 / selfPayoff.size());
        }
    }

    @Override
    public void update(HashMap<Agent, HashMap<String, Float>> possibility) {
        float total = 0;
        for (Map.Entry<String, Float> entry: selfPossibility.entrySet()){
            HashMap<String, float[]> row = matric.getX(entry.getKey());
            for (Map.Entry<String, float[]> block: row.entrySet()){
                total += block.getValue()[1];
            }
        }

        for (Map.Entry<String, Float> entry: selfPossibility.entrySet()){
            HashMap<String, float[]> colomn = matric.getY(entry.getKey());
            float sum = 0;
            for (Map.Entry<String, float[]> block: colomn.entrySet()){
                sum += block.getValue()[1];
            }
            selfPossibility.put(entry.getKey(), sum / total);
        }
    }

    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }

    @Override
    public HashMap<String, Float> getDistribution() {
        return selfPossibility;
    }

    @Override
    public void demote(String self, String other, float strengh) {
        matric.demote(self,other,strengh);
    }

    @Override
    public void promote(String self) {
        matric.promote(self);
    }

}
