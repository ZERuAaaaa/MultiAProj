package org.multiAgent;

import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App{

    public static void main(String[] args) {

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
        Agent agent1 = new Agent(audience1, arguments1);
        agent1.initializeByTopic("go out");
        IVAF Dvaf1 = agent1.getDvaf();
        //Dvaf1.print();


        HashMap<String, Integer> audience2 = new HashMap<>();
        audience2.put("C", 1);
        audience2.put("D", 2);
        audience2.put("V", 3);
        audience2.put("M", 4);

        ArrayList<Argument> arguments2 = new ArrayList<>();
        arguments2.add(argument5);
        arguments2.add(argument6);


        Agent agent2 = new Agent(audience2, arguments2);

        DialogueSystem ds = new DialogueSystem();
        ds.setTopic("go out");
        agent2.initializeByTopic("go out");
        IVAF Dvaf2 = agent2.getDvaf();
        //Dvaf2.print();
        Messager messager = new Messager();
        messager.addMessage(new Move(agent2, MoveType.OPEN, "go out"));
        messager.addMessage(new Move(agent1, MoveType.ASSERT, argument1));
        messager.addMessage(new Move(agent2, MoveType.ASSERT, argument5));
        messager.addMessage(new Move(agent1, MoveType.ASSERT, argument4));
        messager.addMessage(new Move(agent2, MoveType.ASSERT, argument6));
        messager.addMessage(new Move(agent1, MoveType.ASSERT, argument3));
        messager.addMessage(new Move(agent2, MoveType.CLOSE, "go out"));
        messager.addMessage(new Move(agent1, MoveType.CLOSE, "go out"));
        messager.printLog();
        System.out.println(messager.checkClose());

    }
}
