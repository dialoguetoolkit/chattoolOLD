package diet.server.conversationhistory.turn;
import diet.attribval.AttribVal;
import java.io.Serializable;
import java.util.Vector;

import diet.message.Keypress;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.DocChangesIncomingSequenceFIFO;
import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;




/**
 * This is the representation of individual turns in the conversation. It stores the origin, the recipients,
 * the keypresses, the individual words used and their associated patterns of usage, the timing data and the 
 * parse tree (if stored).
 * @author user
 */
public class Turn implements Serializable{
  
  public Conversant sender;
  String senderID ="";
  Vector recipients = new Vector();
  Vector additionalValues = new Vector();
  String subdialogueID;
  public transient ConversationHistory cH;
  public String text="";
   Conversant apparentSender ;
  
   Vector keyPresses = new Vector();
   Vector documentUpdates = new Vector(); 
   Vector clientinterfaceevents = new Vector();
  
  
   public Turn(){
       
   }
  
    
    public String getSubdialogueID(){
        return subdialogueID;
    }
    
    public String getRecipientsAsString(){
        String recipients ="";
        for(int i=0;i<this.recipients.size();i++){
            Conversant convrsnt = (Conversant)this.recipients.elementAt(i);
            if(i==0){
                recipients = convrsnt.getUsername();
            }
            else{
                recipients = recipients + ", "+ convrsnt.getUsername();
            }
        }
        return recipients;
    }
    
    
    
      public Vector getRecipients(){
        return recipients;
    }
    
    public Conversant getSender(){
        return sender;
    }
    
    public String getText(){
        return this.text;
    }
    
   
    
    
    
    public Conversant getApparentSender(){
        return this.apparentSender;
    }
   
    public String getApparentSenderUsername(){
         
         
        
        if(apparentSender==null) return "";
        return apparentSender.getUsername();
    }
    
    public String getSenderID(){
        return senderID;
    }
    
    
    public boolean getTypingWasBlockedDuringTyping(){
        return false;
    }
     
   
    public int getKeypressDeletes(){
        return 0;
    }
    
    public int getDocDeletes(){
        return 0;
   }
    
    

    
    public Vector getAdditionalValues(){
        if(this.additionalValues==null) return new Vector();
        return this.additionalValues;
    }
   
    
     public String getIOAdditionalValues(){
        
        if(this.additionalValues==null){
            return DefaultConversationController.sett.recordeddata_CSVSeparator;
        }
        else if(this.additionalValues.size()==0){
            return DefaultConversationController.sett.recordeddata_CSVSeparator;
        } 
        String returnval = "";
        try{
            for(int i=0;i<this.additionalValues.size();i++){
                returnval = returnval + DefaultConversationController.sett.recordeddata_CSVSeparator+ ((AttribVal)additionalValues.elementAt(i)).getVal();
            }
        }catch (Exception e){
            e.printStackTrace();
            return DefaultConversationController.sett.recordeddata_CSVSeparator+"error";
        }

        return returnval+DefaultConversationController.sett.recordeddata_CSVSeparator;
    }
    
   
     public Vector getClientInterfaceEvents(){
        return this.clientinterfaceevents;
    }
    
    
     public Vector getDocumentUpdates() {
        return documentUpdates;
    }

    public void setDocumentUpdates(Vector documentUpdates) {
        this.documentUpdates = documentUpdates;
    }

    public Vector getKeyPresses() {
        return keyPresses;
    }

    public void setKeyPresses(Vector keyPresses) {
        this.keyPresses = keyPresses;
    }
    
    
    
}
