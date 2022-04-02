package org.multiAgent.Models;

import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.multiAgent.Agent;
import org.multiAgent.IVAFramework.Argument;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SocialWelfareModelTest {
    static Model socialWelfare = new SocialWelfareModel();
    @BeforeClass
    public static void ini(){


        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

        Argument argument7 = new Argument("restaurant", "go out", "-", "M");
        Argument argument8 = new Argument("picnic", "go out", "+", "D");
        Argument argument9 = new Argument("restaurant", "go out", "+", "V");

        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);

        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);

        HashMap<String, Integer> audience2 = new HashMap<>();
        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);

        ArrayList<Argument> arguments2 = new ArrayList<>();
        arguments2.add(argument5);
        arguments2.add(argument6);
        Agent agent1 = new Agent(audience1, arguments1, socialWelfare);
        Agent agent2 = new Agent(audience2, arguments2, socialWelfare);
        ArrayList<Agent> agentlist = new ArrayList<>();
        agentlist.add(agent1);
        agentlist.add(agent2);
        HashMap<Agent, HashMap<String, Integer>> map1 = new HashMap<>();
        map1.put(agent1, audience1);
        map1.put(agent2, audience2);
        socialWelfare.initialize(new Pair<>(agentlist,map1), agent1);
    }
    @Test
    public void initialize() {
    }

    @Test
    public void update() {
        socialWelfare.update(new HashMap());
        System.out.println(socialWelfare.getDistribution());
    }

    @Test
    public void getPossibility() {
    }

    @Test
    public void getDistribution() {
    }

    @Test
    public void demote() {
    }
}