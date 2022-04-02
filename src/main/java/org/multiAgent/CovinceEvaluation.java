package org.multiAgent;

import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Models.RandomModel;
import org.multiAgent.Models.SocialWelfareModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CovinceEvaluation {
    public static void main(String[] args) throws IOException {
        int ROUNDS = 1000;
        File writeFile = new File("data/CovinceEvaluation/NashDynamic.csv");
        File writeFile1 = new File("data/CovinceEvaluation/Random.csv");
        File writeFile2 = new File("data/CovinceEvaluation/SocialWelfare.csv");

        BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
        BufferedWriter writeText1 = new BufferedWriter(new FileWriter(writeFile1));
        BufferedWriter writeText2 = new BufferedWriter(new FileWriter(writeFile2));

        writeText.write("AGENTS,ACTIONS,VALUES,ARGUMENTS,RANDOM AVERAGE LENGTH,RANDOM SUCCESS,RANDOM COVINCE,RANDOM RANK");
        writeText1.write("AGENTS,ACTIONS,VALUES,ARGUMENTS,NASH AVERAGE LENGTH,NASH SUCCESS,NASH COVINCE,NASH RANK");
        writeText2.write("AGENTS,ACTIONS,VALUES,ARGUMENTS,Social AVERAGE LENGTH,Social SUCCESS,Social COVINCE,Social RANK");
        for (int AGENTS = 2 ; AGENTS <= 2; AGENTS ++){
            for (int VALUES = 2; VALUES <= 10 ; VALUES += 2){
                for (int ACTIONS = 2; ACTIONS <= 10; ACTIONS += 2){
                    for (int ARGUMENTS = 2; ARGUMENTS <= VALUES * ACTIONS * 2; ARGUMENTS += 2 ){
                        System.out.println(AGENTS + " " + VALUES + " " + ACTIONS + " " + ARGUMENTS);
                        float RandomSuccess = 0;
                        float NashSuccess = 0;
                        float SocialSuccess = 0;

                        float RandomCovince = 0;
                        float NashCovince = 0;
                        float SocialCovince = 0;

                        float RandomRank = 0;
                        float NashRank = 0;
                        float SocialRank = 0;

                        float AverageLengthR = 0;
                        float AverageLengthN = 0;
                        float AverageLengthS = 0;

                        float totalRank = 0;
                        float totalRank1 = 0;
                        float totalRank2 = 0;

                        for (int z = 0; z < ROUNDS ; z ++){
                            RandomGenerator generator = new RandomGenerator(2);
                            DialogueSystem dialogue = new DialogueSystem();
                            Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> temp = generator.getByAgent(ACTIONS, ARGUMENTS, VALUES);
                            ArrayList<ArrayList<Argument>> arguments = temp.getKey();
                            ArrayList<HashMap<String, Integer>> audiences = temp.getValue();

                            Model nashModel = new NashDynamicModel();
                            Model randomModel = new RandomModel();
                            Model socialModel = new SocialWelfareModel();

                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), randomModel);
                            }

                            float[] result =  dialogue.evaluationRun2("topic");
                            dialogue.reset();

                            float success = result[0];
                            float Convince = result[1];
                            float AverageRank = result[2];
                            float AverageLength = result[3];
                            float countRank = result[4];

                            RandomSuccess += success;
                            RandomCovince += Convince;
                            RandomRank += AverageRank;
                            AverageLengthR += AverageLength;
                            totalRank += countRank;

                            DialogueSystem dialogue1 = new DialogueSystem();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), nashModel);
                            }
                            float[] result1 =  dialogue1.evaluationRun2("topic");
                            dialogue1.reset();

                            float success1 = result1[0];
                            float Convince1 = result1[1];
                            float AverageRank1 = result1[2];
                            float AverageLength1 = result1[3];
                            float countRank1 = result1[4];

                            NashSuccess += success1;
                            NashCovince += Convince1;
                            NashRank += AverageRank1;
                            AverageLengthN += AverageLength1;
                            totalRank1 += countRank1;

                            DialogueSystem dialogue2 = new DialogueSystem();
                            for (int i = 0; i < arguments.size(); i++){
                                dialogue.addAgent(audiences.get(i), arguments.get(i), socialModel);
                            }
                            float[] result2 =  dialogue2.evaluationRun2("topic");
                            dialogue2.reset();

                            float success2 = result2[0];
                            float Convince2 = result2[1];
                            float AverageRank2 = result2[2];
                            float AverageLength2 = result2[3];
                            float countRank2 = result2[4];

                            SocialSuccess += success2;
                            SocialCovince += Convince2;
                            SocialRank += AverageRank2;
                            AverageLengthS += AverageLength2;
                            totalRank2 += countRank2;

                        }
                        writeText.newLine();
                        writeText.write(AGENTS + "," + ACTIONS+ "," +VALUES + "," + ARGUMENTS + "," + AverageLengthR / RandomSuccess + "," + RandomSuccess / ROUNDS + "," + RandomCovince / RandomSuccess +  "," + (RandomRank / totalRank)/ VALUES);

                        writeText1.newLine();
                        writeText1.write(AGENTS + "," + ACTIONS+ "," +VALUES + "," + ARGUMENTS + "," + AverageLengthN / NashSuccess + "," + NashSuccess / ROUNDS + "," + NashCovince / NashSuccess +  "," + (NashRank / totalRank1)/ VALUES);

                        writeText2.newLine();
                        writeText2.write(AGENTS + "," + ACTIONS+ "," +VALUES + "," + ARGUMENTS + "," + AverageLengthS / SocialSuccess + "," + SocialSuccess / ROUNDS + "," + SocialCovince / NashSuccess +  "," + (SocialRank / totalRank2)/ VALUES);}
                }
            }
        }

        writeText.flush();
        writeText.close();
        writeText1.flush();
        writeText1.close();
        writeText2.flush();
        writeText2.close();
        }


}




