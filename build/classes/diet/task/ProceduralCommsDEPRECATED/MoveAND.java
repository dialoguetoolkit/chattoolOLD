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
public class MoveAND extends Move implements Serializable{

    public MoveAND(String s, Participant pA, Participant pB) {
       super(s);
    }
    
    
    
    
    public void evaluate(Participant p, String s){
       
    }
    
    public String getDescription(){
        return "not implemented";
    }
    
    
    
}
