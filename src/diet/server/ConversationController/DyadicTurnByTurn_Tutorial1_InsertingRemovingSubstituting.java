package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageClientInterfaceEvent;
import diet.message.MessageKeypressed;
import diet.message.MessageTask;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.ConnectionListener;
import diet.server.Conversation;
import diet.server.Participant;
import java.util.Random;
import java.util.Vector;




public class DyadicTurnByTurn_Tutorial1_InsertingRemovingSubstituting extends DefaultConversationController{

    
    
    
    public static boolean showcCONGUI(){
        return true;
    } 

    
    
    public DyadicTurnByTurn_Tutorial1_InsertingRemovingSubstituting(Conversation c) {
        super(c);
        String portNumberOfServer = ""+ConnectionListener.staticGetPortNumber();
        this.setID("Dyadic");
        this.experimentHasStarted=true;     
    }
    public DyadicTurnByTurn_Tutorial1_InsertingRemovingSubstituting(Conversation c, long istypingtimeout) {
        super(c,istypingtimeout);
        String portNumberOfServer = ""+ConnectionListener.staticGetPortNumber();
        this.setID("Dyadic");
        this.experimentHasStarted=true;     
        
        
    }
    
    
    
    
    
     @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        return true;        
    }
    
    
    @Override
    public synchronized void participantJoinedConversation(final Participant p) {
        super.participantJoinedConversation(p);
             
    }
    
    

    
    @Override
    public void participantRejoinedConversation(Participant p) {     
        super.participantRejoinedConversation(p); 
        if(c.getNoOfParticipants()==1){
             c.sendInstructionToParticipant(p,"Hello! Please wait for the other participant to log in");
        }
        else if (c.getNoOfParticipants()==2){
             c.sendInstructionToMultipleParticipants(c.getParticipants().getAllParticipants(), "Please start!");
        }
    }
    
   
   
   public synchronized void processTaskMove(MessageTask mtm, Participant origin) {            
    }
   
   
   
    
    
   
    public synchronized void processChatText2(Participant sender, MessageChatTextFromClient mct){    
          itnt.processTurnSentByClient(sender);
          
          String turn = mct.getText();    
          
          if(turn.contains(":(")) {
               turn = turn.replace(":(", "");
               Vector recipients = pp.getRecipients(sender);
               c.sendArtificialTurnFromApparentOriginToParticipants(sender, recipients, turn);
          }
          else{
                c.relayTurnToPermittedParticipants(sender, mct);  
          }          
    }
    
    
    Random r = new Random();
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct){    
          itnt.processTurnSentByClient(sender);
          
          if(r.nextBoolean()){
               String turn = mct.getText() + " :)" ;
               Vector recipients = pp.getRecipients(sender);
               
               c.sendArtificialTurnFromApparentOriginToParticipants(sender, recipients, turn);
          }
          else{
               c.relayTurnToPermittedParticipants(sender, mct);     
          }      
    }
    
    
    
    
    
    
    
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
       super.processKeyPress(sender, mkp);
        // super.processKeyPress(sender, mkp);
        //this.itnt.processDoingsByClient(sender);
         //this.itnt.addSpoofTypingInfo(sender, new Date().getTime()+1000);
         //this.itnt.addSpoofTypingInfo(sender, new Date().getTime()+2000);
       
        
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
 
           //this.itnt.processDoingsByClient(sender);
    }
    
    
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
          // this.itnt.processDoingsByClient(sender);
    }

    
   
    
    @Override
    public void processClientEvent(Participant origin, MessageClientInterfaceEvent mce) {
       
    }
    
   
    
   
   


    
    
   

   

}
