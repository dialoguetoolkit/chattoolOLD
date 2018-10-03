/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ParticipantPartnering;

import diet.server.Participant;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class SubDialogue {
     
    ///For the purposes of experimental design it should be impossible for one participant to be in two subdialogues simultaneously!
    
    
     String id ="";
     Vector vps;
     
     public SubDialogue(String id, Vector vps){
         this.id =id;
         this.vps=vps;
     }
     
     
     
    
     public Vector getParticipants(){
         return vps;
     }
     public String getID(){
         return this.id;
     }
     
     public Vector<Participant> getAllOtherParticipants(Participant p){
         Vector v = new Vector();
         for(int i=0;i<vps.size();i++){
             Participant vpsp = (Participant)vps.elementAt(i);
             if(vpsp!=p)v.add(vpsp);
         }
         return v;
     }
     
    
     
     
}
