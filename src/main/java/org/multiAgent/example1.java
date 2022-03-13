package org.multiAgent;

import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.IVAF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class example1 {

    public static void main(String[] args) {

        Argument argument1 = new Argument("restaurant", "go out", "+", "C");
        Argument argument2 = new Argument("picnic", "go out", "+", "C");
        Argument argument3 = new Argument("picnic", "go out", "+", "M");
        Argument argument4 = new Argument("picnic", "go out", "+", "D");

        Argument argument5 = new Argument("restaurant", "go out", "-", "D");
        Argument argument6 = new Argument("picnic", "go out", "-", "V");
        ArrayList<Argument> arguments1 = new ArrayList<>();
        arguments1.add(argument1);
        arguments1.add(argument2);
        arguments1.add(argument3);
        arguments1.add(argument4);
        arguments1.add(argument5);
        arguments1.add(argument6);
        HashMap<String, Integer> audience1 = new HashMap<>();
        audience1.put("C", 4);
        audience1.put("D", 3);
        audience1.put("V", 2);
        audience1.put("M", 1);
        IVAF ivaf = new IVAF(arguments1, audience1);





    }


}