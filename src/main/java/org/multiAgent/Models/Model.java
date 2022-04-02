package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;

public interface Model {
    public void initialize(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self);
    public void update(HashMap<Agent, HashMap<String, Float>> possibility);
    public HashMap<String, Float> getPossibility();
    public HashMap<String, Float> getDistribution();
    public void demote(String self, String other, float strengh);
    public void promote(String self);
}
