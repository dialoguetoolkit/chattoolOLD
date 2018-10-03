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
public abstract class Move implements Serializable{
    
    boolean isCompleted = false;
    
    public String stringvalue ="";
    
    public Move (String sValue){
        this.stringvalue=sValue;
    }
    
    public String getSValue(){
        return this.stringvalue;
    }
    
    public boolean isCompleted(){
        return isCompleted;
    }
    abstract public void evaluate(Participant p, String s);
    
   // abstract public String getDescription();
    
    //abstract public String reset();
    
}
