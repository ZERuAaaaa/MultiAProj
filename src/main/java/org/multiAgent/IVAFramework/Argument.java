package org.multiAgent.IVAFramework;

import java.util.ArrayList;

/**
 * Implemented Argument constructed by agent
 * which has four tuple <a, p, v, s> which are < action, goal, value, sign>
 * and provide functions to get them
 */
public class Argument {

        private boolean audienced = false;
        private String action;
        private String goal;
        private String audience;
        private Integer val;
        private Sign sign;

        public Argument(String action, String goal){
            this.action = action;
            this.goal = goal;
        }

        public Argument(String action, String goal, String audience, Sign sign, Integer val){
            this.action = action;
            this.goal = goal;
            this.audience = audience;
            this.sign = sign;
            this.val = val;
            this.audienced = true;
        }

        public String toString(){
            return "<"+ action + "," + goal + "," + audience + "," + sign + val + ">";
        }

        public String getAct(){
            return action;
        }

        public String getGoal(){
            return goal;
        }

        public Integer getVal(){
            return val;
        }

        public Sign getSign(){
            return sign;
        }

        public String getAudience(){
            return audience;
        }

        public void setVal(Integer val){
            this.val = val;
        }

        public void seted(){this.audienced = true;}

    }
