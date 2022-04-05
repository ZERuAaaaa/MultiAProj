package org.multiAgent.example;

import org.multiAgent.DialogueSystem;
import org.multiAgent.RandomGenerator;

public class exampleUsingRandomGenerator {

    public static void main(String[] args) throws Exception {
        DialogueSystem dialogue = new DialogueSystem();

        RandomGenerator rand = new RandomGenerator();

        dialogue.initialize(rand.generate(2,10,6,100, "Nash Dynamic"));

        dialogue.runAndDisplay(rand.getTopic());
    }
}
