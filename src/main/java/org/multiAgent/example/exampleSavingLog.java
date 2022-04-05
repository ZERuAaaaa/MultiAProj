package org.multiAgent.example;

import org.multiAgent.DialogueSystem;
import org.multiAgent.RandomGenerator;

public class exampleSavingLog {

    public static  void main(String[] args) throws Exception {
        DialogueSystem dialogue = new DialogueSystem();

        RandomGenerator rand = new RandomGenerator();

        dialogue.initialize(rand.generate(2,10,6,60, "Nash Dynamic"));

        //run and save dialogue result and log to target csv file
        dialogue.runAndSave(rand.getTopic(),"src/main/java/org/multiAgent/example/result.csv",
                "src/main/java/org/multiAgent/example/log.csv");


    }
}
