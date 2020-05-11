/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.server.ConversationController;

import diet.attribval.AttribVal;
import diet.client.ClientInterfaceEvents.ClientInterfaceEvent;
import diet.message.MessageButtonPressFromClient;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageClientInterfaceEvent;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import diet.task.CustomizableReferentialTask.CustomizableReferentialTask;
import javax.swing.JButton;

/**
 *
 * @author LX1C
 */
public class Dyadic_TurnByTurn_Customizable_ReferentialTask extends DefaultConversationController{

    CustomizableReferentialTask crt = new CustomizableReferentialTask(this, 5000);
   // Participant pDirector;
   // Participant pMatcher;
    
    
    public Dyadic_TurnByTurn_Customizable_ReferentialTask(Conversation c) {
        super(c);
    }

    public Dyadic_TurnByTurn_Customizable_ReferentialTask(Conversation c, long istypingtimeout) {
        super(c, istypingtimeout);
    }

    
    
    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
        
        if(mct.getText().startsWith("/")){
            
                crt.processChatText(sender, mct.getText());
          
        
        }
        else{
            
             itnt.processTurnSentByClient(sender);
             c.relayTurnToPermittedParticipants(sender, mct);
        }
        
        
       
        
       
    }
    
    
    
    @Override
    public void participantJoinedConversation(Participant p) {
        super.participantJoinedConversation(p); 
        
       
        if(c.getParticipants().getAllParticipants().size()==2) {
            
             pp.createNewSubdialogue(c.getParticipants().getAllParticipants());
             //this.itnt.addGroupWhoAreMutuallyInformedOfTyping(c.getParticipants().getAllParticipants());
             this.itnt.addPairWhoAreMutuallyInformedOfTyping(c.getParticipants().getAllParticipants().elementAt(0), c.getParticipants().getAllParticipants().elementAt(1));
             
             
             CustomDialog.showDialog("PRESS OK TO START!");
             this.experimentHasStarted=true;
             
             crt.startTask((Participant)c.getParticipants().getAllOtherParticipants(p).elementAt(0), p);
             //c.changeJProgressBar(pDirector, "CHATFRAME", "text", Color.red, 50);
             //c.changeJProgressBar(pMatcher, "CHATFRAME", "text", Color.red, 50);
        }
        
        
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        super.participantRejoinedConversation(p); 
       
        
    }

    

    @Override
    public void processButtonPress(Participant sender, MessageButtonPressFromClient mbfc) {
        super.processButtonPress(sender, mbfc);  
        this.crt.processButtonPress(sender, mbfc.buttonname);
    }
    
    
    
    
    
    
   public static boolean showcCONGUI() {
        return true;
    }
    
}
