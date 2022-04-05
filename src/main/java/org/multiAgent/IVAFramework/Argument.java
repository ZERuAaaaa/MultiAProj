package org.multiAgent.IVAFramework;

/**
 * Implemented Argument constructed by agent
 * which has four tuple <a, p, v, s> which are < action, goal, value, sign>
 * and provide functions to get them
 */
public class Argument {

    private String action;
    private String goal;
    private String audience;
    private Sign sign;

    /**
     * Constructor of argument
     * @param action action
     * @param goal goal
     * @param sign sign
     * @param audience audience
     */
    public Argument(String action, String goal,String sign, String audience){
        switch (sign) {
            case ("-") -> this.sign = Sign.NEGATIVE;
            case ("+") -> this.sign = Sign.POSITIVE;
        }
        this.action = action;
        this.goal = goal;
        this.audience = audience;
    }

    /**
     * return the string format of an argument
     * @return string
     */
    public String toString(){
        return "<"+ action + "," + goal + "," + sign + "," +  audience + ">";
    }

    /**
     * return action of the argument
     * @return action
     */
    public String getAct(){
        return action;
    }

    /**
     * return goal of the argument
     * @return goal
     */
    public String getGoal(){
        return goal;
    }

    /**
     * return sign of the argument
     * @return sign
     */
    public Sign getSign(){
        return sign;
    }

    /**
     * return value of the argument
     * @return value
     */
    public String getAudience(){
        return audience;
    }

}
