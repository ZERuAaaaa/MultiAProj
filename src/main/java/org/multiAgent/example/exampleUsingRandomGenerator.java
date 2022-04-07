package org.multiAgent.example;

import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.DialogueSystem;
import org.multiAgent.RandomGenerator;

import java.util.ArrayList;

public class exampleUsingRandomGenerator {

    public static void main(String[] args) throws Exception {
        DialogueSystem dialogue = new DialogueSystem();
        // generate dialogue data via random generator(number of agents, number of actions, number of values,
        // number of arguments, model using within the dialogue, model using within the dialogue)
        RandomGenerator rand = new RandomGenerator();

        dialogue.initialize(rand.generate(2,10,10,200, "Nash Dynamic"));
        // run the dialogue with the random goal
        dialogue.runAndDisplay(rand.getTopic());
        // get log
        ArrayList<Move> log = dialogue.getLog();
        // reset the dialogue
        dialogue.reset();
    }
}
