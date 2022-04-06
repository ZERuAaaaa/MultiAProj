package org.multiAgent.example;

import org.multiAgent.DialogueSystem;
import org.multiAgent.RandomGenerator;

/**
 * This example show how to save dialogue result to csv file, and initialize dialogue via random generator.
 */
public class exampleSavingLog {

    public static  void main(String[] args) throws Exception {

        DialogueSystem dialogue = new DialogueSystem();
        // generate dialogue data via random generator(number of agents, number of actions, number of values,
        // number of arguments, model using within the dialogue)
        RandomGenerator rand = new RandomGenerator();
        dialogue.initialize(rand.generate(2,10,6,60, "Nash Dynamic"));

        //run and save dialogue result and log to target csv file
        dialogue.runAndSave(rand.getTopic(),"src/main/java/org/multiAgent/example/result.csv",
                "src/main/java/org/multiAgent/example/log.csv");


    }
}
