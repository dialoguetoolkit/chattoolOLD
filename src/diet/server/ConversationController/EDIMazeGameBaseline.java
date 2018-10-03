/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.message.MessageChatTextFromClient;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.mazegame.MazeGameController2WAY;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomDutchSelfRepairInitiation;
import diet.textmanipulationmodules.Selfrepairgeneration.SelfRepair;
import diet.utils.HashtableOfLong;
import static java.awt.SystemColor.info;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class EDIMazeGameBaseline extends MazeGameConversationControllerMultipleDyads {

    //CyclicRandomDutchSelfRepairInitiation crdsr = new CyclicRandomDutchSelfRepairInitiation();
    //SelfRepair sr = new SelfRepair();
    // HashtableOfLong htol = new HashtableOfLong(0);
    
    
    public EDIMazeGameBaseline(Conversation c) {
        super(c);
        this.setID( "DP2015_BASELINE");
    }

    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
      
        if(!this.experimentHasStarted){
              c.newsendInstructionToParticipant(sender, "Please wait for the experiment to start");
              return;
        }
        
        MazeGameController2WAY mgccc = this.getMazeGameController(sender);
        Participant pRecipient = (Participant)this.pp.getRecipients(sender).elementAt(0);
        long currentTurnNo = sender.getNumberOfChatMessagesProduced();  
        MazeGameController2WAY mgcNEW = this.getMazeGameController(sender);
        Vector additionalData = mgcNEW.getAdditionalData(sender);  
        c.newrelayTurnToPermittedParticipants(sender, mct,additionalData);        
        mgcNEW.appendToUI(sender.getUsername()+": "+mct.getText());      
        
        
                
        if(mct.getText().equalsIgnoreCase("rj")){
            this.getMazeGameController(sender).reconnectParticipant(sender);
            c.newsendInstructionToParticipant(sender, "RESETTING");
        }
                
                
    }

    @Override
    public void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
        this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow(),false);
    }

    @Override
    public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
         this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow(),true);
    }
     
    
    
    
    public synchronized void updateInfoAsItIsTyped(Participant sender, String textInBoxOLD, boolean updateISREMOVE){
           try{
              //textInBox = "PRESET"+textInBoxOLD.replaceAll("\n", "").replace("\n", "").replaceAll("\r\n", "").replaceAll("\r", "")+"ENDSET";
               
               
           }catch(Exception e){e.printStackTrace();}
           
          
        
          
    }    
    
}
