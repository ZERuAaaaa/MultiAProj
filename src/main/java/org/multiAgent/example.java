package org.multiAgent;

import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.Models.Model;
import org.multiAgent.Models.NashDynamicModel;
import org.multiAgent.Models.RandomModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class example {

    public static void main(String[] args) {

        Argument argument1 = new Argument("restaurant", "go out", "+", "C");
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

        ArrayList<Argument> arguments3 = new ArrayList<>();
        arguments3.add(argument7);
        arguments3.add(argument8);
        arguments3.add(argument9);

        HashMap<String, Integer> audience3 = new HashMap<>();
        audience3.put("C", 1);
        audience3.put("D", 2);
        audience3.put("V", 4);
        audience3.put("M", 3);
        Model randModel = new RandomModel();
        Model nashModel = new NashDynamicModel();
        DialogueSystem dialogue = new DialogueSystem();
        dialogue.addAgent(audience1, arguments1, randModel);
        dialogue.addAgent(audience2, arguments2, randModel);
        //dialogue.addAgent(audience3, arguments3,randModel);
        dialogue.run("go out");
    }
}