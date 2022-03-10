package org.multiAgent;
import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;

import java.util.*;

public class RandomGenerator {
    Random rand = new Random();
    ArrayList<Argument> arguments = new ArrayList<>();

    HashSet<String> audiences = new HashSet<>();
    Pair<ArrayList<String>, ArrayList<Argument>> generated;

    int agentNum = 0;
    public RandomGenerator(int number){
        agentNum = number;
    }

    public void print(){
        for (Argument arg: arguments){
            System.out.println(arg);
        }
        for (String aud: audiences){
            System.out.println(aud);
        }
    }
    public Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>> getByAgent(int actionNumber, int argumentNumber,int valueNumber){
        for(int i = 0; i < argumentNumber; i++){
            String newAction = generateAction(actionNumber);
            String newSign = generateSign();
            String newAudience = generateAudience(valueNumber);
            audiences.add(newAudience);
            Argument arg = new Argument(newAction, "topic", newSign, newAudience);
            arguments.add(arg);
        }
        ArrayList<HashMap<String, Integer>> audience = new ArrayList<>();
        ArrayList<ArrayList<Argument>> agentArguments = new ArrayList<>();
        int pointer = 0;
        for(int i = 0 ; i < agentNum ; i++){
            HashMap<String, Integer> map = new HashMap<>();
            for (String aud: audiences){
                map.put(aud, generateRandomNumber(valueNumber * 10));
            }
            ArrayList<Argument> list = new ArrayList<>();
            for (int e = 0; e < arguments.size() && e < arguments.size() / agentNum; e++){
                list.add(arguments.get(pointer));
                pointer++;
            }
            audience.add(map);
            agentArguments.add(list);
        }
        return new Pair<>(agentArguments, audience);
    }

    public String generateAction(int actionNumber) {
        int num = 1 + (int) (Math.random() * actionNumber);
        return "ACTION" + num;
    }

    public String generateSign(){
        float num = rand.nextFloat(1);
        if(num >= 0.5){
            return "+";
        }else{
            return "-";
        }

    }

    public String generateAudience(int audienceNum){
        int num = 1 + (int) (Math.random() * audienceNum);
        return "AUDIENCE" + num;
    }

    public int generateRandomNumber(int upperbound){
        int num = 1 + (int) (Math.random() * upperbound);
        return num;
    }


}
