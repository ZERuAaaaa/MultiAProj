package org.multiAgent.Models;

import javafx.util.Pair;
import org.multiAgent.Agent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implement an interface of models, which agents could use model
 * to select asserting arguments.
 */
public interface Model {
    void initialize(Pair<ArrayList<Agent>, HashMap<Agent, HashMap<String, Integer>>> dialogueInfo, Agent self);
    void update(HashMap<Agent, HashMap<String, Float>> possibility);
    HashMap<String, Float> getPossibility();
    HashMap<String, Float> getDistribution();
    void demote(String self, String other, float strength);
    void print();
    Matrix getMatrix();
}
