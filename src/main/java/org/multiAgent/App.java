package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Models.RandomModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Delayed;

/**
 * Hello world!
 *
 */
public class App{

    public static void main(String[] args) {

        int win = 0;
        int loss = 0;
        int win1 = 0;
        int loss1 = 0;

        for (int e = 0; e < 1000; e++){
            System.out.println(e);
            RandomGenerator generator = new RandomGenerator(10);
            DialogueSystem dialogue = new DialogueSystem();
            Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> temp = generator.getByAgent(5, 50, 20);
            ArrayList<ArrayList<Argument>> arguments = temp.getKey();
            ArrayList<HashMap<String, Integer>> audiences = temp.getValue();
            NashDynamicModel nashModel = new NashDynamicModel();
            RandomModel randomModel = new RandomModel();

            for (int i = 0; i < arguments.size(); i++){
                dialogue.addAgent(audiences.get(i), arguments.get(i), randomModel);
            }
            int result =  dialogue.run("topic");
            if (result == 1){
                win ++;
            }else{
                loss++;
            }
            dialogue.reset();

            DialogueSystem dialogue1 = new DialogueSystem();
            for (int i = 0; i < arguments.size(); i++){
                dialogue.addAgent(audiences.get(i), arguments.get(i), nashModel);
            }
            int result1 =  dialogue1.run("topic");
            if (result1 == 1){
                win1 ++;
            }else{
                loss1++;
            }
            dialogue.reset();
        }
        System.out.println("Random win: " + win + " " + "loss: " + loss);
        System.out.println("Nash win: " + win1 + " " + "loss: " + loss1);

    }
}
