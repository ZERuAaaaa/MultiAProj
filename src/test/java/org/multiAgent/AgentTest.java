package org.multiAgent;

import javafx.collections.FXCollections;
import org.junit.BeforeClass;
import org.junit.Test;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.NashDynamicModel;
import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;


public class AgentTest {

    private static DialogueSystem dialogue;
    private static HashMap<String,Integer> audience1;
    @BeforeClass
    public static void initialize(){
        // create arguments to assigning to agents
        Argument argument1 = new Argument("restaurant", "go out", "+","C");
        Argument argument2 = new Argument("picnic", "no", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "no", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");

        // assigning audiences to agents
        audience1 = new HashMap<>();
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
        dialogue = new DialogueSystem();

        // initialize agents with specific models to the dialogue
        dialogue.addAgent(audience1, arguments1, new NashDynamicModel());
        dialogue.addAgent(audience2, arguments2, new NashDynamicModel());

    }
    @Test
    public void initializeByTopic() {
        Agent testAgent = DialogueSystem.agents.get(0);
        testAgent.initializeByTopic("go out", dialogue.getDialogueInfo());
        assertEquals(testAgent.getDvaf().getArguments().size(),2);
    }

    @Test
    public void getAgreeable() {
        Agent testAgent = DialogueSystem.agents.get(0);
        testAgent.initializeByTopic("go out", dialogue.getDialogueInfo());
        ArrayList<Argument> agreeableArgs = new ArrayList<Argument>();
        agreeableArgs.add(new Argument("restaurant", "go out", "+","C"));
        assertTrue(agreeableArgs.get(0).equals(testAgent.getAgreeable().get(0)));
    }

    @Test
    public void getDvaf(){
        Agent testAgent = DialogueSystem.agents.get(0);
        testAgent.initializeByTopic("go out", dialogue.getDialogueInfo());
        assertEquals(testAgent.getDvaf().getArguments().size(),2);
        assertEquals(testAgent.getDvaf().getAudience(),audience1);
    }

    @Test
    public void protocol() {
        Agent agent1 = DialogueSystem.agents.get(0);
        Agent agent2 = DialogueSystem.agents.get(1);
        DialogueSystem.messager.messageLog.clear();
        DialogueSystem.messager.messageLog.add(new Move(agent2, MoveType.OPEN, "go out"));
        DialogueSystem.messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","go out", "+", "C")));
        DialogueSystem.messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","go out", "-", "D")));
        Agent testAgent = DialogueSystem.agents.get(0);
        testAgent.initializeByTopic("go out", dialogue.getDialogueInfo());
        assertEquals(1,testAgent.protocol(DialogueSystem.messager)[0].size());
        assertEquals(0,testAgent.protocol(DialogueSystem.messager)[1].size());
        assertEquals(1,testAgent.protocol(DialogueSystem.messager)[2].size());
    }
}