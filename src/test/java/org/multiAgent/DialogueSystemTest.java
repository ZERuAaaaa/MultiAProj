package org.multiAgent;

import org.junit.Test;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.NashDynamicModel;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DialogueSystemTest {

    @Test(expected = java.lang.Exception.class)
    public void run() throws Exception {
        Agent.AgentCounter = 0;
        // create arguments to assigning to agents
        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

        // assigning audiences to agents
        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        HashMap<String, Integer> audience2 = new HashMap<>();
        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);

        // assigning arguments to agents
        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);

        ArrayList<Argument> arguments2 = new ArrayList<>();
        arguments2.add(argument5);
        arguments2.add(argument6);

        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        // initialize agents with specific models to the dialogue
        dialogue.addAgent(audience1, arguments1, new NashDynamicModel());
        dialogue.addAgent(audience2, arguments2, new NashDynamicModel());

        dialogue.run("go out");
        dialogue.reset();
    }

    @Test(expected = java.lang.Exception.class)
    public void run2() throws Exception {
        Agent.AgentCounter = 0;
        // create arguments to assigning to agents
        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

        // assigning audiences to agents
        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);
        HashMap<String, Integer> audience2 = new HashMap<>();
        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);

        // assigning arguments to agents
        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);

        ArrayList<Argument> arguments2 = new ArrayList<>();


        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        // initialize agents with specific models to the dialogue
        dialogue.addAgent(audience1, arguments1, new NashDynamicModel());
        dialogue.addAgent(audience2, arguments2, new NashDynamicModel());


        dialogue.run("go out");



    }

    @Test(expected = java.lang.Exception.class)
    public void run3() throws Exception {

        Agent.AgentCounter = 0;
        // create arguments to assigning to agents
        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        // assigning audiences to agents
        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);
        // assigning arguments to agents
        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);

        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        // initialize agents with specific models to the dialogue
        dialogue.addAgent(audience1, arguments1, new NashDynamicModel());

        dialogue.run("go out");

    }

    @Test
    public void runAndSave() throws Exception {
        Agent.AgentCounter = 0;
        // create arguments to assigning to agents
        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

        // assigning audiences to agents
        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);

        HashMap<String, Integer> audience2 = new HashMap<>();
        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);

        // assigning arguments to agents
        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);

        ArrayList<Argument> arguments2 = new ArrayList<>();
        arguments2.add(argument5);
        arguments2.add(argument6);

        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        // initialize agents with specific models to the dialogue
        dialogue.addAgent(audience1, arguments1, new NashDynamicModel());
        dialogue.addAgent(audience2, arguments2, new NashDynamicModel());
        // run the dialogue
        dialogue.runAndSave("go out", "src/test/java/org/multiAgent/testCsv/1.csv", "src/test/java/org/multiAgent/testCsv/2.csv");
    }

    @Test(expected = java.lang.Exception.class)
    public void run4() throws Exception {
        Agent.AgentCounter = 0;

        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        CsvTool tool = new CsvTool();
        dialogue.initialize(tool.loadForCsv("src/test/java/org/multiAgent/testCsv/1.csv"));
        // run the dialogue
        dialogue.runAndSave("go out", "src/test/java/org/multiAgent/testCsv/1.csv", "src/test/java/org/multiAgent/testCsv/2.csv");
    }

    @Test(expected = java.lang.Exception.class)
    public void run5() throws Exception {
        Agent.AgentCounter = 0;

        // create dialogue object
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.reset();
        CsvTool tool = new CsvTool();
        dialogue.initialize(tool.loadForCsv(null));
        // run the dialogue
        dialogue.runAndSave("go out", "src/test/java/org/multiAgent/testCsv/1.csv", "src/test/java/org/multiAgent/testCsv/2.csv");
    }



}