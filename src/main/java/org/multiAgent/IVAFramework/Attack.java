package org.multiAgent.IVAFramework;

import javafx.util.Pair;

/**
 * Attack represent the relationship between two argument attacker and attacked within
 * an agent's iVAF
 */
public class Attack {

    Argument attacker;
    Argument attacked;

    /**
     * set relationship between two agents
     * @param attacker attacking agent
     * @param attacked agent attacked
     */
    public Attack(Argument attacker, Argument attacked){
        this.attacker = attacker;
        this.attacked = attacked;
    }

    /**
     * get agents involved in an attack relationship
     * @return two agents within the relationship
     */
    public Pair<Argument, Argument> getParticipants() {
        return new Pair(attacker, attacked);

    }

    /**
     * get attacker of the attack relationship
     * @return attacker
     */
    public Argument getAttacker(){
        return attacker;
    }

    /**
     * get the attacked agent within the relationship
     * @return attacked
     */
    public Argument getAttacked(){
        return attacked;
    }


}