package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This model does not perform any update and only return random probability distribution to choose argument
 */
public class RandomModel implements Model{

    private final HashMap<String, Float> selfPossibility = new HashMap<>();
    Random rand = new Random();
    Matrix matrix;
    public RandomModel(){}
    @Override
    public void initialize(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self){
        HashMap<String, Integer> selfPayoff = dialogueInfo.getValue().get(self);
        for (Map.Entry<String, Integer> entry: selfPayoff.entrySet()){
            selfPossibility.put(entry.getKey(),  rand.nextFloat(1));
        }

    }

    public void update(HashMap<Agent, HashMap<String, Float>> possibility){

    }
    @Override
    public HashMap<String, Float> getPossibility(){
        return selfPossibility;
    }
    @Override
    public HashMap<String, Float> getDistribution(){
        return selfPossibility;
    }
    @Override
    public void demote(String self, String other, float strength) {

    }
    public void print(){
        matrix.print();
    }


}
