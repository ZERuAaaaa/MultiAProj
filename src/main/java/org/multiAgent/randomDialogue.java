package org.multiAgent;

public class randomDialogue {

    public static void main(String[] args) throws Exception {

        try{
            DialogueSystem dialogue = new DialogueSystem();

            RandomGenerator rand = new RandomGenerator();

            dialogue.initialize(rand.generate(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])));

            dialogue.runAndDisplay(rand.getTopic());
            dialogue.reset();
        }catch (Exception e){
            throw new Exception("input format: [number of agents],[number of actions], [number of values], [number of arguments]");
        }
    }
}
