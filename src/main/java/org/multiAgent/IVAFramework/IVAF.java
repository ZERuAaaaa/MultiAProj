package org.multiAgent.IVAFramework;


import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;


public class IVAF{

    private ArrayList<Argument> arguments;

    private HashMap<String, Integer> audiences;

    private ArrayList<Attack> relationship = new ArrayList<>();

    public IVAF(ArrayList<Argument> arguments, HashMap<String, Integer> audiences){
        this.arguments = arguments;
        this.audiences = audiences;
        setRelationship();
    }

    public void insertArgument(Argument argument){
        arguments.add(argument);
        relationship.clear();
        setRelationship();
    }

    public void initializeIVAF(){
        setRelationship();
    }

    public void print(){
        for(Argument arg: arguments){
            System.out.println(arg.getAct() + " " + arg.getAudience() +" " + arg.getGoal() + " " + (arg.getSign().isPositive()? "+" : "-") + " " + audiences.get(arg.getAudience()));
        }
        for(Attack atc: relationship){
            Argument attacker = atc.getAttacker();
            Argument attacked = atc.getAttacked();
            Integer AudienceA1 = audiences.get(attacker.getAudience());
            Integer AudienceA2 = audiences.get(attacked.getAudience());
            System.out.println(attacker.getAct() + " "
                    + attacker.getAudience() + " "
                    + (attacker.getSign().isPositive() ? "+" : "-")
                    + " "  + AudienceA1
                    + " ----> "
                    + attacked.getAct() + " "
                    + attacked.getAudience() + " "
                    + (attacked.getSign().isPositive() ? "+" : "-") + " "
                    + AudienceA2);
        }
        ArrayList<Argument> acceptableArguments = getPreferredExtension();
        for(Argument arg : acceptableArguments){
            System.out.println("{" + arg.getAct()
                    + " " + arg.getAudience()
                    + " " + arg.getSign()
                    + " " + audiences.get(arg.getAudience())
                    + "}");
        }
    }
    /**
     * generate relationship from existing arguments and audience after the audience are set
     */
    private void setRelationship(){
        for(int x = 0; x < arguments.size(); x++){
            for(int y = 0; y < arguments.size(); y++){
                if(x == y){
                    continue;
                }
                Argument i = arguments.get(x);
                Argument e = arguments.get(y);
                Integer audienceI = audiences.get(i.getAudience());
                Integer audienceE = audiences.get(e.getAudience());
                // if under the same goal
                if(i.getGoal().equals(e.getGoal())){
                    // a = a', s = - and s' = +
                    if(i.getAct().equals(e.getAct()) && i.getSign().isNegative() && e.getSign().isPositive()){
                        Attack attackRelationAB = new Attack(i, e);
                        relationship.add(attackRelationAB);
                    // a = a', v != v' and s = s' = +
                    }else if(i.getAct().equals(e.getAct()) && !(audienceI == audienceE) &&
                             i.getSign().isPositive() && e.getSign().isPositive()){
                        Attack attackRelationAB = new Attack(i, e);
                        relationship.add(attackRelationAB);
                    // a != a' and s = s' = +
                    }else if(!i.getAct().equals(e.getAct()) && i.getSign().isPositive() && e.getSign().isPositive()){
                        Attack attackRelationAB = new Attack(i, e);
                        relationship.add(attackRelationAB);
                    }
                }
            }
        }
    }

    /**
     * returns true if an argument a1 is preferred to argument a2
     * @param a1 first argument
     * @param a2 second argument
     */
    public boolean defeat(Argument a1, Argument a2){
        boolean contains = false;
        Integer audienceA1 = audiences.get(a1.getAudience());
        Integer audienceA2 = audiences.get(a2.getAudience());
        for (Attack atc: relationship){
            if (atc.attacker == a2 && atc.attacked == a1) {
                contains = true;
                break;
            }
        }
        if(contains){
            return audienceA1 > audienceA2;
        }else{
            return audienceA1 >= audienceA2;
        }
    }

    private ArrayList<Argument> getAttackers(Argument a){
        ArrayList<Argument> attackers = new ArrayList<>();
        for(Attack attack: relationship){
            if(attack.getAttacked() == a){
                attackers.add(attack.getAttacker());
            }
        }
        return attackers;
    }

    public boolean isAcceptable(Argument a){
        ArrayList<Argument> attackers = getAttackers(a);
        if (attackers.isEmpty()){
            return true;
        }
        for(Argument oneAttacker: attackers){
            ArrayList<Argument> againstAttackers = getAttackers(oneAttacker);
            for (Argument againstAttacker: againstAttackers){
                if(defeat(againstAttacker, oneAttacker)){
                    return true;
                }
            }
        }
        return false;

    }

    public Boolean isConflictFree(ArrayList<Argument> subset){
        for(int x = 0; x < subset.size(); x++){
            for(int y = 0; y < subset.size(); y++){
                Argument attacker = subset.get(x);
                Argument attacked = subset.get(y);
                for(Attack atc: relationship){
                    if(atc.attacker == attacker && atc.attacked == attacked){
                        if(x != y && defeat(attacker, attacked)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private Boolean isAdmissible(ArrayList<Argument> subset){
        if(isConflictFree(subset)){
            for(Argument a: subset){
                if(!isAcceptable(a)){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public ArrayList<Argument> getPreferredExtension(){
        ArrayList<ArrayList<Argument>> allSubset = getAllSubset(arguments);
        allSubset.removeIf(subset -> !isAdmissible(subset));
        if(allSubset.isEmpty()){
            return null;
        }
        ArrayList<Argument> preferredExtension = new ArrayList<>();
        for(ArrayList<Argument> admissibleSet: allSubset){
            if(admissibleSet.size() > preferredExtension.size()){
                preferredExtension = admissibleSet;
            }
        }

        return preferredExtension;
    }

    /**
     * use bit manipulation to get all the subset of arguments
     * @param arguments list of all arguments hold by this IVAF
     * @return subsets
     */
    private ArrayList<ArrayList<Argument>> getAllSubset(ArrayList<Argument> arguments){
        ArrayList<ArrayList<Argument>> subsets = new ArrayList<>();
        int size = 1 << arguments.size();
        for(int i = 0 ; i < size ; i++){
            ArrayList<Argument> subset = new ArrayList<>();
            for(int e = 0; e < arguments.size(); e++){
                if((i & (1 << e))!= 0){
                    subset.add(arguments.get(e));
                }
            }
            subsets.add(subset);
        }
        return subsets;
    }


    public void insert(Argument argument){
        arguments.add(argument);
        setRelationship();
    }
    /**
     * return IVAFTuple of an agent
     * @return <X, A>
     */
    public Pair<ArrayList<Argument>, ArrayList<Attack>> getIVAFTuple(){
        return new Pair(arguments, relationship);
    }


}
