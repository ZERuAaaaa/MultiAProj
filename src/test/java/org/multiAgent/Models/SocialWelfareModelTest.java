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
    static Agent agent1;
    static Agent agent2;
    static ArrayList<Agent> agentlist;
    static HashMap<Agent, HashMap<String, Integer>> map1;
    static HashMap<Agent, HashMap<String, Float>> map2;
    @BeforeClass
    public static void ini(){


        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

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
        agent1 = new Agent(audience1, arguments1, socialWelfare);
        agent2 = new Agent(audience2, arguments2, new SocialWelfareModel());
        agentlist = new ArrayList<>();
        agentlist.add(agent1);
        agentlist.add(agent2);

        map1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);


        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);
        map1.put(agent1, audience1);
        map1.put(agent2, audience2);

        map2 = new HashMap<>();
        HashMap<String, Float> audience2F = new HashMap<>();
        HashMap<String, Float> audience1F = new HashMap<>();
        audience1F.put("C", 4F);
        audience1F.put("D", 3F);
        audience1F.put("V", 2F);
        audience1F.put("M", 1F);


        audience2F.put("C", 1F);
        audience2F.put("D", 2F);
        audience2F.put("V", 3F);
        audience2F.put("M", 4F);
        map2.put(agent1, audience1F);
        map2.put(agent2, audience2F);

    }
    @Test
    public void initialize() {
        socialWelfare.initialize(new Pair<>(agentlist,map1), agent1);
        assertEquals(socialWelfare.getMatrix().getMatric().size(), 4);
    }

    @Test
    public void update() {
        socialWelfare.initialize(new Pair<>(agentlist,map1), agent1);
        socialWelfare.update(map2);
        HashMap<String,Float> map = new HashMap<>();
        map.put("C",0.325F);
        map.put("D",0.275F);
        map.put("V",0.225F);
        map.put("M",0.175F);
        assertEquals(map,socialWelfare.getDistribution());
    }


}