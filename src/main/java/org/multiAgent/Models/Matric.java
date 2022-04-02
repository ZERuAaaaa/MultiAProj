package org.multiAgent.Models;

import java.util.HashMap;
import java.util.Map;

public class Matric {
    HashMap<String,HashMap<String, float[]>> matric;

    public Matric(HashMap<String,Float> self, HashMap<String,Float> other){
        matric = new HashMap<>();
        for (Map.Entry<String, Float> selfEntry: self.entrySet()){
            HashMap<String,float[]> in = new HashMap<>();
            for (Map.Entry<String,Float> otherEntry: other.entrySet()){
                float[] pair = new float[2];
                pair[0] = selfEntry.getValue();
                pair[1] = otherEntry.getValue();
                in.put(otherEntry.getKey(),pair);
            }
            matric.put(selfEntry.getKey(), in);
        }
    }

    public void print(){
        for (Map.Entry<String,HashMap<String, float[]>> entry: matric.entrySet()){
            System.out.print(entry.getKey());
            for (Map.Entry<String, float[]> in: entry.getValue().entrySet()){
                System.out.print(" " + in.getKey() + "=" + "{" + in.getValue()[0] + "," + in.getValue()[1] + "}\n");
            }
        }
    }

    public void demote(String self, String other, float strengh){
        matric.get(self).get(other)[0] = Math.max(matric.get(self).get(other)[0] - strengh, 0);
        matric.get(self).get(other)[1] = Math.max(matric.get(self).get(other)[1] - strengh, 0);
    }

    public void promote(String self){

    }

    public  HashMap<String,HashMap<String, float[]>> getAllX(){
        return matric;
    }

    public HashMap<String, float[]> getX(String X){
        return matric.get(X);
    }

    public HashMap<String, float[]> getY(String Y){
        HashMap<String, float[]> temp = new HashMap<>();
        for (Map.Entry<String, HashMap<String, float[]>> out: matric.entrySet()){
            for (Map.Entry<String, float[]> in: out.getValue().entrySet()){
                if (in.getKey().equals(Y)){
                    temp.put(out.getKey(), in.getValue());
                }
            }
        }
        return temp;
    }

    public HashMap<String,HashMap<String, float[]>> getMatric(){
        return matric;
    }
}
