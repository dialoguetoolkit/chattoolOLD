/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.transcript;

import diet.transcript.ui.JTranscriptFrame2;
import diet.attribval.AttribVal;
import diet.client.ClientInterfaceEvents.ClientInterfaceEvent;
import diet.client.ClientInterfaceEvents.ClientInterfaceEventTracker;
import diet.message.MessageClientInterfaceEvent;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;
import java.awt.Color;
import java.util.Vector;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author gj
 */
public class Transcript_BACKUP2 {
    
     public Vector allRows = new Vector();
    
    
    JTranscriptFrame2 jtf;
    
     public SimpleAttributeSet textSENTBYOTHER = new SimpleAttributeSet();
     public SimpleAttributeSet textSENTBYSELF = new SimpleAttributeSet();
     public SimpleAttributeSet stringTypedByself = new SimpleAttributeSet();
     public SimpleAttributeSet stringTypedByselfWhileOtherIsTyping = new SimpleAttributeSet();

      
     final String participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages = "phtybirisitm";
    
     Participant p;
     Conversation c;
     
     String fileName = "transcript";
    
    public Transcript_BACKUP2(Participant p,Conversation c){
        this.c=c; 
        this.p=p;
        jtf = new JTranscriptFrame2(null,c, p);
        this.initializeStyles();
        fileName = "transcript_"+p.getUsername()+".csv";
       
        c.getConvIO().saveTextToFileCreatingIfNecessary("", fileName);
       
         
    }
    
    boolean latestSaveIsAppendable = true;
    
