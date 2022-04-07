package org.multiAgent;

import org.multiAgent.BroadCastCommunication.Move;

import java.util.ArrayList;

public class randomDialogue {

    public static void main(String[] args) throws Exception {

        try{
            DialogueSystem dialogue = new DialogueSystem();

            RandomGenerator rand = new RandomGenerator();
            // initialize the dialogue with randomly generated data(number of agents, number of actions, number of values, number of arguments)
            dialogue.initialize(rand.generate(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])));
            // run the dialogue and display the process
            dialogue.runAndDisplay(rand.getTopic());
            // get the log of the dialogue
            ArrayList<Move> log = dialogue.getLog();
            // reset the dialogue system
            dialogue.reset();
        }catch (Exception e){
            throw new Exception("input format: [number of agents],[number of actions], [number of values], [number of arguments]");
        }
    }
}
