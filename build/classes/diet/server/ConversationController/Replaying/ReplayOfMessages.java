/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController.Replaying;

import diet.message.Message;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.Participant;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomDutchSelfRepairInitiation;
import diet.textmanipulationmodules.WhitelistBlacklistFilterTrigger.WhitelistBlacklistFilterTrigger;
import diet.textmanipulationmodules.Selfrepairgeneration.SelfRepair;
import diet.textmanipulationmodules.WhitelistBlacklistFilterTrigger.SimpleWhitelistOfDutchOK;
import diet.utils.VectorHashtable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class ReplayOfMessages {
    
    //String fileName = "C:\\New folder\\gd\\2014-2015\\socialmedia\\laboratory\\data\\rawdata\\server1\\data\\Saved experimental data\\0001CCMAZE_DYADIC_MULTI_PARTICIPANTS_SEPARATEDYADS\\messages.dat";

    String fileName = "C:\\New folder\\gd\\2014-2015\\discoursepragmatics\\projects\\DATA\\14.12.11.MM3. DigitalCommunication. BASELINE\\0001CCMAZE_DYADIC_MULTI_PARTICIPANTS_SEPARATEDYADS\\messages.dat";
    
    
    
    
    //Vector incomingMessages = new Vector();
    
    Vector allMessages = new Vector();
    
   //String spreadsheet = "C:\\New folder\\gd\\2014-2015\\socialmedia\\laboratory\\data\\rawdata\\server1\\data\\Saved experimental data\\0001CCMAZE_DYADIC_MULTI_PARTICIPANTS_SEPARATEDYADS\\turns.txt";
    
    
    public void loadMessages(){
        try{
             ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
             while (2<5){
                 //System.out.println("BEFORE");
                 Object o= in.readObject();
                //System.out.println("AFTER");
                 Message m = (Message)o;
                 vht.put(m.getEmail()+m.getUsername(), o);
                 allMessages.addElement(m);
                 
             }
        }catch(Exception e){
            // e.printStackTrace();
            System.err.println("PROBABLY ALL LOADED!");
         }
        
       
        
      
    }
    
    
    public void replayMessagesAddingTentativeOLD(){
        
        SimpleWhitelistOfDutchOK sWDO =  new SimpleWhitelistOfDutchOK();
        
        Vector outputROWS = new Vector();
        WhitelistBlacklistFilterTrigger wlblft = new WhitelistBlacklistFilterTrigger("/whitelistblacklist/whitelistturns.txt", 
                "/whitelistblacklist/whitelistwords.txt",
                "/whitelistblacklist/blacklistturns.txt",
                "/whitelistblacklist/blacklistwords.txt",
                "/whitelistblacklist/blacklistendofturns.txt",
                 "/whitelistblacklist/blacklistpunctuation.txt"
        );
         
       
        
        
        for(int i=0;i<this.allMessages.size();i++){
            Object o = allMessages.elementAt(i);
            if(o instanceof diet.message.MessageChatTextFromClient){
                MessageChatTextFromClient mctfc = (MessageChatTextFromClient)         o;
                String outputROW = mctfc.getEmail()+"|"+mctfc.getText()+"|";
                boolean isOKToTriggerIntervention = wlblft.isContainedInWhitelistsAndNotInBlacklists(mctfc.getText());
                if(isOKToTriggerIntervention){
                    outputROW = outputROW + "|"+ mctfc.getText()+"?"+"|";
                    if((mctfc.getText()+"?").equalsIgnoreCase("?")){
                        System.exit(-5);
                    }
                    
                }
                boolean foundOKYES = sWDO.checkWhitelistForTurn(mctfc.getText());
                if(foundOKYES){
                    if(isOKToTriggerIntervention){
                        outputROW = outputROW + "";
                    }
                    else{
                         outputROW = outputROW + "||";
                    }
                    outputROW = outputROW+    mctfc.getText()+ "(EXCLAMATION_OR...)|";          
                            
                            
                    
                }
                
                outputROWS.addElement(outputROW);
                
            }
        }
        this.saveOutput(outputROWS);
    }
    
    
    
    
    
    VectorHashtable vht = new VectorHashtable();    
    Vector outputStrings = new Vector();
    
    
    
    
    public void replayMessages_SelfRepair(){
        diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomDutchSelfRepairInitiation crdsri = new CyclicRandomDutchSelfRepairInitiation();
        SelfRepair sr = new SelfRepair();
        Enumeration e = vht.ht.keys();
        while (e.hasMoreElements()){
            //System.err.println("--"+e.nextElement());
            Object key = e.nextElement();
            Participant p = new Participant(null,(String)key,"");
            
            
            Vector participantsMessages = vht.get(key);
            int nonInterventions =0;
            int interventions =0;
            
            for (int i=0;i<participantsMessages.size();i++){
               Object o = participantsMessages.elementAt(i);
               if(o instanceof diet.message.MessageWYSIWYGDocumentSyncFromClientInsert){
                    MessageWYSIWYGDocumentSyncFromClientInsert mwdsfci = (MessageWYSIWYGDocumentSyncFromClientInsert)o;       
                    sr.addRevision(p,mwdsfci.getAllTextInWindow() );
                    //sr.
               }
               if(o instanceof diet.message.MessageWYSIWYGDocumentSyncFromClientRemove){
                    MessageWYSIWYGDocumentSyncFromClientRemove mwdsfcr = (MessageWYSIWYGDocumentSyncFromClientRemove)o;       
                    sr.addRevision(p,mwdsfcr.getAllTextInWindow() );
               }
               if(o instanceof diet.message.MessageChatTextFromClient){
                   MessageChatTextFromClient mctftfc = (MessageChatTextFromClient)o;
                   String row = p.getParticipantID() +"|"+ mctftfc.getText()+"|";
                   String[] intervention = sr.getFirstPart_SecondPart_SummaryWithErrorCheck(p);
                   if(intervention[0]==null || intervention[1]==null){
                       System.err.print(".");
                       nonInterventions++;
                       row = row + nonInterventions +"|"+interventions+"|";
                   }
                   else{
                       System.err.print("\nINTERVENTION\n");
                       interventions++;
                       String repairInitiation = crdsri.getNext(p);
                       
                       
                        row = row + nonInterventions +"|"+interventions+"|"+intervention[0]+" "+repairInitiation+" "+intervention[1].replace("\n", "");
                        if(intervention[0].contains("vervolgens")||intervention[1].equalsIgnoreCase("") ||intervention[1].equalsIgnoreCase(null)||intervention[1].equalsIgnoreCase("\n")){
                            System.err.println("EXITING AT NULL:" +intervention[0]);
                            System.err.println("EXITING AT NULL:" +intervention[1].replace("\n", "~")+"..");
                            System.exit(-5);
                        }
                   }
                   sr.setNewTurn(p);                
                   
                  this.outputStrings.addElement(row);
                }
              
           }
           System.err.println(p.getParticipantID()+" "+interventions+ " non: "+nonInterventions);   
                
        
        }
   
        
    }
    
    public void saveOutput(Vector v){
        try {
         
            File file = new File(System.getProperty("user.dir")+File.separator+"replayoutput.csv");
            if (!file.exists()) {
				file.createNewFile();
	    }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0;i<v.size();i++){
                String s = (String)v.elementAt(i);
                bw.write(s+"\n");
                bw.flush();
            }
            bw.close();
	    } catch (IOException e) {
		e.printStackTrace();
            }
    }
    
    
    public static void mainMARTIN(String[] args){
       ReplayOfMessages rom= new ReplayOfMessages();   
       rom.loadMessages();
       System.out.println("DONE LOADING");
       rom.replayMessages_SelfRepair();
       System.out.println("SAVE OUTPUT!");
       rom.saveOutput(rom.outputStrings);
       System.out.println("DONE!");
    }
    
    
    
    
    
    public void replayMessagesAddingTentative(){
        Vector outputROWS = new Vector();
        WhitelistBlacklistFilterTrigger wlblft = new WhitelistBlacklistFilterTrigger("/whitelistblacklist/whitelistturns.txt", 
                "/whitelistblacklist/whitelistwords.txt",
                "/whitelistblacklist/blacklistturns.txt",
                "/whitelistblacklist/blacklistwords.txt",
                "/whitelistblacklist/blacklistendofturns.txt",
                "/whitelistblacklist/blacklistpunctuation.txt"
        );
         
       boolean isOKToTriggerIntervention = wlblft.isContainedInWhitelistsAndNotInBlacklists("? row 5");
       if(isOKToTriggerIntervention){
           System.err.println("IT IS OK TO TRIGGER INTERVENTION");
       }
        
        
    }
    
    
    
    
       
    
     public static void main(String[] args){
       
       ReplayOfMessages rom= new ReplayOfMessages();   
       rom.loadMessages();
       System.out.println("DONE LOADING");
       rom.replayMessagesAddingTentativeOLD();
       System.out.println("SAVE OUTPUT!");
      // rom.saveOutput(rom.outputStrings);
       System.out.println("DONE!");
    }
    
}
