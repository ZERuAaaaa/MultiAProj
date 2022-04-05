package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class implemented a model using nash dynamic approach to update
 * with respect of other agents audiences and possibility of choosing actions
 */
public class NashDynamicModel implements Model{

    ArrayList<Agent> agents;
    HashMap<Agent, HashMap<String, Integer>> audiences;

    Matrix matrix;

    HashMap<String, Float> selfPossibility = new HashMap<>();
    HashMap<String, Float> dialoguePossibility = new HashMap<>();
    Agent self;

    public NashDynamicModel() {}

  /**
   * agent initialize its model at the beginning of the dialogue with * information of other agents'
   * audiences
   * @param dialogueInfo agents information within the dialogue
   * @param self agent
   */
  @Override
  public void initialize(
      Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self) {
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
     * update the model at the agent's turn according to other agents' information
     * @param possibility possibility of other agent choosing values
     */
    @Override
    public void update(HashMap<Agent, HashMap<String, Float>> possibility){
        dialoguePossibility.clear();
        for (Map.Entry<Agent, HashMap<String, Float>> obj: possibility.entrySet()){
            if(obj.getKey() != self){
                for (Map.Entry<String, Float> entry: obj.getValue().entrySet()){
                    if (dialoguePossibility.containsKey(entry.getKey())){
                        dialoguePossibility.put(entry.getKey(),
                                dialoguePossibility.get(entry.getKey()) + entry.getValue());
                    }else{
                        dialoguePossibility.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        dialoguePossibility.replaceAll((k, v) -> v / (agents.size() - 1));

        HashMap<String, Float> expectedUtilities = new HashMap<>();

        Set<String> audience = audiences.get(self).keySet();
        HashMap<String,HashMap<String, float[]>> payOffMatric = matrix.getMatric();
        for (String aud: audience){
            HashMap<String, float[]> row = payOffMatric.get(aud);
            float sum = 0;
            for (Map.Entry<String, float[]> ele: row.entrySet()){
                sum += dialoguePossibility.get(ele.getKey()) * ele.getValue()[0];
            }
            expectedUtilities.put(aud,sum);
        }

        float SQ = 0;

        for (Map.Entry<String, Float> entry: expectedUtilities.entrySet()){
            SQ += selfPossibility.get(entry.getKey()) * entry.getValue();
        }

        float finalSQ = SQ;
        expectedUtilities.forEach((x, y) -> y = Math.max(y - finalSQ,0));

        float covSum = 0;
        for (Map.Entry<String,Float> co: expectedUtilities.entrySet()){
            covSum += co.getValue();
        }
        float finalCovSum = covSum;

        selfPossibility.replaceAll((k , v) ->
                (float) Math.round((selfPossibility.get(k) + expectedUtilities.get(k)) / (1 + finalCovSum) * 1000) / 1000);
    }

    /**
     * possibility of this agent choosing a value
     * @return
     */
    @Override
    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }

    /**
     * get the probability distribution over values
     * @return distribution
     */
    @Override
    public HashMap<String, Float> getDistribution(){
        return this.selfPossibility;
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