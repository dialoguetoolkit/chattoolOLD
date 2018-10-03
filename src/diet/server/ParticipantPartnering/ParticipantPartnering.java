


package diet.server.ParticipantPartnering;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ParticipantPartnering.ui.JPanelParticipantPartnering;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JPanel;





public class ParticipantPartnering {
    
   
    Hashtable htSubdialogues = new Hashtable();
    public Conversation c;
    
    long subdialogueCOUNTING=0;
    
    public ParticipantPartnering(Conversation c) {
        this.c=c;
    }   
    
    
    public String getSubdialogueID(Participant p){
        try{
           Object o =  this.htSubdialogues.get(p);
           if(o==null)return null;
           SubDialogue sd = (SubDialogue)o;        
           return sd.getID();
        
        }catch (Exception e){
            e.printStackTrace();
        }
        return "NOTSET";
    }
    
    
    //Note that in this case, getSenders and getRecipients return the same...but it needn't always be the case
    public Vector<Participant> getSenders(Participant recipient){
        SubDialogue sd = (SubDialogue)this.htSubdialogues.get(recipient);
        if(sd==null)return new Vector();
        return sd.getAllOtherParticipants(recipient);
    }
    
    
    
    public Vector<Participant> getRecipients(Participant p){
         try{
           SubDialogue sd = (SubDialogue)this.htSubdialogues.get(p);
           return sd.getAllOtherParticipants(p);
        
        }catch (Exception e){
           // e.printStackTrace();
            System.err.println("THER ARE NO RECIPIENTS FOR "+p.getUsername());
        }
        return new Vector();
    }
    
    
    public Vector getRecipientsNames(Participant p){
        Vector recipients = this.getRecipients(p);
        Vector names = new Vector();
        
        for(int i=0;i<recipients.size();i++){
            Participant pRecip = (Participant)recipients.elementAt(i);
            names.addElement(pRecip.getParticipantID()+","+pRecip.getUsername());
        }
        return names;
    }
    
    public String getRecipientsAsString(Participant p){
        Vector recipients = this.getRecipients(p);
        String names ="";
        for(int i=0;i<recipients.size();i++){
            Participant pRecip = (Participant)recipients.elementAt(i);
            names = names + pRecip.getParticipantID()+","+pRecip.getUsername()+";";
        }
        return names;
    }
    
    
    
    
    public void createNewSubdialogue(Vector vparticipants){
        Vector vparticipantsclone = (Vector)vparticipants.clone();
        String id =  ""+subdialogueCOUNTING;
        subdialogueCOUNTING++;
        this.createNewSubdialogue(id, vparticipantsclone);
    }
    
   
    public void createNewSubdialogue(Participant...ps){
        Vector v = new Vector(Arrays.asList(ps));
        String id =  ""+subdialogueCOUNTING;
        subdialogueCOUNTING++;
        this.createNewSubdialogue(id, v);
    }
    
    public void createNewSubdialogue(String subdialogueID, Participant...ps){
        Vector v = new Vector(Arrays.asList(ps));
        String id =  ""+subdialogueCOUNTING;
        subdialogueCOUNTING++;
        this.createNewSubdialogue(subdialogueID,v);
    }
    
    
    public void createNewSubdialogue(String id,Vector vvps){
         Vector vparticipantsclone = (Vector)vvps.clone();
        ///First need to remove them from all other subdialogues
        for(int i=0;i<vparticipantsclone.size();i++){
            Participant p = (Participant)vparticipantsclone.elementAt(i);
            SubDialogue sd = (SubDialogue)this.htSubdialogues.get(p);
            if(sd!=null){
               sd.vps.remove(p);
               sd.id=sd.id+"_removed("+p.getParticipantID()+","+p.getUsername()+"";
            }
            
            
        }
        
        SubDialogue sd = new SubDialogue(id,vparticipantsclone);
        for(int i=0;i<vparticipantsclone.size();i++){
            Participant p = (Participant)vparticipantsclone.elementAt(i);
            this.htSubdialogues.put(p, sd);
        }
        
        Conversation.printWSln("Main", "---------------------------------------------------------");
        Conversation.printWSln("Main", "Created new subdialogue called: "+id +" with the following participants:");
        Vector newParticipants = sd.getParticipants();
        for(int i=0;i<newParticipants.size();i++){
            Participant p = (Participant)newParticipants.elementAt(i);
            Conversation.printWSln("Main", p.getParticipantID()+" " +p.getUsername());
        }
        c.getCHistoryUIM().updateParticipantPartneringHasChanged();
        
    }
    
    
    
    
    
    public Vector getRecipientsSettings_DEPRECATEDFOROLDCOMPATIBILITY(Participant p){
        //Returns Vector with first element vector of Participants, 2nd element is vector of Participants' names'
        //Default seetting is that all other participants are enabled.
        Vector participants = c.getParticipants().getAllParticipants(); 
        Vector vRecipients = new Vector();
        Vector vRecipientsEmails = new Vector();
        Vector vRecipientsUsernames = new Vector();
        Vector vRecipientsWindowNumbers = new Vector();
        Vector v = new Vector();
        int pIndex = participants.indexOf(p);
        for(int i=0;i<participants.size();i++){
            //System.err.println("Getting permission for "+p.getUsername());
            Participant p2 = (Participant)participants.elementAt(i);
            if(p!=p2){//Doesn't need to be able to send to self'
                
                        vRecipients.addElement(p2);
                        vRecipientsEmails.addElement(p2.getParticipantID());
                        vRecipientsUsernames.addElement(p2.getUsername());
                        vRecipientsWindowNumbers.addElement(0);
    
            }
        }
        v.addElement(vRecipients);
        v.addElement(vRecipientsEmails);
        v.addElement(vRecipientsUsernames);
        v.addElement(vRecipientsWindowNumbers);                
        return v;
     }     
        
    
    
   
    
    
  public JPanelParticipantPartnering getJPanel(){
      return new JPanelParticipantPartnering(this);
  }
    
    
    

}
