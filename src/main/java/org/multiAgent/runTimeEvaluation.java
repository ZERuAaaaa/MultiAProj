package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.RandomModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class runTimeEvaluation {

    public static void main(String[] args) throws Exception{

        int ROUNDS = 10;
        File writeFile = new File("data/TimeEvaluation/TimeEvaluation.csv");

        BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

        writeText.write("VALUES,ACTIONS,ARGUMENTS,TIME");

        for (int AGENTS = 2 ; AGENTS <= 2; AGENTS ++){
            for (int VALUES = 2; VALUES <= 10 ; VALUES += 2){
                for (int ACTIONS = 2; ACTIONS <= 10; ACTIONS += 2){
                    for (int ARGUMENTS = 2; ARGUMENTS <= VALUES * ACTIONS * 2; ARGUMENTS += 2 ){
                        System.out.println(VALUES + " " + ACTIONS + " " + ARGUMENTS);
                        long totaltime = 0;
                        for (int e = 0; e <  ROUNDS; e++){
                            RandomGenerator generator = new RandomGenerator(AGENTS);
                            DialogueSystem dialogue = new DialogueSystem();
                            Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> temp = generator.getByAgent(ACTIONS, ARGUMENTS, VALUES);
                            ArrayList<ArrayList<Argument>> arguments = temp.getKey();
                            ArrayList<HashMap<String, Integer>> audiences = temp.getValue();

                            RandomModel randomModel = new RandomModel();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), randomModel);
                            }


                            long startTime = System.currentTimeMillis();
                            dialogue.run("topic");
                            long endTime = System.currentTimeMillis();
                            long time = (endTime - startTime) / 1000;

                            totaltime += time;

                            dialogue.reset();

                            }
                        writeText.newLine();
                        writeText.write(VALUES +","+
                                ACTIONS+"," +
                                ARGUMENTS + "," + totaltime / ROUNDS);
                    }
                }
            }
        }
        writeText.flush();
        writeText.close();
    }


}