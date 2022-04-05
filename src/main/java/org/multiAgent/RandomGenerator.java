package org.multiAgent;
import javafx.util.Pair;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Models.RandomModel;
import org.multiAgent.Models.SocialWelfareModel;

import java.util.*;

/**
 * This class provide a random argument and audience generator to support system evaluating
 */
public class RandomGenerator {
    Random rand = new Random();
    ArrayList<Argument> arguments = new ArrayList<>();

    HashSet<String> audiences = new HashSet<>();
    Pair<ArrayList<String>, ArrayList<Argument>> generated;

    int agentNum = 2;

    public RandomGenerator(){

    }

    /**
     * print the generated data
     */
    public void print(){
        for (Argument arg: arguments){
            System.out.println(arg);
        }
        for (String aud: audiences){
            System.out.println(aud);
        }
    }

    /**
     * generate argument according to limited number of agents, actions, values, arguments,
     * for two agents (2 <= Args <= 2 * value * action)
     * @param number number of agents
     * @param actionNumber number of actions
     * @param valueNumber number of values
     * @param argumentNumber number of arguments
     * @return A set of data which could be applied to initialize a dialogue
     */
    public Pair<Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>>, ArrayList<Model>>
    generate(int number, int actionNumber, int valueNumber , int argumentNumber){
        for(int i = 0; i < argumentNumber; i++){
            agentNum = number;
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
        return new Pair<>(new Pair<>(agentArguments, audience), randomModels(number));
    }


    public Pair<Pair<ArrayList<ArrayList<Argument>>, ArrayList<HashMap<String, Integer>>>, ArrayList<Model>>
    generate(int number, int actionNumber, int valueNumber , int argumentNumber, String model){

        for(int i = 0; i < argumentNumber; i++){
            agentNum = number;
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
        ArrayList<Model> models = new ArrayList<>();

        for (int i = 0; i < number; i++){
            switch (model){
                case "Nash Dynamic": models.add(new NashDynamicModel());break;
                case "Social Welfare": models.add(new SocialWelfareModel());break;
                case "Random": models.add(new RandomModel());break;
            }
        }

        return new Pair<>(new Pair<>(agentArguments, audience), models);
    }

    /**
     * generate actions according to limit
     * @param actionNumber action number
     * @return return a random action
     */
    public String generateAction(int actionNumber) {
        int num = 1 + (int) (Math.random() * actionNumber);
        return "ACTION" + num;
    }
    /**
     * generate sign between {+,-}
     * @return return a random sign
     */
    public String generateSign(){
        float num = rand.nextFloat(1);
        if(num >= 0.5){
            return "+";
        }else{
            return "-";
        }

    }

    /**
     * generate random audience according to limit
     * @param audienceNum audience limit
     * @return a random audience
     */
    public String generateAudience(int audienceNum){
        int num = 1 + (int) (Math.random() * audienceNum);
        return "AUDIENCE" + num;
    }

    /**
     * generate a random number within a limit (0 <= number <= limit)
     * @param upperbound limit
     * @return random number
     */
    public int generateRandomNumber(int upperbound){
        return 1 + (int) (Math.random() * upperbound);
    }

    public String getTopic(){
        return "topic";
    }
    public ArrayList<Model> randomModels(int agentNumber){

        ArrayList<Model> models = new ArrayList<>();
        for (int i = 0; i < agentNumber; i++){
            int num = 1 + (int) (Math.random() * 75);
            if (0 <= num && num < 25){
                models.add(new NashDynamicModel());
            }else if(25 <= num && num < 50){
                models.add(new RandomModel());
            }else{
                models.add(new SocialWelfareModel());
            }
        }
        return models;
    }

}
