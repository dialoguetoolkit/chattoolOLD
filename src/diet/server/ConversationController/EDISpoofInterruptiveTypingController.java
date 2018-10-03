package diet.server.ConversationController;
import diet.attribval.AttribVal;
import diet.debug.Debug;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageTask;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.ConnectionListener;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.ConversationController.ui.JInterfaceToggleButtonsFIVE;
import diet.server.ConversationController.ui.JInterfaceMenuButtonsReceiverInterface;
import diet.server.IsTypingController.IsTypingOrNotTyping;
import diet.server.Participant;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;




public class EDISpoofInterruptiveTypingController extends DefaultConversationController implements JInterfaceMenuButtonsReceiverInterface{

       
    String currentMode = "instructions";
    
    public static boolean showcCONGUI(){
        return true;
    } 

     
        
    
    public EDISpoofInterruptiveTypingController(Conversation c) {
        super(c);
        CustomDialog.showDialog("Must logon with either..AAAA1"+ " or BBBB1");
        String portNumberOfServer = ""+ConnectionListener.staticGetPortNumber();
        this.setID("DefaultDyadicConversation"); 
        sett.login_numberOfParticipants =2;
        JInterfaceToggleButtonsFIVE jimb = new JInterfaceToggleButtonsFIVE(this, "instructions", "faketypingbyA", "faketypingbyB", "nofaketyping", "nofaketypingnodeleting");
    }

    @Override
    public void performActionTriggeredByUI(String s) {
        this.currentMode=s;
        System.err.println("SELECTION OF "+s);
       
        int indexOffset=0;
        if(s.equalsIgnoreCase("faketypingbyA"))indexOffset=1;
        if(s.equalsIgnoreCase("faketypingbyB"))indexOffset=2;
        if(s.equalsIgnoreCase("nofaketyping"))indexOffset=3;
        if(s.equalsIgnoreCase("nofaketypingnodeleting")){        
            indexOffset=0;
            
        }
        itnt = new IsTypingOrNotTyping(this);
        
         for(int i=0;i<this.participantsQueuedAAAA.size();i++){
                 Participant pA = (Participant)this.participantsQueuedAAAA.elementAt(i);
                 int indexOfB = i + indexOffset;
                 if(indexOfB>=this.participantsQueuedAAAA.size()) indexOfB = indexOfB - this.participantsQueuedAAAA.size();
                  Participant pB = (Participant)this.participantsQueuedBBBB.elementAt(indexOfB);   
                 pp.createNewSubdialogue(pA,pB);
                 if(s.equalsIgnoreCase("nofaketypingnodeleting")){
                      c.changeClientInterface_DisableDeletes(pB);
                      c.changeClientInterface_DisableDeletes(pA);
                 }
                 else{
                     c.changeClientInterface_EnableDeletes(pB);
                     c.changeClientInterface_EnableDeletes(pA);
                 }
                 
                 this.itnt.addPairWhoAreMutuallyInformedOfTyping(pA, pB);
                 Conversation.printWSln("Main", "Pairing "+i+ "("+pA.getParticipantID()+","+pA.getUsername()+")" +"   with "+indexOfB+"("+pB.getParticipantID()+","+pB.getUsername()+")");
                 System.err.println("Pairing "+i+ "("+pA.getParticipantID()+","+pA.getUsername()+")" +"   with "+indexOfB+"("+pB.getParticipantID()+","+pB.getUsername()+")"); 
          }
          //this.itnt.setWhoSeesEachOthersTyping(pp);
    }
   
     /*
      public boolean requestParticipantJoinConversationDEBUG(String participantID) {
          boolean isCorrect = this.requestParticipantJoinConversationCORRECT(participantID);
          if(!isCorrect)return false;
          if(this.participantsQueuedAAAA.size()==0 & this.participantsQueuedBBBB.size()==0){
              return true;
          }
          if(this.participantsQueuedAAAA.size()>this.participantsQueuedBBBB.size()&participantID.startsWith("BBB")) return true;
          if(this.participantsQueuedAAAA.size()<=this.participantsQueuedBBBB.size()&participantID.startsWith("AAA")) return true;
          return false;
      }
    */
    
    
    
