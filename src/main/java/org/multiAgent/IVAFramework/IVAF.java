package org.multiAgent.IVAFramework;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class IVAF {
    private ArrayList<Argument> arguments;

    private HashMap<String, Integer> audiences;

    private ArrayList<Attack> relationship = new ArrayList<>();

    private ArrayList<Attack> defeatRelationship = new ArrayList<>();

    public ArrayList<Argument>[] labelings = new ArrayList[3];

    public ArrayList<ArrayList<Argument>> candidate_labellings = new ArrayList<>();

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
//        ArrayList<Argument> acceptableArguments = getPreferredExtension();
//        for(Argument arg : acceptableArguments){
//            System.out.println("{" + arg.getAct()
//                    + " " + arg.getAudience()
//                    + " " + arg.getSign()
//                    + " " + audiences.get(arg.getAudience())
//                    + "}");
//        }
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


    public void find_labellings(ArrayList<Argument>[] labeling){
        if (isCandidateSubset(labeling[0])){
            return;
        }
        if(getIllegalIN(labeling).isEmpty()){
            for (ArrayList<Argument> arg: candidate_labellings){
                if (isSubSet(arg, labeling[0])){
                    candidate_labellings.remove(arg);
                }
            }
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

    public ArrayList<Argument>[] transition_step(Argument x, ArrayList<Argument>[] labelin){
         ArrayList<Argument>[] labeling = labelin;
         moveArgument(labelin, x,0,1);
         ArrayList<Argument> OUT = getIllegalOUT(labeling);
         for (Argument arg: OUT){
             moveArgument(labeling,arg,1,2);
         }
         return labeling;
    }

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

    public void moveArgument(ArrayList<Argument>[] labeling ,Argument argument, int from, int to){
        labeling[from].remove(argument);
        labeling[to].add(argument);
    }

    public boolean isLabelingAdmissible(ArrayList<Argument>[] labeling){
        for(Argument arg: labeling[0]){
            if(!isLegallyIN(arg, labeling)){
                return false;
            }
        }
        for(Argument arg: labeling[1]){
            if(!isLegallyOUT(arg, labeling)){
                return false;
            }
        }
        return true;
    }

    public boolean isLabelingComplete(ArrayList<Argument>[] labeling){
        for(Argument arg: labelings[2]){
            if(!isLegallyUNDEC(arg, labeling)){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Argument> getSuperIllegalIN(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[0]){
            if (isSuperIllegallyIN(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

    public ArrayList<Argument> getIllegalIN(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[0]){
            if (!isLegallyIN(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

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

    public ArrayList<Argument> getIllegalOUT(ArrayList<Argument>[] labeling){
        ArrayList<Argument> out = new ArrayList<>();
        for (Argument arg: labeling[1]){
            if (!isLegallyOUT(arg, labeling)){
                out.add(arg);
            }
        }
        return out;
    }

    public boolean isSubSet(ArrayList<Argument> child, ArrayList<Argument> parent){
        for (Argument arg: child){
            if (!parent.contains(arg)){
                 return false;
            }
        }
        return true;
    }


    public boolean isCandidateSubset(ArrayList<Argument> child){
        for (ArrayList<Argument> e: candidate_labellings){
            if(isSubSet(child, e)){
                return true;
            }
        }
        return false;
    }


    private ArrayList<Argument> getAttackers(Argument a){
        ArrayList<Argument> attackers = new ArrayList<>();
        for(Attack attack: defeatRelationship){
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
