/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.mazegame.MazeGameController2WAY;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomDutchSelfRepairInitiation;
import diet.textmanipulationmodules.Selfrepairgeneration.SelfRepair;
import diet.utils.HashtableOfLong;
import static java.awt.SystemColor.info;

/**
 *
 * @author gj
 */
public class DP2015_SELFREPAIR_BACKUP extends MazeGameConversationControllerMultipleDyads {

    CyclicRandomDutchSelfRepairInitiation crdsr = new CyclicRandomDutchSelfRepairInitiation();
    SelfRepair sr = new SelfRepair();
     HashtableOfLong htol = new HashtableOfLong(0);
    
    
    public DP2015_SELFREPAIR_BACKUP(Conversation c) {
        super(c);
        this.setID("DP2015_SELFREPAIR");
    }

    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
        /*super.processChatText(sender, mct); 
       
        MazeGameController2WAY mgccc = this.getMazeGameController(sender);
        Participant pRecipient = (Participant)this.pp.getRecipients(sender).elementAt(0);
        
        long mostRecentIntervention = htol.get(sender);
        long currentTurnNo = sender.getNumberOfChatMessagesProduced();
        if(currentTurnNo-mostRecentIntervention < 2){
                   c.relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(sender, pRecipient,0, mct, mazeNo, moveNo, indivMveNo,1,info);    
                   System.err.println("Not even trying to look for intervention - 5 turns haven't elapsed yet");
                   Conversation.printWSln("Main", "Not triggering delete because delete was in previous turn");
                   c.sendLabelDisplayToParticipantInOwnStatusWindow(pRecipient, " ", false);
                   mgccc.appendToUI(sender.getUsername()+": "+mct.getText());
                   sr.setNewTurn(sender);
                   return;
        }
         String[] modifiedTurn = sr.getFirstPart_SecondPart_SummaryWithErrorCheck(sender);
         if(modifiedTurn[0]==null||modifiedTurn[1]==null){
               c.relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(sender, pRecipient,0, mct, mazeNo, moveNo, indivMveNo,1,info);
               c.sendLabelDisplayToParticipantInOwnStatusWindow(pRecipient, " ", false);
               mgccc.appendToUI(sender.getUsername()+": "+mct.getText());
               sr.setNewTurn(sender);
               return;
               
           }
           else{
              String repairInitiation = crdsr.getNext(sender);
              String fullChangedTurn = modifiedTurn[0]+" "+repairInitiation+" "+ modifiedTurn[1];
              c.sendArtificialMazeGameTurnFromApparentOriginToRecipientWithEnforcedTextColour(sender.getUsername(), pRecipient, fullChangedTurn, 0, info, 1);
              c.sendLabelDisplayToParticipantInOwnStatusWindow(pRecipient, " ", false);
              mgccc.appendToUI("FAKEREPAIR: "+sender.getUsername()+": "+mct.getText());
              sr.setNewTurn(sender);
              this.htol.put(sender, sender.getNumberOfChatMessagesProduced());
              return;
                   
           }
                
                
                
        */        
    }
     
    
    
}
