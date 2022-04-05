package org.multiAgent.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * This class construct payoff matrix for models to determine between values.
 */
public class Matrix {
    HashMap<String,HashMap<String, float[]>> matrix;

    /**
     * Constructor of the payoff matrix
     * @param self self audience
     * @param other other's audience
     */
    public Matrix(HashMap<String,Float> self, HashMap<String,Float> other){
        matrix = new HashMap<>();
        for (Map.Entry<String, Float> selfEntry: self.entrySet()){
            HashMap<String,float[]> in = new HashMap<>();
            for (Map.Entry<String,Float> otherEntry: other.entrySet()){
                float[] pair = new float[2];
                pair[0] = selfEntry.getValue();
                pair[1] = otherEntry.getValue();
                in.put(otherEntry.getKey(),pair);
            }
            matrix.put(selfEntry.getKey(), in);
        }
    }

    /**
     * print the matrix
     */
    public void print(){
        for (Map.Entry<String,HashMap<String, float[]>> entry: matrix.entrySet()){
            System.out.print(entry.getKey());
            for (Map.Entry<String, float[]> in: entry.getValue().entrySet()){
                System.out.print(" " + in.getKey() + "=" + "{" + in.getValue()[0] + "," + in.getValue()[1] + "}\n");
            }
        }
    }

    /**
     * perform a demoting to a value within the matrix
     * @param self demoted value of the agent itself
     * @param other value of other agent use to demote this agent's value
     * @param strength degree of demote
     */
    public void demote(String self, String other, float strength){
        matrix.get(self).get(other)[0] = Math.max(matrix.get(self).get(other)[0] - strength, 0);
        matrix.get(self).get(other)[1] = Math.max(matrix.get(self).get(other)[1] - strength, 0);
    }

    public  HashMap<String,HashMap<String, float[]>> getAllX(){
        return matrix;
    }

    /**
     * get values within the matric by row
     * @param X value
     * @return set of values
     */
    public HashMap<String, float[]> getX(String X){
        return matrix.get(X);
    }

    /**
     * get values within the matric by column
     * @param Y value
     * @return set of values
     */
    public HashMap<String, float[]> getY(String Y){
        HashMap<String, float[]> temp = new HashMap<>();
        for (Map.Entry<String, HashMap<String, float[]>> out: matrix.entrySet()){
            for (Map.Entry<String, float[]> in: out.getValue().entrySet()){
                if (in.getKey().equals(Y)){
                    temp.put(out.getKey(), in.getValue());
                }
            }
        }
        return temp;
    }

    /**
     *@return
     */
    public HashMap<String,HashMap<String, float[]>> getMatric(){
        return matrix;
    }


}
