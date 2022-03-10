package org.multiAgent.Strategy;

import org.multiAgent.BroadCastCommunication.Messager;
import org.multiAgent.BroadCastCommunication.Move;
import org.multiAgent.BroadCastCommunication.MoveType;
import org.multiAgent.IVAFramework.Argument;
import org.multiAgent.IVAFramework.Sign;
import org.multiAgent.Models.NashDynamicModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * this strategy selects move according to the preference ordering
 * agree > proposing assert > attacking assert > close
 */
public class Preference {

    /**
     * default constructor
     */
    public Preference(){

    }

    /**
     * this function select a best move depend on the preference ordering
     * @param availableMoves protocal of an agent
     * @param agreeable all agreeable arguments
     * @param argumentsOfAgreeable all arguments under agreeable action
     * @param model a model to select the best move
     * @param messager the logger which keeping rocording the dialogue
     * @return  selected move
     */
    public Move pickStrategy(HashSet<Move>[] availableMoves, ArrayList<Argument> agreeable, ArrayList<Argument> argumentsOfAgreeable, NashDynamicModel model, Messager messager){
        HashMap<String, Float> Model = model.getDistribution();
        // available agree move
        ArrayList<Move> legalAgreeMove = new ArrayList<>();
        for (Move move : availableMoves[1]){
            for (Argument arg: agreeable){
                if((move.getContent()) == arg.getAct()){
                    legalAgreeMove.add(move);
                }
            }
        }
        // available prop assert move
        ArrayList<Move> legalPropAssertMove = new ArrayList<>();
        for (Move move : availableMoves[0]){
            Argument currentArgument = (Argument) move.getContent();
            if(argumentsOfAgreeable.contains(currentArgument) && currentArgument.getSign() == Sign.POSITIVE){
                    legalPropAssertMove.add(move);
            }

        }
        // available attack assert move
        ArrayList<Move> legalAttackAssertMove = new ArrayList<>();
        for (Move move : availableMoves[0]){
            Argument currentArgument = (Argument) move.getContent();
            if(!argumentsOfAgreeable.contains(currentArgument) && messager.checkMessageExistence(MoveType.ASSERT, currentArgument.getAct(), Sign.POSITIVE) && currentArgument.getSign() == Sign.NEGATIVE){
                legalAttackAssertMove.add(move);
            }
        }
        if(!legalAgreeMove.isEmpty()){
            return legalAgreeMove.get(0);
        }else if (!legalPropAssertMove.isEmpty()){
            return selectBest(legalPropAssertMove, Model);
        }else if (!legalAttackAssertMove.isEmpty()){
            return selectBest(legalAttackAssertMove, Model);
        }else{
            return (Move) availableMoves[2].toArray()[0];
        }
    }

    /**
     * pick a best move according to the model
     * @param moves available moves
     * @param Model model
     * @return selected move
     */
    public Move selectBest(ArrayList<Move> moves, HashMap<String, Float> Model){
        Float max = (float) Integer.MIN_VALUE;
        Move out = null;
        for (Move move: moves){
            String audience = ((Argument) move.getContent()).getAudience();
            if(Model.get(audience) > max){
                max = Model.get(audience);
                out = move;
            }
        }
        return out;
    }
}
