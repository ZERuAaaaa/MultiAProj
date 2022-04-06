package org.multiAgent;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Models.RandomModel;
import org.multiAgent.Models.SocialWelfareModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A csv tool could load dialogue information from csv file or write dialogue information into csv file
 */
public class CsvTool {
    /**
     * default constructor
     */
    public CsvTool(){}

    /**
     * load dialogue information from csv file
     * @param url path to the file
     * @return dialogue information
     * @throws Exception exception
     */
    public Pair<Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String,Integer>>>,
            ArrayList<Model>> loadForCsv(String url) throws Exception{
        if (url.equals("") || url == null){
            throw new Exception("url cannot be empty");
        }
        ArrayList<HashMap<String, Integer>> maps = new ArrayList<>();
        ArrayList<ArrayList<Argument>> AgentArguemnts = new ArrayList<>();
        ArrayList<Model> models = new ArrayList<>();
        try{
            CSVReader reader = new CSVReader(new FileReader(url));
            String[] initial = reader.readNext();
            int numberOfAgent = Integer.parseInt(initial[0]);
            int numberOfAudience = Integer.parseInt(initial[1]);

            for (int i = 0; i < numberOfAgent; i++){
                HashMap<String,Integer> audience = new HashMap<>();
                for (int a = 0; a < numberOfAudience; a++){
                    String[] current = reader.readNext();
                    audience.put(current[0], Integer.parseInt(current[1]));
                }
                maps.add(audience);
                String[] agentInfo = reader.readNext();
                int numberOfArgument = Integer.parseInt(agentInfo[0]);
                switch(agentInfo[1]){
                    case("Nash Dynamic"):  models.add(new NashDynamicModel()); break;
                    case("Random"):models.add(new RandomModel());break;
                    case("Social Welfare"): models.add(new SocialWelfareModel());break;
                    default:
                        throw new Exception("can't find corresponding model");
                }

                ArrayList<Argument> arguments = new ArrayList<>();
                for (int e= 0; e < numberOfArgument;e++){
                    String[] current = reader.readNext();
                    String action = current[0];
                    String goal = current[1];
                    String value = current[2];
                    String sign = current[3];
                    Argument arg = new Argument(action,goal,sign,value);
                    arguments.add(arg);
                }
                AgentArguemnts.add(arguments);
            }
        }catch (Exception e){
            throw new Exception("please check the format of input csv file");
        }
        return new Pair<>(new Pair<>(AgentArguemnts,maps), models);
    }

    public static void write(String output, String resultUrl) {
        File file = new File(resultUrl);
        try {
            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);
            String[] write = {output};
            writer.writeNext(write);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(ArrayList<String[]> log, String resultUrl) {
        File file = new File(resultUrl);
        try {
            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);
            for (String[] current: log){
                writer.writeNext(current);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
