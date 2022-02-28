package org.multiAgent.IVAFramework;

public class Audience {

    private String action;
    private String audienceLabel;
    private Sign sign;
    private Integer val;

    public Audience(String action, String audienceLebal, String sign, Integer val){
        this.action = action;
        this.val = val;
        this.audienceLabel = audienceLebal;
        switch (sign){
            case("-"): this.sign = Sign.NEGATIVE;
                break;
            case("+"): this.sign = Sign.POSITIVE;
                break;
        }
    }

    public String getAction(){
        return action;
    }

    public Sign getSign(){
        return sign;
    }

    public Integer getVal(){
        return val;
    }

    public String getAudience(){
        return audienceLabel;
    }
}
