package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import javax.swing.JFileChooser;




public class CC_TANGRAM extends DefaultConversationController{

    public CC_TANGRAM(Conversation c) {
        super(c);
    }
   
    
    
   
    
    
    @Override
    public void participantJoinedConversation(Participant p) {                                                   //self       //other     //instruction
        //c.changeClientInterface_AllColours(Color.black, Color.white, Color.blue, Color.red, Color.magenta, Color.orange, Color.gray, Color.green, Color.cyan, Color.cyan, 16);
   
    }
    
        
    
    public void enableParticipantAdisableParticipantB(Participant a,Participant b){
         c.changeClientInterface_disableTextEntry(b);
         c.changeClientInterface_enableTextEntry(a);        
    }
    
    
    
   

   
    
    
    
     

     
     
     
     
    
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
          
           c.deprecated_relayTurnToAllOtherParticipants(sender,mct, pp.getRecipientsSettings_DEPRECATEDFOROLDCOMPATIBILITY(sender));
           //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false, pp.getRecipientsSettings_DEPRECATEDFOROLDCOMPATIBILITY(sender));
           
          
          
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
        c.newsaveClientKeypressToFile(sender, mkp);

    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
          
       
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        //c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);
        //turnBeingConstructed.remove(mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime()); 
        //chOut.addMessage(sender,mWYSIWYGkp);
    }
    
    

    @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        //return super.requestParticipantJoinConversation(participantID);
        if(c.getParticipants().getAllParticipants().size()<2)return true;
        return false;
    }
    
   
   


    
    
    

   

}
