package org.multiAgent.IVAFramework;

import java.util.ArrayList;

/**
 * Implemented Argument constructed by agent
 * which has four tuple <a, p, v, s> which are < action, goal, value, sign>
 * and provide functions to get them
 */
public class Argument {

        private String action;
        private String goal;
        private String audience;
        private Double val;
        private Sign sign;

        public Argument(String action, String goal, String audience, String sign, Double val){
            this.action = action;
            this.goal = goal;
            this.audience = audience;
            switch (sign){
                case "-": this.sign = Sign.NEGATIVE;
                    break;
                case "+": this.sign = Sign.POSITIVE;
                    break;
            }
            this.val = val;
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

        public Double getVal(){
            return val;
        }

        public Sign getSign(){
            return sign;
        }

        public String getAudience(){
            return audience;
        }
        public void setVal(Double val){
            this.val = val;
        }

        public void setAudience(String audience){this.audience = audience;}

    }
