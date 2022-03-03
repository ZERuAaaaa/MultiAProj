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
        private Sign sign;

        public Argument(String action, String goal,String sign, String audience){
            switch (sign){
                case("-"): this.sign = Sign.NEGATIVE;
                    break;
                case("+"): this.sign = Sign.POSITIVE;
                    break;
            }
            this.action = action;
            this.goal = goal;
            this.audience = audience;
        }


        public String toString(){
            return "<"+ action + "," + goal + "," + sign + "," +  audience + ">";
        }

        public String getAct(){
            return action;
        }

        public String getGoal(){
            return goal;
        }


        public Sign getSign(){
            return sign;
        }

        public String getAudience(){
            return audience;
        }

        public void seted(){this.audienced = true;}

    }
