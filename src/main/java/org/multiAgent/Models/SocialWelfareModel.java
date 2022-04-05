package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implemented a model to maximum social welfare for agents.
 */
public class SocialWelfareModel implements Model{
    ArrayList<Agent> agents;
    HashMap<Agent, HashMap<String, Integer>> audiences;

    Matrix matrix ;
    
    HashMap<String, Float> selfPossibility = new HashMap<>();
    HashMap<String, Float> dialoguePossibility = new HashMap<>();
    private Agent self;

    /**
     * initialize pay off matrix of the model to for selecting arguemnt
     * @param dialogueInfo agents information within the dialogue
     * @param self this agent
     */
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

        matrix = new Matrix(selfPayoff, dialoguePayoff);
        for (Map.Entry<String, Float> entry: selfPayoff.entrySet()){
            selfPossibility.put(entry.getKey(),  (float) 1 / selfPayoff.size());
        }
    }

    /**
     * update the matrix according to the dialogue
     * @param possibility other agent's possibility of choosing value, not using in this part
     */
    @Override
    public void update(HashMap<Agent, HashMap<String, Float>> possibility) {
        float total = 0;
        for (Map.Entry<String, Float> entry: selfPossibility.entrySet()){
            HashMap<String, float[]> row = matrix.getX(entry.getKey());
            for (Map.Entry<String, float[]> block: row.entrySet()){
                total += block.getValue()[0] + block.getValue()[1];
            }
        }

        for (Map.Entry<String, Float> entry: selfPossibility.entrySet()){
            HashMap<String, float[]> row = matrix.getX(entry.getKey());
            float sum = 0;
            for (Map.Entry<String, float[]> block: row.entrySet()){
                sum += block.getValue()[0] + block.getValue()[1];
            }
            selfPossibility.put(entry.getKey(), sum / total);
        }
    }
    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }

    /**
     *  @return probability distribution over values
     */
    @Override
    public HashMap<String, Float> getDistribution() {
        return selfPossibility;
    }

    /**
     * demote a value
     * @param self demoted value of the agent
     * @param other value of other agent demoting the agent's value
     * @param strength degree of reduction to the demoted value
     */
    @Override
    public void demote(String self, String other, float strength) {
        matrix.demote(self,other, strength);
    }

    public void print(){
        matrix.print();
    }

}
