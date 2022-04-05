package org.multiAgent.Models;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

public class MatrixTest {
    static HashMap<String,Float> self = new HashMap();
    static HashMap<String,Float> other = new HashMap();
    @BeforeClass
    public static void initialize(){
        self.put("C", (float) 4);
        self.put("D", (float) 3);
        self.put("V", (float) 2);
        self.put("M", (float) 1);

        other.put("C", (float)1);
        other.put("D", (float)2);
        other.put("V", (float)3);
        other.put("M", (float)4);
    }

    Matrix matrix = new Matrix(self, other);


    @Test
    public void print() {
        matrix.print();
    }

    @Test
    public void demote(){
        matrix.demote("C", "D", 1);
        matrix.print();
    }

    @Test
    public void get(){
        for (Map.Entry<String, float[]> entry: matrix.getX("C").entrySet()){
            System.out.println(entry.getValue()[0] + " " + entry.getValue()[1]);
        }
        for (Map.Entry<String, float[]> entry: matrix.getY("D").entrySet()){
            System.out.println(entry.getValue()[0] + " " + entry.getValue()[1]);
        }
    }
}