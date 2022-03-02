package org.multiAgent.IVAFramework;


import javafx.util.Pair;
import java.util.ArrayList;


public class IVAF{

    private ArrayList<Argument> arguments;

    private ArrayList<Audience> audiences;

    private ArrayList<Attack> relationship = new ArrayList<>();

    public IVAF(ArrayList<Argument> arguments, ArrayList<Audience> audiences){
        this.arguments = arguments;
        this.audiences = audiences;
        fitAudiences();
        setRelationship();
    }

    public void fitAudiences(){
        ArrayList<Argument> newArguments = new ArrayList<>();
        for(Audience aud: audiences){
            for(Argument arg: arguments){
                if(aud.getAction().equals(arg.getAct())){
                    newArguments.add(new Argument(arg.getAct(), arg.getGoal(), aud.getAudience(), aud.getSign(), aud.getVal()));
                }
            }
        }
        arguments = newArguments;
    }
    public void initializeIVAF(){
        setRelationship();
    }

    public void print(){
        for(Argument arg: arguments){
            System.out.println(arg.getAct() + " " + arg.getGoal() + " " + (arg.getSign().isPositive()? "+" : "-") + " " + arg.getVal());
        }
        for(Attack atc: relationship){
            Argument attacker = atc.getAttacker();
            Argument attacked = atc.getAttacked();
            System.out.println(attacker.getAct() + " "
                    + attacker.getAudience() + " "
                    + (attacker.getSign().isPositive() ? "+" : "-")
                    + " "  + attacker.getVal()
                    + " ----> "
                    + attacked.getAct() + " "
                    + attacked.getAudience() + " "
                    + (attacked.getSign().isPositive() ? "+" : "-") + " "
                    + attacked.getVal());
        }
        ArrayList<Argument> acceptableArguments = getPreferredExtension();
        for(Argument arg : acceptableArguments){
            System.out.println("{" + arg.getAct()
                    + " " + arg.getAudience()
                    + " " + arg.getSign()
                    + " " + arg.getVal()
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
                // if under the same goal
                if(i.getGoal().equals(e.getGoal())){
                    // a = a', s = - and s' = +
                    if(i.getAct().equals(e.getAct()) && i.getSign().isNegative() && e.getSign().isPositive()){
                        Attack attackRelationAB = new Attack(i, e);
                        relationship.add(attackRelationAB);
                    // a = a', v != v' and s = s' = +
                    }else if(i.getAct().equals(e.getAct()) && !(i.getVal() == e.getVal()) &&
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
        for (Attack atc: relationship){
            if (atc.attacker == a2 && atc.attacked == a1) {
                contains = true;
                break;
            }
        }
        if(contains){
            return a1.getVal() > a2.getVal();
        }else{
            return a1.getVal() >= a2.getVal();
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
