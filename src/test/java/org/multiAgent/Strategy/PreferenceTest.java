package org.multiAgent.Strategy;

import org.junit.BeforeClass;
import org.junit.Test;
import org.multiAgent.Agent;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.DialogueSystem;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PreferenceTest {

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
    public void pickStrategy() {
        Agent agent1 = DialogueSystem.agents.get(0);
        Agent agent2 = DialogueSystem.agents.get(1);
        agent1.initializeByTopic("go out", dialogue.getDialogueInfo());
        DialogueSystem.messager.messageLog.clear();
        DialogueSystem.messager.messageLog.add(new Move(agent2, MoveType.OPEN, "go out"));
        DialogueSystem.messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("picnic","go out", "+", "C")));
        DialogueSystem.messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","go out", "-", "D")));
        assertEquals(agent1.Act(DialogueSystem.messager).getType(), MoveType.ASSERT);
        assertTrue(((Argument) agent1.Act(DialogueSystem.messager).getContent()).equals(new Argument("restaurant","go out","+","C")));
    }

    @Test
    public void selectBest() {
        Preference pre = new Preference();
        Agent agent1 = DialogueSystem.agents.get(0);
        Agent agent2 = DialogueSystem.agents.get(1);
        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(agent1,MoveType.ASSERT, new Argument("picnic", "go out", "+","C")));
        moves.add(new Move(agent1,MoveType.ASSERT, new Argument("picnic", "go out", "+","D")));
        moves.add(new Move(agent1,MoveType.ASSERT, new Argument("picnic", "go out", "+","V")));
        moves.add(new Move(agent1,MoveType.ASSERT, new Argument("picnic", "go out", "+","M")));
        HashMap<String,Float> model = new HashMap<>();
        model.put("C",0.25F);
        model.put("D",0.17F);
        model.put("V",0.16F);
        model.put("M",0.42F);
        assertEquals(((Argument)pre.selectBest(moves,model).getContent()).getAudience(),"M");
    }
}