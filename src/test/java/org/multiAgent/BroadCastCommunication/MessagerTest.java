package org.multiAgent.BroadCastCommunication;

import org.junit.BeforeClass;
import org.junit.Test;
import org.multiAgent.Agent;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Sign;

public class MessagerTest {

    private static Messager messager = new Messager();
    static private Agent agent1 = new Agent(null,null,null);
    static private Agent agent2 = new Agent(null,null,null);
      @BeforeClass
      public static void initialize(){
        messager.messageLog.add(new Move(agent2,MoveType.OPEN, "topic"));
        messager.messageLog.add(new Move(agent1, MoveType.ASSERT, new Argument("restaurant","topic", "+", "C")));
        messager.messageLog.add(new Move(agent2, MoveType.ASSERT, new Argument("restaurant","topic", "-", "D")));
}

      @Test
      public void broadCast() {
      }

      @Test
      public void printLog() {
      }

      @Test
      public void addMessage() {
      }

      @Test
      public void getLastOne() {
      }

      @Test
      public void printLastOne() {
      }

      @Test
      public void checkMessageExistence() {
      }

      @Test
      public void testCheckMessageExistence() {
      }

      @Test
      public void checkClose() {
      }

      @Test
      public void checkAgreed() {
      }

      @Test
      public void checkDemote() {
        System.out.println(messager.checkDemote(agent1,0.7F).get(0)[2]);
      }
}