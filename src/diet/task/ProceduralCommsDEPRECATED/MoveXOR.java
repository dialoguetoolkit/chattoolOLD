/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.ProceduralCommsDEPRECATED;

import diet.server.Participant;

/**
 *
 * @author gj
 */
public class MoveXOR extends Move {
    
    Participant p;

    public MoveXOR(Participant p, String sname) {
        super(sname);
        this.p = p;
    }
    
    
    
     public void evaluate(Participant p, String s){
        
    }
     
    public String getDescription(){
        return "not implemented";
    }
    
    
    
}
