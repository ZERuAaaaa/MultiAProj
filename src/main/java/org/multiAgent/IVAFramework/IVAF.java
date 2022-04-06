package org.multiAgent.IVAFramework;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stand for instantiated value-based argumentation framework,
 * an iVAF is used to determine the conflict between arguments of
 * an agent.
 */
public class IVAF {

    private ArrayList<Argument> arguments;
    private HashMap<String, Integer> audiences;

    private ArrayList<Attack> relationship = new ArrayList<>();
    private ArrayList<Attack> defeatRelationship = new ArrayList<>();

    public ArrayList<Argument>[] labelings = new ArrayList[3];
    public ArrayList<ArrayList<Argument>> candidate_labellings = new ArrayList<>();

    /**
     * Constructor of ivaf
     * @param arguments arguments
     * @param audiences audiences
     */
    public IVAF(ArrayList<Argument> arguments, HashMap<String, Integer> audiences){
        for (int i = 0; i < labelings.length; i++){
            labelings[i] = new ArrayList<>();
        }
        for (Argument arg: arguments){
            labelings[0].add(arg);
        }

        this.arguments = arguments;
        this.audiences = audiences;
        setRelationship();
    }

    /**
     * use to assert new argument to the agent's ivaf
     * @param argument new argument
     */
    public void insertArgument(Argument argument){
        arguments.add(argument);
        relationship.clear();
        setRelationship();
    }
    /**
     * print the ivaf of an agent
     */
    public void print(){
        for(Argument arg: arguments){
            System.out.println(arg.getAct() + " " + arg.getAudience() +" " + arg.getGoal() + " " +
                    (arg.getSign().isPositive()? "+" : "-") + " " + audiences.get(arg.getAudience()));
        }
        for(Attack atc: defeatRelationship){
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
        defeatRelationship.clear();
        for (Attack atc: relationship){
            Argument attacker = atc.getAttacker();
            Argument attacked = atc.getAttacked();
            if (defeat(attacker, attacked)){
                defeatRelationship.add(atc);
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

    /**
     * recursively find a preferred extension of the agent's iVAF
     * @param labeling labelling of arguments
     */
    public void find_labellings(ArrayList<Argument>[] labeling){
        if (isCandidateSubset(labeling[0])){
            return;
        }
        if(getIllegalIN(labeling).isEmpty()){
            candidate_labellings.removeIf(arg -> isSubSet(arg, labeling[0]));
            candidate_labellings.add(labeling[0]);
            return;
        }else{
            ArrayList<Argument> illegal = getSuperIllegalIN(labeling);
            if (!illegal.isEmpty()){
                find_labellings(transition_step(illegal.get(0),labeling));
            }else{
                illegal = getIllegalIN(labeling);
                for (Argument arg: illegal){
                    find_labellings(transition_step(arg,labeling));
                }
            }
        }
    }

    /**
     * return the preferred extension from agent's ivaf determined by labelling approach
     * @return preferred extension
     */
    public ArrayList<Argument> getPreferredExtension(){
        for (ArrayList<Argument> list: labelings){
            list.clear();
        }
        for (Argument arg: arguments){
            labelings[0].add(arg);
        }
        candidate_labellings.clear();
        find_labellings(labelings);
        return candidate_labellings.get(0);
    }

    /**
     * perform a transition step of labeling approach
     * @param x a specific argument
     * @param labelin current labeling
     * @return new labelling
     */
    public ArrayList<Argument>[] transition_step(Argument x, ArrayList<Argument>[] labelin){
         ArrayList<Argument>[] labeling = labelin;
         moveArgument(labelin, x,0,1);
         ArrayList<Argument> OUT = getIllegalOUT(labeling);
         for (Argument arg: OUT){
             moveArgument(labeling,arg,1,2);
         }
         return labeling;
    }

    /**
     * check whether an argument is legally in
     * @param argument a specific argument
     * @param labeling current labeling
     * @return boolean
     */
    public boolean isLegallyIN(Argument argument, ArrayList<Argument>[] labeling){
        if(labeling[0].contains(argument)){
            ArrayList<Argument> attackers = getAttackers(argument);
            for (Argument atc: attackers){
                if (!labeling[1].contains(atc)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * check whether an argument is super legally in
     * @param argument a specific argument
     * @param labeling current labeling
     * @return boolean
     */
    public boolean isSuperIllegallyIN(Argument argument, ArrayList<Argument>[] labeling){
        if (labeling[0].contains(argument)){
            ArrayList<Argument> attackers = getAttackers(argument);
            for (Argument atc: attackers){
                if(labeling[0].contains(atc) || labeling[2].contains(atc)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check whether an argument is super legally out
     * @param argument a specific argument
     * @param labeling current labeling
     * @return boolean
     */
    public boolean isLegallyOUT(Argument argument, ArrayList<Argument>[] labeling){
        if (labeling[1].contains(argument)){
            ArrayList<Argument> attackers = getAttackers(argument);
            for(Argument atc: attackers){
                if (labeling[0].contains(atc)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check whether an argument is legally undec
     * @param argument a specific argument
     * @param labeling current labeling
     * @return boolean
     */
    public boolean isLegallyUNDEC(Argument argument, ArrayList<Argument>[] labeling){
        if (labeling[2].contains(argument)){
            ArrayList<Argument> attackers = getAttackers(argument);
            boolean notEvery = false;
            boolean noAttackingIN = true;
            for (Argument atc: attackers){
                if(!labeling[1].contains(atc)){
                    notEvery = true;
                }
                if(labeling[0].contains(atc)){
                    noAttackingIN = false;
                }
            }
            return notEvery & noAttackingIN;
        }
        return false;
    }

    /**
     * move argument between different labellings
     * @param labeling current labeling
     * @param argument a specific argument
     * @param from labelling
     * @param to target labelling
     */
    public void moveArgument(ArrayList<Argument>[] labeling ,Argument argument, int from, int to){
        labeling[from].remove(argument);
        labeling[to].add(argument);
    }

    /**
     * get all arguments labelled legal super illegal in
     * @param labeling labeling
     * @return list of arguments
     */
    public ArrayList<Argument> getSuperIllegalIN(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[0]){
            if (isSuperIllegallyIN(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

    /**
     * get all arguments labelled legal in
     * @param labeling current labeling
     * @return list of arguments
     */
    public ArrayList<Argument> getIllegalIN(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[0]){
            if (!isLegallyIN(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

    /**
     * get all arguments labelled legal INP
     * @param labeling current labeling
     * @return list of arguments
     */
    public ArrayList<Argument> getIllegalINP(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = getSuperIllegalIN(labeling);
        ArrayList<Argument> temp = getIllegalIN(labeling);
        for (Argument arg: temp){
            if(!out.contains(arg)){
                out.add(arg);
            }
        }
        return out;
    }

    /**
     * get all arguments labelled legally out
     * @param labeling current labeling
     * @return list of arguments
     */
    public ArrayList<Argument> getIllegalOUT(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[1]){
            if (!isLegallyOUT(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

    /**
     * check whether list A is subset of list B
     * @param child child list
     * @param parent parent list
     * @return boolean
     */
    public boolean isSubSet(ArrayList<Argument> child, ArrayList<Argument> parent){
        for (Argument arg: child){
            if (!parent.contains(arg)){
                 return false;
            }
        }
        return true;
    }

    /**
     * check whether a set of argument is a candidate subset
     * @param child set of arugments
     * @return boolean
     */
    public boolean isCandidateSubset(ArrayList<Argument> child){
        for (ArrayList<Argument> e: candidate_labellings){
            if(isSubSet(child, e)){
                return true;
            }
        }
        return false;
    }

    /**
     * get all arguments that attacking argument a
     * @param a argument
     * @return set of arguments
     */
    private ArrayList<Argument> getAttackers(Argument a){
        ArrayList<Argument> attackers = new ArrayList<>();
        for(Attack attack: defeatRelationship){
            if(attack.getAttacked() == a){
                attackers.add(attack.getAttacker());
            }
        }
        return attackers;
    }

    /**
     * return IVAFTuple of an agent
     * @return <X, A>
     */
    public Pair<ArrayList<Argument>, ArrayList<Attack>> getIVAFTuple(){
        return new Pair(arguments, relationship);
    }

    public ArrayList<Argument> getArguments(){
        return arguments;
    }

    public HashMap<String,Integer> getAudience() {
        return audiences;
    }
}
