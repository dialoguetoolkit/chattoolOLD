/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.server.ConversationController;

import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.JointReference.JointReferenceTaskController;
import java.awt.Color;
import java.util.Date;

/**
 *
 * @author LX1C
 */
public class Dyadic_TurnByTurn_GROUNDING extends DefaultConversationController{

    JointReferenceTaskController jrtc = new JointReferenceTaskController(this, 5000);
    Participant pDirector;
    Participant pMatcher;
    
    
    public Dyadic_TurnByTurn_GROUNDING(Conversation c) {
        super(c);
    }

    public Dyadic_TurnByTurn_GROUNDING(Conversation c, long istypingtimeout) {
        super(c, istypingtimeout);
    }

    
    
    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
        
        if(mct.getText().startsWith("/")){
            if(sender!=pDirector )jrtc.processChatText(sender, mct.getText());        
        
        }
        else{
            
             itnt.processTurnSentByClient(sender);
             c.newrelayTurnToPermittedParticipants(sender, mct);
        }
        
        
       
        
       
    }
    
    
    
    @Override
    public void participantJoinedConversation(Participant p) {
        super.participantJoinedConversation(p); 
        
        if(c.getParticipants().getAllParticipants().size()==1){
            this.pDirector=p;
        }
        if(c.getParticipants().getAllParticipants().size()==2) {
             this.pMatcher=p;
             pp.createNewSubdialogue(c.getParticipants().getAllParticipants());
             //this.itnt.addGroupWhoAreMutuallyInformedOfTyping(c.getParticipants().getAllParticipants());
             this.itnt.addPairWhoAreMutuallyInformedOfTyping(c.getParticipants().getAllParticipants().elementAt(0), c.getParticipants().getAllParticipants().elementAt(1));
             this.experimentHasStarted=true;
             
             jrtc.startTask(pDirector, pMatcher);
             //c.changeJProgressBar(pDirector, "CHATFRAME", "text", Color.red, 50);
             //c.changeJProgressBar(pMatcher, "CHATFRAME", "text", Color.red, 50);
        }
        
        
    }
    
    
   public static boolean showcCONGUI() {
        return true;
    }
    
}
