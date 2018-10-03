/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.transcript;

import diet.message.MessageClientInterfaceEvent;
import diet.server.Conversation;
import diet.server.Participant;
import java.util.Hashtable;

/**
 *
 * @author gj
 */
public class Transcripts {
    
    Conversation c;
    Hashtable htTranscripts = new Hashtable();
    
    public Transcripts (Conversation c){
        this.c=c;
        
    }
    
    
    public void processClientInterfaceEvent(Participant p, MessageClientInterfaceEvent mcie){
        Transcript tt = (Transcript)htTranscripts.get(p);
        if(tt==null){
            tt = new Transcript(p,c);
            htTranscripts.put(p, tt);
        }
        tt.processClientEvent(mcie);
        
    }
    
}
