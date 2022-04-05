package org.multiAgent;

import com.opencsv.CSVReader;
import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CsvTool {
    public CsvTool(){}

    public Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String,Integer>>> loadForCsv(String url) throws Exception{
        CSVReader reader = new CSVReader(new FileReader(url));
        String[] initial = reader.readNext();
        int numberOfAgent = Integer.parseInt(initial[0]);
        int numberOfAudience = Integer.parseInt(initial[1]);
        ArrayList<HashMap<String, Integer>> maps = new ArrayList<>();
        ArrayList<ArrayList<Argument>> AgentArguemnts = new ArrayList<>();
        for (int i = 0; i < numberOfAgent; i++){
            HashMap<String,Integer> audience = new HashMap<>();
            for (int a = 0; a < numberOfAudience; a++){
                String[] current = reader.readNext();
                audience.put(current[0], Integer.parseInt(current[1]));
            }
            maps.add(audience);
            int numberOfArgument = Integer.parseInt(reader.readNext()[0]);
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

        return new Pair<>(AgentArguemnts,maps);
    }

    public void writeCsv(Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String,Integer>>> data){

    }
}
