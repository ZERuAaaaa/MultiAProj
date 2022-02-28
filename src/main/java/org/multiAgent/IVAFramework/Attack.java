package org.multiAgent.IVAFramework;

import javafx.util.Pair;

public class Attack {

    Argument attacker;
    Argument attacked;

    public Attack(Argument attacker, Argument attacked){
        this.attacker = attacker;
        this.attacked = attacked;
    }

    public Pair<Argument, Argument> getParticipants() {
        return new Pair(attacker, attacked);

    }

    public Argument getAttacker(){
        return attacker;
    }

    public Argument getAttacked(){
        return attacked;
    }


}