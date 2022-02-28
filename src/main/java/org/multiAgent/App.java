package org.multiAgent;

import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App{

    public static void main(String[] args) {


        Agent agent1 = new Agent();
        agent1.addArgument("restaurant", "go out");
        agent1.addArgument("picnic", "go out");
        agent1.addAudience("restaurant", "C", "+", 4);
        agent1.addAudience("picnic", "M", "+", 1);
        agent1.addAudience("picnic", "D", "+", 3);
        agent1.addAudience("picnic", "C", "+", 4);

        agent1.initializeByTopic("go out");
        agent1.getDvaf().print();


        Agent agent2 = new Agent();
        agent2.addArgument("restaurant", "go out");
        agent2.addArgument("picnic", "go out");
        agent2.addAudience("restaurant", "D", "-", 2);
        agent2.addAudience("picnic", "V", "-", 3);

        agent2.initializeByTopic("go out");
        agent2.getDvaf().print();
    }
}
