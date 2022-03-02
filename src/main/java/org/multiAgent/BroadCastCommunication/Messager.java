package org.multiAgent.BroadCastCommunication;

import org.multiAgent.Agent;

import java.util.ArrayList;

public class Messager {

    ArrayList<Agent> participants = new ArrayList<>();
    ArrayList<Message> messageLog = new ArrayList<>();

    public Messager(){

    }

    public <T> void broadCast(Agent proposer, Move move, T content){

    }

    public void printLog(){
        for(int i = 0; i < messageLog.size(); i++){
            System.out.println(i + ":" + messageLog.get(i).toString());
        }
    }

    public void printLastOne(){
        System.out.println(messageLog.get(messageLog.size() - 1));
    }
}
