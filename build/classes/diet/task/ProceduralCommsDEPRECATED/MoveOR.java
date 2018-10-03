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
public class MoveOR extends Move {
    
     Participant p;
    
    
    
    
    public MoveOR(Participant p,String s){
        super(s);
        this.p=p;
        
    }
    
    public void evaluate(Participant p, String s){
       
    }
    
    
    public String getDescription(){
        return "not implemented";
    }
}