    public boolean requestParticipantJoinConversation(String participantID) {
        
        
        if(participantID.equals("AAAA1"))return true;
        if(participantID.equals("AAAA2"))return true;
        if(participantID.equals("AAAA3"))return true;
        if(participantID.equals("AAAA4"))return true; 
        if(participantID.equals("AAAA5"))return true;
        if(participantID.equals("AAAA6"))return true;
        if(participantID.equals("AAAA7"))return true;
        if(participantID.equals("AAAA8"))return true;
        if(participantID.equals("AAAA9"))return true;
        if(participantID.equals("AAAA0"))return true;
        if(participantID.equals("AAAAX"))return true;
         if(participantID.equals("AAAAY"))return true;
        
          if(participantID.equals("BBBBY"))return true;
        if(participantID.equals("BBBB1"))return true;
        if(participantID.equals("BBBB2"))return true;
        if(participantID.equals("BBBB3"))return true;
        if(participantID.equals("BBBB4"))return true;
        if(participantID.equals("BBBB5"))return true;
        if(participantID.equals("BBBB6"))return true;
        if(participantID.equals("BBBB7"))return true;
        if(participantID.equals("BBBB8"))return true;
        if(participantID.equals("BBBB9"))return true;
        if(participantID.equals("BBBB0"))return true;
        if(participantID.equals("BBBBX"))return true;
        
        return false;
    }
    
    
   
    
    
    
    
    Vector participantsQueuedAAAA = new Vector(); 
    Vector participantsQueuedBBBB = new Vector(); 
    
    @Override
    public synchronized void participantJoinedConversation(final Participant p) {
         if(p.getParticipantID().startsWith("AAA")){
             this.participantsQueuedAAAA.addElement(p);
         }
         else if(p.getParticipantID().startsWith("BBB")){
             this.participantsQueuedBBBB.addElement(p);
         }
         else{
             Conversation.printWSln("Main", "ERROR:" +p.getUsername() + " JUST LOGGED IN! BUT SHOULDNT BE ABLE TO");
             System.err.println("ERROR:" +p.getUsername() + " JUST LOGGED IN! BUT SHOULDNT BE ABLE TO");
             return;
         }
          Conversation.printWSln("Main", p.getUsername() + " JUST LOGGED IN");
          System.err.println(p.getUsername() + " JUST LOGGED IN");
          if(this.participantsQueuedAAAA.size()==this.participantsQueuedBBBB.size()){
              Conversation.printWSln("Main", "THERE IS NOW AN EQUAL NUMBER OF PARTICIPANTS ON BOTH SIDES = "+this.participantsQueuedAAAA.size());
              System.err.println("THERE IS NOW AN EQUAL NUMBER OF PARTICIPANTS ON BOTH SIDES = "+this.participantsQueuedAAAA.size());
          }
          else{
              Conversation.printWSln("Main", "UNEQUAL: AAAA="+this.participantsQueuedAAAA.size()+"-----------BBBBB="+this.participantsQueuedBBBB.size());
              System.err.println("UNEQUAL: AAAA="+this.participantsQueuedAAAA.size()+"-----------BBBBB="+this.participantsQueuedBBBB.size());
          }
         
         
        
    }
    
    
    @Override
    public void startExperiment(){
        
    } 
    
    
    
   
    
    

    
    
