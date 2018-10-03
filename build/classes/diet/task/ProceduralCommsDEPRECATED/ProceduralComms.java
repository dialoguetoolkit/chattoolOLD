/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.ProceduralCommsDEPRECATED;

import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import diet.utils.StringOperations;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class ProceduralComms {

    Random r = new Random();
    
    Vector sequenceOfMoves = new Vector();
   
    Participant pA;
    Participant pB;
    
    
    DefaultConversationController cC;
    
    int rowsGUI =4;
    int columnsGUI =6;
    
    int gridwidth = 180;
    int gridheight = 80;
    
    JProceduralComms2 jpc;
    
   int serverGUIMONOSPACECHARS=20;
    
    public ProceduralComms(DefaultConversationController cC, Participant pA, Participant pB) {
        this.cC=cC;
        this.pA=pA;
        this.pB=pB;
        jpc = new JProceduralComms2(this);
        
    }
    
    public void startExperiment(){
        createRandomSequence(5);
        initializeInterfaces();
        this.sendSequencesToParticipants();
        this.displayOnUI();
    }
   
    private void createRandomSequence(int length){
       sequenceOfMoves.removeAllElements();
       for(int i=0;i<length;i++){
            MoveONLY m0= new MoveONLY(this,pA,"MOVE"+i); 
            sequenceOfMoves.addElement(m0);
       } 
    
    }
    
    private void initializeInterfaces(){
         ///cC.getC().gridTextStimuli_Initialize(pA, rowsGUI,columnsGUI, new Vector(), gridwidth,gridheight, new Date().getTime(), "TEXT2");
    }
    
    
   
    
    
    
    public void processMove(Participant p, String selection){
        Move m = this.getFirstUncompletedMove();
        if(m!=null)m.evaluate(p, selection);
        
        
        if(sequenceIsCompleted()){
           // nextSequence();
        }
        this.displayOnUI();
         
    }
    
    
    public void nextSequence(){
        int length = r.nextInt(25);
        createRandomSequence(length);
    }
    
    public void sendSequencesToParticipants(){
         Vector movesForA = new Vector();
         Vector movesForB = new Vector();
         for(int i=0;i<this.sequenceOfMoves.size();i++){
             Move m = (Move)this.sequenceOfMoves.elementAt(i);
             if(m instanceof MoveXOR) {
                 if (((MoveXOR)m).p == pA) movesForA.addElement(m.getSValue());
                 if (((MoveXOR)m).p == pB) movesForB.addElement(m.getSValue());
             } 
             else if(m instanceof MoveOR) {
                 if (((MoveOR)m).p == pA) movesForA.addElement(m.getSValue());
                 if (((MoveOR)m).p == pB) movesForB.addElement(m.getSValue());
             } 
              else if(m instanceof MoveONLY) {
                 if (((MoveONLY)m).p == pA) movesForA.addElement(m.getSValue());
                 if (((MoveONLY)m).p == pB) movesForB.addElement(m.getSValue());
             } 
             
         }
        
        
         for(int i=0;i<movesForA.size();i++){
             
         }
        
        //cC.getC().gridTextStimuli_changeTexts(pA, movesForA, new Date().getTime(), "TEXT3"); 
        //cC.getC().gridTextStimuli_changeTexts(pB, movesForB, new Date().getTime(), "TEXT3"); 
    }
    
    
    public void padWordlist(Vector v, int size){
        if(v.size()>size)cC.getC().printWlnLog("Main", "ERROR - size of padding is larger than vector");
    }
    
    
    public void signalError(){
        cC.getC().deprecated_sendArtificialTurnToRecipient(pA, "ERROR IN SELECTING", 0);
        cC.getC().deprecated_sendArtificialTurnToRecipient(pB, "ERROR IN SELECTING", 0);
    }
    
    
    
    public boolean sequenceIsCompleted(){
         for(int i=0;i<this.sequenceOfMoves.size();i++){
              Move m = (Move)this.sequenceOfMoves.elementAt(0);
              if(!m.isCompleted)return false;
         }
         return true;
    }
    
    public Move getFirstUncompletedMove(){
        Move m0 = (Move)this.sequenceOfMoves.elementAt(0);
        if(!m0.isCompleted)return m0;
        for(int i=0;i<this.sequenceOfMoves.size()-1;i++){
            Move m = (Move)this.sequenceOfMoves.elementAt(i);
            Move m2 = (Move)this.sequenceOfMoves.elementAt(i+1);
            if(m.isCompleted & !m2.isCompleted)return m2;
         
        }
        return null;
    } 
    
    
    
    public static void main(String[] args){
        
    }
    
    
    public void displayOnUI(){
        String description = this.getDescription();
        this.jpc.displayMOVES(description);
    }
 
    
    public void displayEvaluationTextOnUI(String s){
        this.jpc.appendEvaluationText(s);
    }
    
    
    
    public String getDescription(){
           
        String description = "";
        
        description = description + StringOperations.appWS_ToRight(description+"pA: "+pA.getUsername(), serverGUIMONOSPACECHARS);
        description = description + "     " + "pB: "+pB.getUsername()+"\n";
       
       
        
        int highestSuccessIndex=0;
        for(int i=0;i<this.sequenceOfMoves.size();i++){
            Move m = (Move)this.sequenceOfMoves.elementAt(i);
            if(m.isCompleted)highestSuccessIndex = i;
            if(m instanceof MoveONLY){
                MoveONLY mO = (MoveONLY)m;
                String successString ="(   )";
                if(m.isCompleted)successString = "( X )";
                if(mO.p == pA) {
                    description = description + StringOperations.appWS_ToRight(mO.getSValue(), serverGUIMONOSPACECHARS) + successString+"\n";
                }
                else if (mO.p==pB){
                    description = description + StringOperations.appWS_ToRight(" ", serverGUIMONOSPACECHARS) + successString + mO.getSValue()+"\n";
                }     
            }
            
            
            
            
           // description = description + m.getDescription() +"\n";
        }
        description = description + "\nCompleted: ("+highestSuccessIndex+"/"+sequenceOfMoves.size()+")\n";
        
        
        return description;
    }
    
    
}