    private void saveData(String sTurntype, String sEDITS, String selfFULLTURN, String otherFULLTURN, String otherTEXTINWIDOW1){
         String[] row = {sTurntype, sEDITS, selfFULLTURN,otherFULLTURN, otherTEXTINWIDOW1};
          this.allRows.addElement(row);
          if(sEDITS.length()>0){
              c.getConvIO().saveTextToFileCreatingIfNecessary("\n"+ sTurntype +  DefaultConversationController.sett.recordeddata_CSVSeparator + sEDITS, fileName);
              latestSaveIsAppendable=true;
          }
          else{
             String textToSave =  sTurntype + DefaultConversationController.sett.recordeddata_CSVSeparator +
                                       sEDITS + DefaultConversationController.sett.recordeddata_CSVSeparator +
                                       selfFULLTURN + DefaultConversationController.sett.recordeddata_CSVSeparator +
                                       otherFULLTURN + DefaultConversationController.sett.recordeddata_CSVSeparator +
                                       otherTEXTINWIDOW1;
             if(latestSaveIsAppendable) 
             latestSaveIsAppendable=false;
             textToSave="\n"+textToSave;
             c.getConvIO().saveTextToFileCreatingIfNecessary( textToSave , fileName);
          }
    }
    private void appendDatatoEdits(String sAdding){
        if(allRows.size()==0){
            String[] row = {"","","","",""};
            allRows.addElement(row);
        }
        
         String[] row = (String[])this.allRows.lastElement();
         row[1] = row[1] + sAdding;
         c.getConvIO().saveTextToFileCreatingIfNecessary(sAdding, fileName);
        
    }
    
    
    public void processClientEvent(MessageClientInterfaceEvent mcie){
        //jtf.appendText("THIS\u2287", stringTypedByselfWhileOtherIsTyping);
        ClientInterfaceEvent cie = mcie.getClientInterfaceEvent();
        
        
        
        if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyotherparticipant)){
            AttribVal av  = (AttribVal)cie.getAttribVal("text");
            String text = (String)av.getVal();
            AttribVal avWindowNumber = (AttribVal)cie.getAttribVal("windowno");
            if(avWindowNumber.getValAsString().equalsIgnoreCase("0")){
                 this.saveData("", "", "", text, "");     
            }
            else{
                this.saveData("", "", "", "", text);     
            }
           
            this.previousTYPE= ClientInterfaceEventTracker.chatwindow_appendbyotherparticipant;
             
        }
        
        if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring)){
            
           // String sLatest = ((String[])this.allRows.lastElement())[0];
            AttribVal av  = (AttribVal)cie.getAttribVal("text");
            String text ="";
            text = (String)av.getVal();
            //jtf.appendText(text, stringTypedByself);
            if(text.equalsIgnoreCase("\n"))return; //This is because the new turn being sent will automatically be sent as a full turn
             if( this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages ){
                     this.appendDatatoEdits(text);
             }
            
            
             else if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyotherparticipant) || 
               this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyself_clearingtextentry) ||
               this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring)){
                 this.saveData("",text, "", "", "");
            }
            else{
                this.appendDatatoEdits(text);
            }
            this.previousTYPE= ClientInterfaceEventTracker.textentryfield_insertstring;
            
        }
        if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_replacestring)){
            
            Object o = cie.getAttribVal("textbeingdeleted");
            if(o!=null){
                //System.exit(-4234);
                AttribVal av  = (AttribVal)o;
                String textbeingdeleted = (String)av.getVal();
               //jtf.appendText(text, stringTypedByself);
               if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyotherparticipant) || 
                  this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyself_clearingtextentry) ||
                  this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring)){
                      //this.saveData("", textbeingdeleted, "", "");
               }
               else{
                     //this.appendData("", textbeingdeleted, "", "");
            }
            //this.previousTYPE= ClientInterfaceEventTracker.textentryfield_replacestring;  
            }
           
        }
        if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring)){
            
            Object o = cie.getAttribVal("textbeingdeleted");
            String sLatest = ((String[])this.allRows.lastElement())[0];
            Object o2 = cie.getAttribVal("autoclear");
            String value ="";
            if(o2!=null){
                AttribVal av = (AttribVal)o2;
                value = (String)av.getVal();
                
            }

            if(o!=null && value.equalsIgnoreCase("FALSE")){
                //System.exit(-4234);
                AttribVal av  = (AttribVal)o;
                String textbeingdeleted = (String)av.getVal();
               //jtf.appendText(text, stringTypedByself);
                if( this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages ){
                     this.appendDatatoEdits(textbeingdeleted);
                }
                
                else if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyotherparticipant) || 
                  this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyself_clearingtextentry) ||
                  this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring)) {
                      this.saveData( "<<<DELETION<<<",textbeingdeleted,"", "","");
               }
               else{
                    this.appendDatatoEdits(textbeingdeleted);
            }
            this.previousTYPE= ClientInterfaceEventTracker.textentryfield_removestring;  
            }
            
            
           
        }
        
        //This is appending of turn to main chat window
        if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.chatwindow_appendbyself_clearingtextentry)){
            AttribVal av  = (AttribVal)cie.getAttribVal("text");
            String text = (String)av.getVal();
            this.saveData("", "", text, "","");
            //jtf.appendString(text+"\n", 1, this.textSENTBYSELF);
            this.previousTYPE= ClientInterfaceEventTracker.chatwindow_appendbyself_clearingtextentry;
            
        }
        if(cie.isStartOfIsTyping()){
             String text = startOfIsTypingByOther;
             
             //String sLatest = ((String[])this.allRows.lastElement())[0];
             
             
              if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring) ||
                      this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring) ||
                      this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages  ){
                  
                 this.appendDatatoEdits(startOfIsTypingByOther);
            } else{ 
                 this.saveData( "",startOfIsTypingByOther, "", "","");   
                 this.previousTYPE= participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages;
              }
             
           
                 
        }
        else if(cie.isEndOfIsTyping()){
            //String text = endOfIsTypingByOther;
              String sLatest = ((String[])this.allRows.lastElement())[0];
              if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring) ||
                      this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring)  ||
                      this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages ){
                      this.appendDatatoEdits(endOfIsTypingByOther);
            } else{ 
                 this.saveData("",endOfIsTypingByOther,  "", "","");
                  this.previousTYPE= participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages;
              }
            
                  
        }
        
        else if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.mazegameOpenGates)){
           
             // String text = startOfIsTypingByOther;
             
             //String sLatest = ((String[])this.allRows.lastElement())[0];
             
             
              if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring) ||
                      this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring) ||
                      this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages  ){
                  
                 this.appendDatatoEdits(mazegameOpenGates);
            } else{ 
                      this.saveData( "",mazegameOpenGates, "", "","");   
                      this.previousTYPE= participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages;
              }
             
           
                 
        }
        else if(cie.getType().equalsIgnoreCase(ClientInterfaceEventTracker.mazegameCloseGates)){
           
             // String text = startOfIsTypingByOther;
             
             //String sLatest = ((String[])this.allRows.lastElement())[0];
             
             
              if(this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_insertstring) ||
                      this.previousTYPE.equalsIgnoreCase(ClientInterfaceEventTracker.textentryfield_removestring) ||
                      this.previousTYPE== participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages  ){
                  
                 this.appendDatatoEdits(mazegameCloseGates);
            } else{ 
                      this.saveData( "",mazegameCloseGates, "", "","");   
                      this.previousTYPE= participantHasntTypedYetButIsReceivingIsAndIsntTypingMessages;
              }
             
           
                 
        }
        
        
        
        this.jtf.updateDataHasChanged();
       
    }
 
    
    //public final static String mazegameOpenGates = "mgog";
    
    
    
    
    
    
    private void initializeStyles(){
        StyleConstants.setFontFamily(textSENTBYOTHER, "Monospaced");
        StyleConstants.setFontSize(textSENTBYOTHER, 20);
        StyleConstants.setBold(textSENTBYOTHER, true);
        StyleConstants.setItalic(textSENTBYOTHER, true);
        StyleConstants.setForeground(textSENTBYOTHER,Color.BLUE);
        StyleConstants.setStrikeThrough(textSENTBYOTHER, false);
        
         StyleConstants.setFontFamily(textSENTBYSELF, "Monospaced");
        StyleConstants.setFontSize(textSENTBYSELF, 20);
        StyleConstants.setBold(textSENTBYSELF, true);
        StyleConstants.setItalic(textSENTBYSELF, true);
        StyleConstants.setForeground(textSENTBYSELF,Color.ORANGE);
        StyleConstants.setStrikeThrough(textSENTBYSELF, false);
        
        
        
        StyleConstants.setFontFamily(stringTypedByself, "Monospaced");
        StyleConstants.setFontSize(stringTypedByself, 20);
        StyleConstants.setBold(stringTypedByself, true);
        StyleConstants.setItalic(stringTypedByself, true);
        StyleConstants.setForeground(stringTypedByself,Color.GREEN);
        StyleConstants.setStrikeThrough(stringTypedByself, false);
        
        StyleConstants.setFontFamily(stringTypedByselfWhileOtherIsTyping, "Monospaced");
        StyleConstants.setFontSize(stringTypedByselfWhileOtherIsTyping, 20);
        StyleConstants.setBold(stringTypedByselfWhileOtherIsTyping, true);
        StyleConstants.setItalic(stringTypedByselfWhileOtherIsTyping, true);
        StyleConstants.setForeground(stringTypedByselfWhileOtherIsTyping,Color.RED);
        StyleConstants.setUnderline(stringTypedByselfWhileOtherIsTyping, true);
         
         
        

    }
  
     static String startOfIsTypingByOther ="\u2286";
     static String endOfIsTypingByOther =  "\u2287";
     
     static String mazegameOpenGates = "\u24DE";
    static String mazegameCloseGates = "\u24D2";
    
    String previousTYPE ="";
    String mostRecentWrite ="";
    
    
    
    
    
    
    
    
    
    
    
    
    
    public int getRowCount(){
        return this.allRows.size();
    }
    
    
}
