package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.NashDynamicModel;
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
public class normalEvaluation {

    public static void main(String[] args) throws Exception{

        int ROUNDS = 1000;
        File writeFile = new File("data/normalEvaluation/normalEvaluation.csv");

        BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

        writeText.write("VALUES,ACTIONS,ARGUMENTS,DIALOGUE SUCCESS,CONSENSUS SUCCESS,DS0,DS1,DS2,DS3,DSCORE,CS0,CS1,CS2,CS3,CSCORE");

        for (int AGENTS = 2 ; AGENTS <= 2; AGENTS ++){
            for (int VALUES = 2; VALUES <= 10 ; VALUES += 2){
                for (int ACTIONS = 2; ACTIONS <= 10; ACTIONS += 2){
                    for (int ARGUMENTS = 2; ARGUMENTS <= VALUES * ACTIONS * 2; ARGUMENTS += 2 ){
                        System.out.println(VALUES + " " + ACTIONS + " " + ARGUMENTS);

                        float lengthA = 0;
                        float dialogueSucessA = 0;
                        float consensusSucessA = 0;
                        float dialogueScore1 = 0;
                        float dialogueScore2 = 0;
                        float dialogueScore3 = 0;

                        float consensusScore1 = 0;
                        float consensusScore2 = 0;
                        float consensusScore3 = 0;


                        for (int e = 0; e <  ROUNDS; e++){
                            RandomGenerator generator = new RandomGenerator(AGENTS);
                            DialogueSystem dialogue = new DialogueSystem();
                            Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> temp = generator.getByAgent(ACTIONS, ARGUMENTS, VALUES);
                            ArrayList<ArrayList<Argument>> arguments = temp.getKey();
                            ArrayList<HashMap<String, Integer>> audiences = temp.getValue();

                            RandomModel randomModel = new RandomModel();
                            NashDynamicModel nashModel = new NashDynamicModel();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), nashModel);
                            }
                            int[] result =  dialogue.evaluationRun("topic");
                            dialogue.reset();

                            int length = result[0];
                            int dialogueSucess = result[1];
                            int consensusSucess = result[2];
                            int dialogueScore = result[3];
                            int consensusScore = result[4];

                            lengthA += length;
                            dialogueSucessA += dialogueSucess;
                            consensusSucessA += consensusSucess;

                            switch(dialogueScore){
                                case (1): dialogueScore1++; break;
                                case (2): dialogueScore2++; break;
                                case (3): dialogueScore3++; break;
                            }

                            switch(consensusScore){
                                case (1): consensusScore1++; break;
                                case (2): consensusScore2++; break;
                                case (3): consensusScore3++; break;
                            }


                        }
                        writeText.newLine();
                        writeText.write(VALUES +","+
                                ACTIONS+"," +
                                ARGUMENTS +","+
                                (ROUNDS - dialogueSucessA) + ","+
                                dialogueSucessA / ROUNDS+"," +
                                consensusSucessA / ROUNDS+"," +
                                dialogueScore1 / ROUNDS+"," +
                                dialogueScore2 / ROUNDS+"," +
                                dialogueScore3 / ROUNDS+"," +
                                (dialogueScore1 + dialogueScore2 + dialogueScore3) / ROUNDS + ","+
                                (ROUNDS -consensusSucessA) + ","+
                                consensusScore1 / ROUNDS+"," +
                                consensusScore2/ ROUNDS+"," +
                                consensusScore3/ ROUNDS +  ","+
                                (consensusScore1 + consensusScore2 + consensusScore3) / ROUNDS);
                    }
                }
            }
        }
        writeText.flush();
        writeText.close();
    }


}