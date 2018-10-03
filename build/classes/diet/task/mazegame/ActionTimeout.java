/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.mazegame;

import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class ActionTimeout extends Thread{
    
    
    Hashtable htTimeOfLastTypingByParticipant = new Hashtable();
    Hashtable htTimeOfLastALLCLEARFROMSERVER = new Hashtable();
    
    boolean doAction=true;
    
    Vector participants = new Vector();
    
    long durationOfIsTypingMessage= 3000;
    
    DefaultConversationController cC;
    
    public ActionTimeout(DefaultConversationController cC){
        this.cC=cC;
        this.start();
    }
    
    
    
    public void run(){
        while(doAction){
             
            Vector participantsLC = (Vector)this.getParticipants().clone();
            for(int i=0;i<participantsLC.size();i++){
                Participant p = (Participant)participantsLC.elementAt(i);
                this.updateOthersOfAllClear(p);
                
            }
            try{
                  System.err.println("SLEEPINGSLEEPING");
                  Thread.sleep(durationOfIsTypingMessage/2);
                } catch (Exception e){
                    e.printStackTrace();
            }  
             
             
             
        }
    }
   
    private synchronized void updateOthersOfAllClear(Participant p){
        try{
                   
                    long timeOfLastALLCLEAR = -1;
                    Object o  = this.htTimeOfLastALLCLEARFROMSERVER.get(p);
                    if(o ==null){
                        timeOfLastALLCLEAR = new Date().getTime();
                    }
                    else{
                        timeOfLastALLCLEAR = (Long)o;
                    }
                    long timeDifference = new Date().getTime()-timeOfLastALLCLEAR;
                    System.err.println("THE TIME DIFFERENCE IS: "+timeDifference);
                    if(timeDifference>durationOfIsTypingMessage){
                         this.notifyParticipantOfAllClear(p);
                    }
                    
                }catch(Exception e){
                    e.printStackTrace();
                    System.err.println("ERROR IN ACTION TIMEOUT REALLY SHOULDN'T HAPPEN");
                }
    }
    
    private synchronized Vector getParticipants(){
        return this.participants;
    }
    
    public synchronized void notifyParticipantOfAllClear(Participant p){
        htTimeOfLastALLCLEARFROMSERVER.put(p, new Date().getTime());
        cC.getC().deprecated_sendArtificialTurnToRecipient(p, "ALL CLEAR", 0);
        //Code to sed notification message here..
    }
    
    public synchronized void updateParticipantHasTyped(Participant p){
        boolean found=false;
        for(int i=0;i<participants.size();i++){
            Participant plookup = (Participant)participants.elementAt(i);
            if(plookup==p)found=true;
        }
        if(!found){
            participants.add(p);
        }
        this.htTimeOfLastTypingByParticipant.put(p, new Date().getTime());
       
        
        
    }
    
    
    
   
    
    
    
}