    @Override
    public void participantRejoinedConversation(Participant p) {
       
        super.participantRejoinedConversation(p);
        
        
    }
    
    
    
   
   
   
   
   
   
   
   public synchronized void processTaskMove(MessageTask mtm, Participant origin) {            
    }
   
   
   
    
    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct){    
        
        
          if(this.currentMode.equalsIgnoreCase("instructions")){
              c.newsendInstructionToParticipant(sender, "Please do not type anything. Please wait for instructions" + sender.getNumberOfChatMessagesProduced());
              return;
          }
        
          
          
          boolean interferedWith = false;
          Object o = htTurnIsToBeInterferedWith.get(sender);
          if (o != null) interferedWith = (boolean)o;  
          AttribVal av = new AttribVal("flaggedformanipulation","false");  
          if(interferedWith){
               av = new AttribVal("flaggedformanipulation","true");  
          }         
          Vector vAV = new Vector(); vAV.addElement(av);
          
          if(Debug.debugtimers)mct.saveTime("ServerDefaultDyadicConverationController.StartingITNTProcessTurnSentByClient");
          itnt.processTurnSentByClient(sender);       
           if(Debug.debugtimers)mct.saveTime("ServerDefaultDyadicConverationController.FinishedITNTProcessTurnSentByClient");
          
          Object o2 = htTurnWasInterferedWith.get(sender);
          if (o2 != null) interferedWith = (boolean)o2;  
          AttribVal av2 = new AttribVal("wasmanipulated","false");  
          if(interferedWith){
               av2 = new AttribVal("wasmanipulated","true");  
          }         
          vAV.addElement(av2);
           
          
          
          
          
          
          
          
          c.newrelayTurnToPermittedParticipants(sender, mct,vAV);
          
          
          
          
          
          
          int nextTurnIsInterferedWith = r.nextInt(3);
          this.htTurnWasInterferedWith.put(sender, false);
          if(nextTurnIsInterferedWith==0){
              this.htTurnIsToBeInterferedWith.put(sender, true);   
              //c.newsendInstructionToParticipant(sender,"INTERFERING");
          }
          else{
              this.htTurnIsToBeInterferedWith.put(sender, false);    
               //c.newsendInstructionToParticipant(sender,"NOT");
          }
          
          
    }
    
    
    //The more complex version:
    
    //A sends turn
    /////Possibility 1 : B has text already typed and B is typing
    /////Possibility 2 : B has text already typed and B is not typing
    /////Possibility 3:  B has no text typed and B is not typing
    
    
   
    /////////////////////////
    //The simple version
    //A sends turn
    
   
    
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
         
         Vector recipients = pp.getRecipients(sender);
         if(recipients.size()==0)return;
         if(recipients.size()>1){
             Conversation.printWSln("Main", "Error - the recipient "+sender.getUsername()+" has more than one partner...");
         }
         Participant recipient = (Participant)recipients.elementAt(r.nextInt(recipients.size()));
         System.err.println("THE KEYCODE IS: "+mkp.getKeypressed().getKeycode());
         
         if(mkp.getKeypressed().getKeycode()!=KeyEvent.VK_ENTER){
             if( (sender.getParticipantID().startsWith("BBB")&&this.currentMode.equalsIgnoreCase( "faketypingbyA"))
             ||  (sender.getParticipantID().startsWith("AAA")&&this.currentMode.equalsIgnoreCase( "faketypingbyB"))) {
                 
                 Object o = htTurnIsToBeInterferedWith.get(sender);
                 if (o == null)return;
                 boolean interfereWithTurn = (boolean)o;    
                 if(interfereWithTurn)  this.createFakeTyping(recipient, sender);
                
                 
                
             }            
         }
    }

    Hashtable htTurnIsToBeInterferedWith = new Hashtable();
    Hashtable htTurnWasInterferedWith = new Hashtable();
    
    public void createFakeTyping(Participant spoofTyping, Participant victim){

        int decision = r.nextInt(10);
        if(decision == 1){
           for(long l = new Date().getTime(); l < new Date().getTime()+ r.nextInt(10000); l = l+ 900){
                itnt.addSpoofTypingInfo(spoofTyping, l);     
           }
           this.htTurnWasInterferedWith.put(victim, true);
            
        }
        
        
        //itnt.addSpoofTypingInfo(recipient, l);
        
       /* for(long l = new Date().getTime(); l < new Date().getTime()+ r.nextInt(5000); l = l+ 900){
                      itnt.addSpoofTypingInfo(recipient, l);
                      //System.exit(-213);
                  }   
        
        */
        
    }
    
    
    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
           
           long startOfKeypressWrite = new Date().getTime();
          
           this.itnt.processDoingsByClient(sender);
           long finishOfKeypressWrite = new Date().getTime();
           
           long durationOfKeypressProcessBYITNT =  finishOfKeypressWrite-startOfKeypressWrite;
           c.convIO.saveTextToFileCreatingIfNecessary(durationOfKeypressProcessBYITNT+"", "KEYPRESSWRITE");
           
           
           
           
    }
    
    
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
          this.itnt.processDoingsByClient(sender);
    }
    
   
    
   
   


    
    
   

   

}
