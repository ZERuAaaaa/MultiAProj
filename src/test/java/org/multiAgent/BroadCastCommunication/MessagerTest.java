package org.multiAgent.BroadCastCommunication;

import org.junit.BeforeClass;
import org.junit.Test;
import org.multiAgent.Agent;
import org.multiAgent.DialogueSystem;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Sign;
import org.multiAgent.Models.NashDynamicModel;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagerTest {

    private static Messager messager = new Messager();
    private static DialogueSystem dialogue;
    private static HashMap<String,Integer> audience1;
        @BeforeClass
        public static void initialize(){
            Agent.AgentCounter = 0;
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
            DialogueSystem.topic = "go out";

}
          @Test
          public void getLastOne() {
              Agent agent1 = DialogueSystem.agents.get(0);
              Agent agent2 = DialogueSystem.agents.get(1);
              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "go out"));
              messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
              messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
              assertTrue(((Argument)messager.getLastOne().getContent()).equals(new Argument("restaurant", "topic", "-", "D")));
          }

          @Test
          public void checkMessageExistence() {
              Agent agent1 = DialogueSystem.agents.get(0);
              Agent agent2 = DialogueSystem.agents.get(1);
              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "go out"));
              messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
              messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
              assertTrue(messager.checkMessageExistence(MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
              assertFalse(messager.checkMessageExistence(MoveType.ASSERT, new Argument("1111111","topic", "+", "C")));
          }

          @Test
          public void checkClose() {
              Agent agent1 = DialogueSystem.agents.get(0);
              Agent agent2 = DialogueSystem.agents.get(1);
              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "go out"));
              messager.messageLog.add(new Move(agent1, MoveType.CLOSE, "go out"));
              messager.messageLog.add(new Move(agent2, MoveType.CLOSE, "go out"));
              assertEquals(java.util.Optional.ofNullable(messager.checkClose()),java.util.Optional.ofNullable(-1));


              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "go out"));
              messager.messageLog.add(new Move(agent1, MoveType.AGREE, "go out"));
              messager.messageLog.add(new Move(agent2, MoveType.AGREE, "go out"));
              assertEquals(java.util.Optional.ofNullable(messager.checkClose()),java.util.Optional.ofNullable(1));

              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "go out"));
              messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
              messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
              assertEquals(java.util.Optional.ofNullable(messager.checkClose()),java.util.Optional.ofNullable(0));
          }

          @Test
          public void checkAgreed() {
              Agent agent1 = DialogueSystem.agents.get(0);
              Agent agent2 = DialogueSystem.agents.get(1);
              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "topic"));
              messager.messageLog.add(new Move(agent1, MoveType.AGREE, "restaurant"));
              messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
              assertFalse(messager.checkAgreed("restaurant",agent1));

              messager.messageLog.clear();
              messager.messageLog.add(new Move(agent2,MoveType.OPEN, "topic"));
              messager.messageLog.add(new Move(agent1, MoveType.AGREE, "what"));
              messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "+", "D")));
              assertTrue(messager.checkAgreed("restaurant",agent1));


          }

          @Test
          public void checkDemote() {
                Agent agent1 = DialogueSystem.agents.get(0);
                Agent agent2 = DialogueSystem.agents.get(1);
                messager.messageLog.clear();
                messager.messageLog.add(new Move(agent2,MoveType.OPEN, "topic"));
                messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
                messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
                assertNotNull(messager.checkDemote(agent1,1));
          }
}