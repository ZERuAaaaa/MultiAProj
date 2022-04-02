package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;
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
public class evaluation {

    public static void main(String[] args) throws Exception{

        int ROUNDS = 5;
        File writeFile = new File("data/writeSucess.csv");
        File writeFile1 = new File("data/writeSucess1.csv");

        BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
        BufferedWriter writeText1 = new BufferedWriter(new FileWriter(writeFile1));

        writeText.write("VALUES,ACTIONS,ARGUMENTS,AVERAGE LENGTH,DIALOGUE SUCCESS,CONSENSUS SUCCESS,DIALOGUE SCORE,CONSENSUS SCORE");
        writeText1.write("VALUES,ACTIONS,ARGUMENTS,AVERAGE LENGTH,DIALOGUE SUCCESS,CONSENSUS SUCCESS,DIALOGUE SCORE,CONSENSUS SCORE");
        for (int AGENTS = 2 ; AGENTS <= 2; AGENTS ++){
            for (int VALUES = 2; VALUES < 10 ; VALUES += 2){
                for (int ACTIONS = 2; ACTIONS < 10; ACTIONS += 2){
                    for (int ARGUMENTS = 2; ARGUMENTS <= VALUES * ACTIONS * 2; ARGUMENTS += 2 ){
                        System.out.println(AGENTS + " " + VALUES + " " + ACTIONS + " " + ARGUMENTS);
                        float lengthA = 0;
                        float dialogueSucessA = 0;
                        float consensusSucessA = 0;
                        float dialogueScoreA = 0;
                        float consensusScoreA = 0;

                        float lengthB = 0;
                        float dialogueSucessB = 0;
                        float consensusSucessB = 0;
                        float dialogueScoreB = 0;
                        float consensusScoreB = 0;

                        for (int e = 0; e <  ROUNDS; e++){
                            RandomGenerator generator = new RandomGenerator(AGENTS);
                            DialogueSystem dialogue = new DialogueSystem();
                            Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> temp = generator.getByAgent(ACTIONS, ARGUMENTS, VALUES);
                            ArrayList<ArrayList<Argument>> arguments = temp.getKey();
                            ArrayList<HashMap<String, Integer>> audiences = temp.getValue();
                            NashDynamicModel nashModel = new NashDynamicModel();
                            RandomModel randomModel = new RandomModel();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), randomModel);
                            }
                            int[] result =  dialogue.evaluationRun("topic");
                            dialogue.reset();

                            DialogueSystem dialogue1 = new DialogueSystem();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), nashModel);
                            }
                            int[] result1 =  dialogue.evaluationRun("topic");
                            dialogue1.reset();


                            int length0 = result[0];
                            int dialogueSucess0 = result[1];
                            int consensusSucess0 = result[2];
                            int dialogueScore0 = result[3];
                            int consensusScore0 = result[4];

                            lengthA += length0;
                            dialogueSucessA += dialogueSucess0;
                            consensusSucessA += consensusSucess0;
                            dialogueScoreA += dialogueScore0;
                            consensusScoreA += consensusScore0;

                            int length1 = result1[0];
                            int dialogueSucess1 = result1[1];
                            int consensusSucess1 = result1[2];
                            int dialogueScore1 = result1[3];
                            int consensusScore1 = result1[4];

                            lengthB += length1;
                            dialogueSucessB += dialogueSucess1;
                            consensusSucessB += consensusSucess1;
                            dialogueScoreB += dialogueScore1;
                            consensusScoreB += consensusScore1;
                        }
                        writeText.newLine();
                        writeText.write(VALUES +","+ ACTIONS+"," + ARGUMENTS +","+ lengthA / dialogueSucessA+"," + dialogueSucessA / ROUNDS+"," + consensusSucessA / ROUNDS+"," + dialogueScoreA / dialogueSucessA+"," +  consensusScoreA / consensusSucessA);
                        writeText1.newLine();
                        writeText1.write(VALUES +","+ ACTIONS+"," + ARGUMENTS +","+ lengthB / dialogueSucessB+"," + dialogueSucessB / ROUNDS+"," + consensusSucessB / ROUNDS+"," + dialogueScoreB / dialogueSucessB+"," +  consensusScoreB / consensusSucessB);
                    }
                }
            }
        }
        writeText.flush();
        writeText.close();
        writeText1.flush();
        writeText1.close();
    }


}