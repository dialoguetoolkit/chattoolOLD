/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.ProceduralCommsDEPRECATED;

import diet.server.Participant;
import java.io.Serializable;

/**
 *
 * @author gj
 */
public class MoveONLY extends Move implements Serializable{
    
    
    
    Participant p;
    transient ProceduralComms pc;
   
    
    
    public MoveONLY(ProceduralComms pc, Participant p, String s){
        super(s);
        this.p=p;
        this.pc=pc;
    }
    
     public void evaluate(Participant pMadeMove, String s){
         String evaluationText ="";
         if(p==pMadeMove){
            evaluationText = "CORRECT PARTICIPANT "+pMadeMove.getUsername();
            if(s.equals(super.stringvalue)){
                evaluationText = evaluationText+ " CORRECT TEXT";
                super.isCompleted=true;
            }
            else{
                evaluationText = evaluationText+ " INCORRECT TEXT: "+s;
            }
        }
         else{
             evaluationText = "INCORECT PARTICIPANT: "+pMadeMove.getUsername();
         }
         pc.displayEvaluationTextOnUI(evaluationText);
    }
     
    public String getDescription(){
        return "not implemented";
    }
    
}
