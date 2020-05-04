/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *     Please, whatever you do, don't delete or rewrite any of the existing code in this class....
 * 
 *     But please DO ADD whatever you need!
 * 
 * 
 *  * 
 * 
 * 
 * 
 * 
 * 
 */
package diet.server;

import diet.attribval.AttribVal;
import diet.client.ClientInterfaceEvents.ClientInterfaceEvent;
import diet.client.ClientInterfaceEvents.ClientInterfaceEventTracker;
import diet.client.DocumentChange.DocChange;
import java.io.File;
import java.util.Date;
import java.util.Vector;


import diet.message.*;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.experimentmanager.EMUI;
import diet.server.io.IntelligentIO;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.text.MutableAttributeSet;


/**
 * This is the main server class. Each experiment has an associated Conversation object. The Conversation object
 * acts as the intermediary between the clients. It constantly checks for incoming messages, and relays them to a
 * ConversationController object that determines whether the message should be transformed.
 *
 * The Conversation object contains methods that should be used to send messages to clients.
 *
 * @author user
 */


public class Conversation extends Thread{

    private ConversationHistory cH;
    private DefaultConversationController cC;
    private ConversationUIManager cHistoryUIM;
    private Participants ps;
    private boolean conversationIsActive = true;
    private boolean conversationThreadHasTerminated=false;
    private DocChangesIncoming turnsconstructed = new DocChangesIncoming(this); 
    public IntelligentIO convIO;
  
    private ExperimentManager expManager;
    
   
    static Conversation statC;


    Metrics mm;
    

    
     public Conversation(ExperimentManager expM,String nameOfDefaultConversationController){
         this.expManager=expM;
         mm = new Metrics(this);
         statC=this;
         try{  
             // Class c = Class.forName( "diet.server.ConversationController."+nameOfDefaultConversationController);
              
            //  Class c = Class.forName( "diet.server.ConversationController.DefaultDyadicConversationController");
              
              
              this.doBasicSetup(nameOfDefaultConversationController);
              }catch (Exception e){
                      e.printStackTrace();
                      try{ 
                       Class c = Class.forName( "diet.server.ConversationController.obsoltebucket."+nameOfDefaultConversationController);
                       this.doBasicSetup(nameOfDefaultConversationController);
                      }catch(Exception ee){
                          ee.printStackTrace();
                           
                      } 
              }      
     }
         
 
           
          
            
             
         
     


  

  
    
     ///private void doBasicSetup(){
     ///    doBasicSetup(null);
     ///}
  
  
    /**
     *
     * This method retrieves the individual settings for the Conversation by extracting
     * the {@link diet.parameters.Parameter} objects from {@link diet.parameters.ExperimentSettings}.
     * It instantiates the correct {@link ConversationController} using dynamic class loading if necessary, {@link TaskController} (if any), ensures the data will be saved
     * in a particular directory.
     *
     * <p>After initializing the components, it starts the ConversationController Thread.
     *
     * <p>Editing this method has to be done carefully. The sequence of the objects that are initialized is very
     * important as there are many complex interdependencies.
     *
     */
    private void doBasicSetup(String nameOfConversationController){
        //Editing this method has to be done carefully
        //The sequence of the objects that are initialized
        //Is very important..

        try{
            String cCType = nameOfConversationController.trim();
            System.err.println("THE TRIMMED NAME IS: "+cCType);
            Class c = null;
            if(cCType.contains("diet.server.ConversationController")){
               c=Class.forName(cCType);
            }
            else{
               c = Class.forName( "diet.server.ConversationController."+cCType);
            }
            //DefaultConversationController dcc = (DefaultConversationController)c.newInstance();
            
            Class[] intArgsClass = new Class[] {Conversation.class};
           try{
             // c = Class.forName( "diet.server.ConversationController.DefaultDyadicConversationController");
           }catch(Exception e){
               e.printStackTrace();
           }
            if(c==null){
                System.err.println("IN CONVERSATION - DOBASICSETUP COULD NOT FIND THE CLASS!");
            }
            else{
                System.err.println("IN CONVERSATION - DOBASICSETUP COULD FIND THE CLASS!");
            }
           
            Constructor cons = c.getConstructor(intArgsClass);
            
            if(cons==null){
                System.err.println("CONS IS NULL");
            }
            else{
              
                System.err.println("CONS IS NOT NULL");
            }
            
           
            DefaultConversationController dcc = (DefaultConversationController)cons.newInstance(this);
            
            
            cC=dcc;
                      
           
            
            
            
           }catch(Exception e){
                  System.err.println("COULD NOT FIND AND DYNAMICALLY LOAD CONVERSATION CONTROLLER...trying to load"+nameOfConversationController);
                  e.printStackTrace();
                  InvocationTargetException ite = (InvocationTargetException)e;
                  System.err.println("----------");
                   System.err.println("----------");
                    System.err.println("----------");
                     System.err.println("----------");
                      System.err.println("----------");
                       System.err.println("----------");
                        System.err.println("----------");
                         System.err.println("----------");
                  ite.getTargetException().printStackTrace();
                   System.err.println("---------");
                    System.err.println("----------");
                     System.err.println("----------");
                      System.err.println("----------");
                       System.err.println("----------");
                  if(this.expManager.emui!=null){
                    this.expManager.emui.print("Main","Could not dynamically load "+nameOfConversationController);
                    e.printStackTrace();
                    
                    System.err.println("--------------------------------------------------------------------------");
                    System.err.println("--------------------------------------------------------------------------");
                    System.err.println("--------------------------------------------------------------------------");
                    
                    e.getCause();
                    e.getCause().printStackTrace();

                  }else{
                      System.err.println("Could not dynamically load "+nameOfConversationController);
                      e.printStackTrace();
                  }
           }
         
            
        
        
        
        ////String parserFileLocation = (String)expSettings.getV("Parser file location");
       
        cH= new ConversationHistory(this,  cC.getID(), convIO);
        cHistoryUIM = new ConversationUIManager(cH,this);

        cH.setConversationUIManager(cHistoryUIM);
        ps = new Participants(this);


       
       
        
        this.expManager.connectUIWithExperimentManager(this,cHistoryUIM);
        
        cC.initializePostSetup();
        
    }

    
    
    
    
    
    /**
     * Returns the collection of tables that are associated with this Conversation
     * @return ConversationUIManager
     */
    public ConversationUIManager getCHistoryUIM() {
        return cHistoryUIM;
    }


    

    public synchronized boolean requestPermissionForNewParticipantToBeAddedToConversation(String participantID){
        return cC.requestParticipantJoinConversation(participantID);
    }




    /**
     * Adds participant to the Conversation and sends the participant a MessageClientSetupParameters message with
     * the necessary window and client settings.
     *
     * @param p Participant to be added to the Conversation
     */
    public synchronized boolean addNewParticipant(Participant p){
        
          Vector participants = ps.getAllParticipants();
        
          for(int i=0;i<participants.size();i++){
              Participant pAlreadyLoggedIn = (Participant)participants.elementAt(i);
              if(p.getParticipantID().equals(pAlreadyLoggedIn.getParticipantID())){
                    String messageToDisplay = "There is an error: A participant with a duplicate ParticipantID is logging in:\n"+
                            pAlreadyLoggedIn.getParticipantID()+", "+pAlreadyLoggedIn.getUsername()+" is already logged in\n"+
                            p.getParticipantID()+", "+p.getUsername()+" is about to log in\n"
                            + "THIS SHOULD NOT HAPPEN! PLEASE CHECK THE INSTRUCTIONS GIVEN TO THE PARTICIPANTS!!!\n"
                            + "IF YOU HAVE JUST STARTED AN EXPERIMENT, YOU MUST RESTART THE SERVER AND CLIENTS!!!! DO NOT CONTINUE!\n"
                            + "IF YOU ARE IN THE MIDDLE OF RUNNING AN EXPERIMENT, YOU SHOULD PROBABLY PRESS OK AND HOPE FOR THE BEST!";
                    
                            
                     this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns("ERROR", messageToDisplay);
                    CustomDialog.showDialog(messageToDisplay);
                   
              }
          }



         // if(p.getUsername().equalsIgnoreCase("HELLO"))return false;

        //Possible thread error if this thread sleeps inbetween adding participant and sending the default message
        //As the conversation might send a message before the participant has received the chat window setup.


        //Get the information from the Permissions
        //create the send client setup parameters

        //Ensure that the permissions are set up properly
        //There also has to be a script detailing how any new participant is
        //Dealt with, whether the participant that is newly added will be enabled
        //And who the participant can receive from
        //
        //Ensure that participant being added doesn't have the same name'
      try{
        //if(Debug.debugGROOP4)System.err.println("ADDPARTICIPANTA: "+p.getUsername());
        ps.addNewParticipant(p);
        //Permission perm = ps.getPermissions(Participant p);
        int ownWindowNumber = 0;
        MessageClientSetupParameters mcsp =cC.processRequestForInitialChatToolSettings();
        mcsp.setNewEmailAndUsername(p.getParticipantID(),p.getUsername());
        p.sendMessage(mcsp);
        System.out.println("added new participant "+p.getParticipantID());
        if(this.expManager.emui!=null){
           this.expManager.emui.println("Main", "Participant with email: "+p.getParticipantID()+" and with username: "+p.getUsername()+" has logged in to "+cC.getID()+" there are "+ps.getAllParticipants().size());
        }
      }catch (Exception e){
         System.err.println("Problem adding new participant");
      }
     
      cC.participantJoinedConversation(p);
      
      this.cHistoryUIM.updateParticipantsListChanged(this.getParticipants().getAllParticipants());
      this.mm.updateParticipants(this.getParticipants().getAllParticipants().size());
      
      return true;
      }

    /**
     * This method still needs to be implemented and verified.
     * @param p
     */
    public void reactivateParticipant(Participant p){

       try{
        Conversation.printWSln("Main", "LOGGINGA");
        int ownWindowNumber = 0;
        Conversation.printWSln("Main", "LOGGINGB");
        MessageClientSetupParameters mcsp = cC.processRequestForInitialChatToolSettings();
        Conversation.printWSln("Main", "LOGGINGC");
        mcsp.setNewEmailAndUsername(p.getParticipantID(),p.getUsername());
        Conversation.printWSln("Main", "LOGGINGD");
        p.sendMessage(mcsp);
        Conversation.printWSln("Main", "LOGGINGE");
        cC.participantRejoinedConversation(p);
        CustomDialog.showModelessDialog("Participant ID: "+p.getParticipantID()+" USERNAME"+p.getUsername()+" was reconnected.");
        
        Conversation.printWSln("Main", "LOGGINGF");
        Conversation.printWSln("Main", "Participant "+p.getParticipantID()+" reactivated ");
      }catch (Exception e){
          System.err.println("Problem reactivating participant");
          e.printStackTrace();
      }
   }

    
    
    

    long timeOfLastEvent=-9999;
    public void saveTime(String eventName){
        String description ="";
        if(timeOfLastEvent==-9999) {
            description = eventName + new Date().getTime()+"\n";
            timeOfLastEvent= new Date().getTime();
        }
        else{
            long currentTime = new Date().getTime();
            long timeSinceLast = currentTime - timeOfLastEvent;
            description = timeSinceLast + ": "+eventName+": "+currentTime+"\n";
            timeOfLastEvent=currentTime;
        }
        
        
        convIO.saveTextToFileCreatingIfNecessary(description, "debuggingevents.txt");
    }


     private Message mostRecentMessageReceived = null;

    /**
     * Loop that polls {@link Participants} for any incoming messages. On receiving a message it calls the corresponding
     * methods in {@link diet.server.ConversationController}.
     *
     */
    @Override
    public void run(){
        System.out.println("Starting conversationcontroller");
        while (isConversationActive()){
          try{
             
             if(DefaultConversationController.sett.debug_debugTime)this.saveTime("1");
             mm.registerLoadingNextMessage(mostRecentMessageReceived);
             Message m = (Message) ps.getNextMessage();
             mostRecentMessageReceived =m;
             mm.registerIncomingMessage(mostRecentMessageReceived);
             
             
             if(DefaultConversationController.sett.debug_debugTime)this.saveTime("2");
             if (m!=null){
               if(DefaultConversationController.sett.debug_debugTime)this.saveTime("3"+m.getClass().toString());
               cHistoryUIM.updateControlPanel(m);
               if(DefaultConversationController.sett.debug_debugTime)this.saveTime("4");
               convIO.saveMessage(m);
               if(DefaultConversationController.sett.debug_debugTime)this.saveTime("5");
               //if(m instanceof diet.message.MessageClientInterfaceEvent) 
              
               
               
               
               
               Participant origin = ps.findParticipantWithEmail(m.getEmail());
                if(DefaultConversationController.sett.debug_debugTime)this.saveTime("6");
              
               
               // if(DefaultConversationController.debugMESSAGEBLOCKAGE){System.out.println("MCT66609999999m");System.out.flush();}
               //System.out.println("UPDATINGCONTROLPANEL");

                 if (m instanceof MessageWYSIWYGFloorRequest){
                         MessageWYSIWYGFloorRequest mwysiwygfr = (MessageWYSIWYGFloorRequest)m;
                         cC.processWYSIWYGFloorRequestDEPRECATED(origin, mwysiwygfr );
                         try{
                             
                         }catch(Exception e){
                             printWln("Main","There was an error processing floor request");
                             printWln("Main",e.getMessage());
                             e.printStackTrace();
                             this.saveErrorLog("Error processing floor request");
                             convIO.saveErrorLog(e);  
                         }
                                 
                     }
                
                 if (m instanceof MessageWYSIWYGFloorChangeConfirm){
                         MessageWYSIWYGFloorChangeConfirm mwysiwygfr = (MessageWYSIWYGFloorChangeConfirm)m;
                         cC.processWYSIWYGFloorChangeConfirmDEPRECATED(origin, mwysiwygfr );
                         try{
                             
                         }catch(Exception e){
                             printWln("Main","There was an error processing floor change confirm");
                             printWln("Main",e.getMessage());
                             e.printStackTrace();
                             this.saveErrorLog("Error processing floor change confirm");
                             convIO.saveErrorLog(e);  
                         }
                                 
                     }
                 
                if(m instanceof MessageButtonPressFromClient){
                    MessageButtonPressFromClient mbpfc = (MessageButtonPressFromClient)m; 
                    cC.processButtonPress(origin, mbpfc);
                } 
                 
                
                if (m instanceof MessageChatTextFromClient) {
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("7");
                    MessageChatTextFromClient msctfc = (MessageChatTextFromClient)m;
                    //MessageChatTextToClient msccttc = new MessageChatTextToClient(msctfc.getEmail(),msctfc.getUsername(),0,msctfc.getText(),1);
                    //cHistoryUIM.updateChatToolTextEntryFieldsUI(msctfc);
                    //System.out.println("Received message onset"+msctfc.getTypingOnset()+":"+msctfc.getEndOfTyping());
                   // System.out.println("MCT1");
                    try{
                      cC.processChatText(origin,msctfc);
                    }catch(Exception e){
                         printWln("Main","There was an error processing chattext");
                         printWln("Main",e.getMessage());
                         e.printStackTrace();
                         this.saveErrorLog("Error processing chattext");
                         convIO.saveErrorLog(e);  
                    }
                    
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("8"); 
                    if(m instanceof MessageChatTextFromClient && diet.debug.Debug.debugtimers ){    
                         ((MessageChatTextFromClient)m).saveTime("serverConversation.hasBeenProcessedByConversationController");
                     }
                    
                    
                    
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("9"); 

                    if(!msctfc.hasBeenRelayedByServer){
                         String prefix = "NOTRELAYED";
                         Vector vAdditionalValues = cC.getAdditionalInformationForParticipant(origin);
                         
                        String subdialogueID =  cC.pp.getSubdialogueID(origin);
                         
                         this.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(origin, msctfc, subdialogueID, vAdditionalValues);
                         
                        if(diet.debug.Debug.debugIO){
                            diet.debug.Debug.showDebug2("\n----CONVERSATION:"+ msctfc.getText() +    msctfc.getKeypresses().size());
                           
                        }
                         
                         
                         if(m instanceof MessageChatTextFromClient && diet.debug.Debug.debugtimers ){    
                           ((MessageChatTextFromClient)m).saveTime("serverConversation.hasBeenSavedBecauseNotrelayed");
                         }
                    
                    }
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("10"); 
                    if(diet.debug.Debug.debugtimers){
                        Vector allTimesSaved = msctfc.allTimes;
                        String textToSave = "------------\n";
                        textToSave = textToSave + msctfc.getEmail()+":"+msctfc.getUsername()+"\n"+ msctfc.getText()+"\n";
                        for(int i=0;i<allTimesSaved.size();i++){
                            AttribVal av = (AttribVal)allTimesSaved.elementAt(i);
                            
                            String diffval="";
                            if(i>0) {
                                long prevvalue = (long)((AttribVal)allTimesSaved.elementAt(i-1)).value;
                                long difference = (long)av.value - prevvalue;
                                diffval = difference+"";
                            }  
                            textToSave  =   textToSave+ diffval+":  "+ av.id +": "+ av.getValAsString()+"\n";
                        }     
                        this.convIO.saveTextToFileCreatingIfNecessary(textToSave,"debugoutputA");
                         long prevvalue = (long)((AttribVal)allTimesSaved.elementAt(allTimesSaved.size()-1)).value;
                         long difference = (long)new Date().getTime() - prevvalue;
                         String diffv = ""+difference;
                        
                        
                        
                        textToSave = textToSave + diffv+ "SecondSave: "+new Date().getTime()+"\n";
                        this.convIO.saveTextToFileCreatingIfNecessary(textToSave,"debugoutputB");
                    }
                    
                    
                    
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("11"); 
                    //if(DefaultConversationController.debugMESSAGEBLOCKAGE){System.out.println("MCT2");System.out.flush();}
                    //sendMessageToAllOtherParticipants(p, msccttc);
                }
                else if (m instanceof MessageKeypressed){
                    if(DefaultConversationController.sett.debug_debugTime)this.saveTime("12"); 
                    MessageKeypressed mkp = (MessageKeypressed)m;
                    String txtEntered = mkp.getContentsOfTextEntryWindow();
                    if(txtEntered!=null){
                        if(txtEntered.length()>0){
                           char txtEnteredLastChar = txtEntered.charAt(txtEntered.length()-1);
                          // if(Character.isWhitespace(txtEnteredLastChar))this.cHistoryUIM.parseTreeAndDisplayOfParticipant(origin,txtEntered);
                        }
                    }
                    System.out.println(origin.getParticipantID());
                    System.out.println(mkp.getKeypressed());
                    this.newsaveClientKeypressToFile(origin, mkp);
                    cC.processKeyPress(origin,mkp);

                }
                else if (m instanceof MessageWYSIWYGDocumentSyncFromClientInsert){
                    
                    MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp = (MessageWYSIWYGDocumentSyncFromClientInsert)m;
                    getDocChangesIncoming().addInsert(origin,mWYSIWYGkp.getTextToAppendToWindow(),mWYSIWYGkp.getOffset(),mWYSIWYGkp.getTimeOfReceipt().getTime());
                    cC.processWYSIWYGTextInserted(origin,mWYSIWYGkp);
                    try{
                        String textToappendtoWindow =  mWYSIWYGkp.getTextToAppendToWindow();
                        //System.err.println("TEXTTOAPPEND:"+textToappendtoWindow);
                        //char txtToInsert = mWYSIWYGkp.getTextToAppendToWindow().charAt(mWYSIWYGkp.getTextToAppendToWindow().length()-1);
                        
                    }catch(Exception e){
                        e.printStackTrace();
                        //System.exit(-456);
                    }
                    ///char txtToInsert = mWYSIWYGkp.getTextToAppendToWindow().charAt(mWYSIWYGkp.getTextToAppendToWindow().length()-1);
                    ///this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin);   
                    this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin.getParticipantID(), origin.getUsername(),mWYSIWYGkp.getAllTextInWindow() );
                }
                else if (m instanceof MessageWYSIWYGDocumentSyncFromClientRemove){
                    MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp = (MessageWYSIWYGDocumentSyncFromClientRemove)m;
                    getDocChangesIncoming().addRemove(origin,mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeOfReceipt().getTime());
                    cC.processWYSIWYGTextRemoved(origin,mWYSIWYGkp);
                    //String txtEntered = getDocChangesIncoming().getTurnBeingConstructed(origin).getParsedText();
                    this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin.getParticipantID(), origin.getUsername(),mWYSIWYGkp.getAllTextInWindow() );
                }

               
               
                else if (m instanceof MessageErrorFromClient){
                    MessageErrorFromClient mefc = (MessageErrorFromClient)m;
                    this.printWln("Main", "ERROR IN CLIENT: "+m.getEmail()+" "+m.getUsername()+"\n"+mefc.getErrorMessage());
                    System.err.println("ERROR IN CLIENT: "+m.getEmail()+" "+m.getUsername()+"\n"+mefc.getErrorMessage());
                    Throwable t = mefc.getThrowable();
                    this.saveErrorLog("ERROR IN CLIENT: "+m.getEmail()+" "+m.getUsername()+"\n"+mefc.getErrorMessage());
                    

                }
                else if(m instanceof MessageClientInterfaceEvent){
                    
                    MessageClientInterfaceEvent mcie = (MessageClientInterfaceEvent)m;
                    try{
                    cC.processClientEvent(origin,mcie);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    convIO.saveClientEvent(mcie);
                    String eventtype = mcie.getClientInterfaceEvent().getType();
                    if(eventtype.equalsIgnoreCase("stimulusimage_change_confirm")){
                       long timeOfDisplayOnClient =  mcie.getClientInterfaceEvent().getClientTimeOfDisplay();
                       String name = (String)mcie.getClientInterfaceEvent().getValue("name");
                        
                      
                       this.newsaveAdditionalRowOfDataProducedOnCLIENTToSpreadsheetOfTurns( timeOfDisplayOnClient,"stimulusimage_change_confirm"  , origin, name);     
                    }
                    
                   
                    
                    
                    
                }
                else if (m instanceof MessagePopupResponseFromClient){
                    try{
                       MessagePopupResponseFromClient mpr = (MessagePopupResponseFromClient)m;
                       String[] options = mpr.getOptions();
                       String question = mpr.getQuestion();
                       String title = mpr.getTitle();
                       String optionsFLATTENED = "";
                       for(int l=0;l<options.length;l++){
                           optionsFLATTENED = optionsFLATTENED+options[l];
                       }
                       
                       
                        String participantID = "not_yet_set";
                        String username = "not_yet_set";
                        participantID = mpr.getEmail();
                        username = mpr.getUsername();//origin.getUsername();
                        
                        String popupID=mpr.getPopupID();
                      
                       //System.out.println(id+username+"---------------------------------------------------");
                       //System.exit(-4);
                       
                       String s4 = ""+mpr.getTimeOfReceipt().getTime();
                       String s5 = ""+mpr.getTimeOfReceipt();
                       String s7 = optionsFLATTENED;
                       
                       String timeOnClientOfShowing = "(TimeOnClientOfShowing:"+mpr.timeOnClientOfDisplay+")";
                       String timeOnClientOfChoice = "(TimeOnClientOfSelecting:"+mpr.timeOfChoice+")";
                       
                      // String text = mpr.getTitle()+"_"+mpr.getQuestion()+"_"+mpr.getSelection()+"_"+mpr.getSelectedValue();
                       
                        String textToSave = (title+"/"+question+"/"+optionsFLATTENED).replaceAll("\n","(NEWLINE)")+ timeOnClientOfShowing+timeOnClientOfChoice+"_"+mpr.getSelection()+"__"+mpr.getSelectedValue();
                       
                       this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns("popupreceived_"+popupID, origin, textToSave);
                       
                     
                       
                       cC.processPopupResponse(origin, (MessagePopupResponseFromClient)m);
                        
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    
                }
                
                
                
                
                else if(m instanceof MessageTask){
                   cC.processTaskMove((MessageTask)m, origin);
                }
                else{
                   //System.exit(-23456);
                }
         }

          if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("MCT44");System.out.flush();}
          }catch (Exception e){
               System.err.println(cC.getID()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
               printWln("Main","There is an ERROR in the Conversation Controller");
               printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
               printWln("Main","the saved experimental data");
               printWln("Main",e.getMessage());
                e.printStackTrace();
               convIO.saveErrorLog(e);
              
          }
       if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("MCT45");System.out.flush();}
      }
         if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("MCT46");System.out.flush();}
          System.err.println("THREAD TERMINATED");
          System.err.flush();
          conversationThreadHasTerminated=true;
    }




    
    
   
    public void newsendInstructionToParticipant( Participant recipient, String text, Vector additionalValues){  
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
        MutableAttributeSet style = cC.getStyleManager().getStyleForInstructionMessages(recipient);
        int windowNo = cC.getStyleManager().getWindowNumberInWhichParticipantBReceivesTextFromServer_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(recipient);
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server","",text,windowNo,false,style);
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(),  "server",text, recipientNames, additionalValues, false);
    }
    
    public void newsendInstructionToParticipant( Participant recipient, String text){  
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
         MutableAttributeSet style = cC.getStyleManager().getStyleForInstructionMessages(recipient);
        int windowNo = cC.getStyleManager().getWindowNumberInWhichParticipantBReceivesTextFromServer_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(recipient); 
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server","",text,windowNo,false,style);
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(), "server",text, recipientNames, new Vector(), false);
    }
   
    public void newsendInstructionToMultipleParticipants(Vector recipients, String text){
        for(int i=0;i<recipients.size();i++){
             Participant pRecipient = (Participant )recipients.elementAt(i);
             newsendInstructionToParticipant(pRecipient, text);
        }
    }
    

    public void newsendArtificialTurnFromApparentOrigin( Participant apparentOrigin, Participant recipient, String text){  
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
        MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(apparentOrigin, recipient);
        int windowNo = cC.getStyleManager().getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(apparentOrigin, recipient);
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOrigin.getUsername(),text,windowNo,true,style);
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(apparentOrigin);
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
           
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(),   apparentOrigin.getUsername(),text, recipientNames, additionalInfo, false);
    }
    
    
    
    public void newsendArtificialTurnFromApparentOrigin( String apparentOriginUsername, Participant recipient, String text){  
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
        MutableAttributeSet style = cC.getStyleManager().getStyleFoOTHER1();
        int windowNo = 0;
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOriginUsername,text,windowNo,true,style);
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(null);
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
             if(additionalInfo==null)additionalInfo = new Vector();
        }
           
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(),   apparentOriginUsername,text, recipientNames, additionalInfo, false);
    }
    
    
    
    
    
    
   
    
    
      public void newsendArtificialTurnFromApparentOrigin( Participant apparentOrigin, Participant recipient, String text, int windowNo, Vector additionalValues){  
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
        MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(apparentOrigin, recipient);     
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOrigin.getUsername(),text,windowNo,true,style);
        mctc.setPrefixUsername(false);
        
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        if(additionalValues == null)additionalValues = new Vector();
        String textWithNoEnters = text.replace("\n", DefaultConversationController.sett.recordeddata_newlinestring).replace("\r", DefaultConversationController.sett.recordeddata_newlinestring);
        
        
        
        //String textWithNoEnters ="-------------";
        
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(),   apparentOrigin.getUsername(),textWithNoEnters, recipientNames, additionalValues, false);
    }
    
    
    
   

   public void newsendArtificialTurnFromApparentOriginToRecipient( String apparentOriginUsername, Participant recipient, boolean prefixUsername, String text,  String subdialogueID, MutableAttributeSet style, int windowNo, Vector additionalValues){  
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOriginUsername,text,windowNo,true,style);
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        
        
        Vector additionalInfo = new Vector();
        try{
           Participant pApparentOriginUsername = ps.findParticipantWithUsername(apparentOriginUsername);
           if(pApparentOriginUsername!=null){
               additionalInfo = cC.getAdditionalInformationForParticipant(pApparentOriginUsername);           
           }
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(), apparentOriginUsername,text, recipientNames, additionalInfo, false);
    }
   
   public void newsendArtificialTurnFromApparentOriginToMultipleRecipients(String apparentOriginUsername, String subdialogueID ,Vector recipients, String text,   MutableAttributeSet style, int windowNo, Vector additionalValues){  
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOriginUsername,text,windowNo,true,style);
        Vector recipientNames = new Vector();
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            recipientNames.addElement(recipient.getUsername());
            ps.sendMessageToParticipant(recipient,mctc);        
        }
        
        Vector additionalInfo = new Vector();
        try{
           Participant pApparentOriginUsername = ps.findParticipantWithUsername(apparentOriginUsername);
           if(pApparentOriginUsername!=null){
               additionalInfo = cC.getAdditionalInformationForParticipant(pApparentOriginUsername);           
           }
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(), apparentOriginUsername,text, recipientNames, additionalInfo, false);
    }
    
   
   public void newsendArtificialTurnFromApparentOriginToMultipleRecipients(Participant apparentOrigin, Vector recipients, String text   ){   
       
       
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(apparentOrigin); 
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
       
       
       
        for(int i=0;i<recipients.size();i++){
            Participant recipient = this.getParticipants().findParticipantWithUsername((String) recipients.elementAt(i));
            String subdialogueID = cC.pp.getSubdialogueID(recipient);
            MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(apparentOrigin, recipient);
            int windowNo = cC.getStyleManager().getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(apparentOrigin, recipient);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),"server",apparentOrigin.getUsername(),text,windowNo,true,style); 
             Vector recipientName = new Vector();
            recipientName.addElement(recipient.getUsername());
            ps.sendMessageToParticipant(recipient,mctc);        
            cH.saveArtificialMessageCreatedByServer(subdialogueID, mctc.getTimeOfSending().getTime(), apparentOrigin.getUsername(),text, recipientName, additionalInfo, false);
   
            }
         }
   
   
   
    public void deprecated_sendArtificialTurnToAllParticipants(String text, int windowNo){   ////This is what it calls
          Vector v = ps.getAllParticipants();
          MutableAttributeSet style = cC.getStyleManager().getDefaultStyleForInstructionMessages();
          this.newsendArtificialTurnFromApparentOriginToMultipleRecipients("", "", v, text, null, windowNo, v);
     }
     
   
   
    
    public void deprecated_sendArtificialTurnFromApparentOriginToRecipient(Participant apparentOrigin,Participant recipient, String text){
        MutableAttributeSet style = cC.getStyleManager().getStyleForInstructionMessages(recipient);
        newsendArtificialTurnFromApparentOriginToRecipient(apparentOrigin.getUsername(), recipient, true,text, "", style,0, new Vector() );
    }
   

    
     public void deprecated_sendArtificialTurnToRecipient(Participant recipient,String text, int windowNo){
         MutableAttributeSet style = cC.getStyleManager().getStyleForInstructionMessages(recipient);
         newsendArtificialTurnFromApparentOriginToRecipient("", recipient, false,text, "", style,windowNo, new Vector() );
         
     }

     
     public void deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(Participant recipient,String text, int windowNo,  MutableAttributeSet style){
          deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(recipient, text, windowNo, "",style);
     }
     public void deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(Participant recipient,String text, int windowNo,  MutableAttributeSet style, String cvsPREFIX){
          deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(recipient, text, windowNo, cvsPREFIX,style);
     }

    public void deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(Participant recipient,String text, int windowNo, String prefix,  MutableAttributeSet style ){
        newsendArtificialTurnFromApparentOriginToRecipient("", recipient, false,text, "", style,windowNo, new Vector() );
     }    
     









//
    
    
     

 
    public void deprecated_sendArtificialTurnToRecipient(Participant recipient,String text, int windowNo, String prefixFORCSVSpreadsheetIDENTIFIER ){
        MutableAttributeSet style = cC.getStyleManager().getStyleForInstructionMessages(recipient);
        newsendArtificialTurnFromApparentOriginToRecipient("", recipient, false,text, "", style,windowNo, new Vector() );   
    }
    
    
     public void deprecated_sendArtificialMazeGameTurnFromApparentOriginToRecipientWithEnforcedTextColour(String apparentOriginUsername, Participant recipient,String text, int windowNo, String prefixFORCSVSpreadsheetIDENTIFIER,  MutableAttributeSet style ){
         newsendArtificialTurnFromApparentOriginToRecipient("", recipient, false,text, "", style,windowNo, new Vector() );   
     }
    
    
    public void deprecated_sendArtificialTurnToRecipient(Participant recipient,String text, int windowNo,  MutableAttributeSet style, String prefixFORCSVSpreadsheetIDENTIFIER ){
         newsendArtificialTurnFromApparentOriginToRecipient("", recipient, false,text, "", style,windowNo, new Vector() );   
    }
    
   
    
     
    
    public void showPopupOnClientQueryInfo(String popupID,Participant recipient, String question, String[] options, String title, int selection){
        MessagePopup mp = new MessagePopup (popupID,"server", "server", question, options, title, selection);
        ps.sendMessageToParticipant(recipient,mp);
        
        String questionCleaned = question.replaceAll("\n", "(NEWLINE)");
        String titleCleaned = title.replaceAll("\n", "(NEWLINE)");
        String subdialogueID = cC.pp.getSubdialogueID(recipient);
        
        this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(subdialogueID, "popupsent", recipient.getParticipantID(), recipient.getUsername(), new Date().getTime() ,new Date().getTime(), new Date().getTime(),titleCleaned+"-"+questionCleaned, new Vector());
        //this.saveDataToFile(title, title, question, selection, selection, title, null);
    }
    
    
    
    
    public void deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(Participant recipient, String subdialogueID, String data){
        this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns(subdialogueID,"data"   , "Server", "server", recipient.getParticipantID()+"."+recipient.getUsername(),new Date().getTime() ,new Date().getTime(), new Date().getTime(), new Vector(), data, new Vector());
 
        //this.saveDataToFile(title, title, question, selection, selection, title, null);
    }
    
    public void deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(Participant sender,String recipientName , long timeCREATEDONCLIENT, String subdialogueID, String data){
        //String recipient
        this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns(subdialogueID, "data",  sender.getParticipantID(), sender.getUsername(), recipientName, timeCREATEDONCLIENT,new Date().getTime(), new Date().getTime(), new Vector()  ,data,  new Vector());
 
       
    }
    
    
    public void deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(String subDialogueID, String data){    
        long timeOfCreationOnServer = new Date().getTime();
        Conversation.this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns(subDialogueID, "data", "server", "server", "server",timeOfCreationOnServer ,timeOfCreationOnServer, timeOfCreationOnServer, new Vector(),data, new Vector());
        
    }
    
  
     public void deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(String subdialogue, String datatype, String senderID, String senderUsername, long timeOfCreationOnClient,long timeOfSendOnClient, long timeOfRELAYONSERVER, String text, Vector additionalData){
        try{
            newsaveAdditionalRowOfDataToSpreadsheetOfTurns( subdialogue, datatype,  senderID, senderUsername, "", timeOfCreationOnClient , timeOfSendOnClient, timeOfRELAYONSERVER, new Vector(), text, additionalData);
        
         }catch (Exception e){
            Conversation.printWSln("Main", "Error saving data");
        }
    }
    
    
    
    public void newsaveAdditionalRowOfDataToSpreadsheetOfTurns(String subdialogueID, String datatype,  String senderID, String senderUsername, String apparentSenderUsername,long timeOfCreationOnClient ,long timeOfSendOnClient, long timeOfRELAYONSERVER, Vector recipientsNames, String text, Vector<AttribVal> additionalData){
        try{
           //System.err.println("SL02");
           cH.saveDataAsRowInSpreadsheetOfTurns(subdialogueID, datatype,timeOfCreationOnClient ,timeOfSendOnClient, timeOfRELAYONSERVER ,senderID, senderUsername, apparentSenderUsername,text.replaceAll("\n", "(NEWLINE)"),recipientsNames,false,new Vector<Keypress>(), new Vector<DocChange>()  ,new Vector<ClientInterfaceEvent>() , additionalData, false);
             }catch (Exception e){
                e.printStackTrace();
        }
    }
    
    public void newsaveAdditionalRowOfDataProducedOnCLIENTToSpreadsheetOfTurns(long timeOnClient, String datatype,  Participant p, String value){
        try{
           String subDialogueID = cC.pp.getSubdialogueID(p);
           long timeOfCreationOnClient = timeOnClient; 
           long timeOfSendOnClient = timeOfCreationOnClient;
           long timeOfRELAYONSERVER = new Date().getTime();
           String senderID = p.getParticipantID();
           String senderUsername = p.getUsername();
           String recipient = p.getUsername();
           String text = value;
           
           Vector<AttribVal> additionalInfo = new Vector();
           try{
             additionalInfo = cC.getAdditionalInformationForParticipant(p);
             if(additionalInfo==null)additionalInfo = new Vector();
           }catch(Exception e){
              e.printStackTrace();
           }
           
           
           
           cH.saveDataAsRowInSpreadsheetOfTurns(subDialogueID, datatype,timeOfCreationOnClient ,timeOfSendOnClient, timeOfRELAYONSERVER ,senderID, senderUsername, recipient,text.replaceAll("\n", "(NEWLINE)"),new Vector(),false,new Vector<Keypress>(), new Vector<DocChange>()  ,new Vector<ClientInterfaceEvent>() , additionalInfo, false);
           }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    
    public void newsaveAdditionalRowOfDataToSpreadsheetOfTurns(String datatype,  Participant p, String value, long timeOfCreationOnClient){
        try{
           String subDialogueID = cC.pp.getSubdialogueID(p);
           //long timeOfCreationOnClient = new Date().getTime(); 
           long timeOfSendOnClient = timeOfCreationOnClient;
           long timeOfRELAYONSERVER = new Date().getTime();
           String senderID = p.getParticipantID();
           String senderUsername = p.getUsername();
           String recipient = p.getUsername();
           String text = value;
           
           Vector<AttribVal> additionalInfo = new Vector();
           try{
             additionalInfo = cC.getAdditionalInformationForParticipant(p);
             if(additionalInfo==null)additionalInfo = new Vector();
           }catch(Exception e){
              e.printStackTrace();
           }
           
           
           cH.saveDataAsRowInSpreadsheetOfTurns(subDialogueID, datatype,timeOfCreationOnClient ,timeOfSendOnClient, timeOfRELAYONSERVER ,senderID, senderUsername, recipient,text.replaceAll("\n", "(NEWLINE)"),new Vector(),false,new Vector<Keypress>(), new Vector<DocChange>()  ,new Vector<ClientInterfaceEvent>() , additionalInfo, false);
           }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void newsaveAdditionalRowOfDataToSpreadsheetOfTurns(String datatype,  Participant p, String value){
        if(value==null)value ="";
        newsaveAdditionalRowOfDataToSpreadsheetOfTurns(datatype,  p,  value, new Vector());
    }
    public void newsaveAdditionalRowOfDataToSpreadsheetOfTurns(String datatype,  Participant p, String value, Vector additionalAttribVals){
        try{
           if (value==null)value="";
           String subDialogueID = cC.pp.getSubdialogueID(p);
           long currentTIME = new Date().getTime(); 
           long timeOfSendOnClient = currentTIME;
           long timeOfRELAYONSERVER = currentTIME;
           String senderID = p.getParticipantID();
           String senderUsername = p.getUsername();
           String recipient = p.getUsername();
           String text = value;
           
           Vector additionalInfo = new Vector();
           additionalInfo.addElement(""); 
           try{
             additionalInfo = cC.getAdditionalInformationForParticipant(p);
             if(additionalInfo==null)additionalInfo = new Vector();
           }catch(Exception e){
              e.printStackTrace();
           }
           if(additionalAttribVals!=null & additionalAttribVals.size()>0){
               additionalInfo.addAll(additionalAttribVals);
           }
           
           cH.saveDataAsRowInSpreadsheetOfTurns(subDialogueID, datatype,currentTIME ,timeOfSendOnClient, timeOfRELAYONSERVER ,senderID, senderUsername, recipient,text.replaceAll("\n", "(NEWLINE)"),new Vector(),false,new Vector<Keypress>(), new Vector<DocChange>()  ,new Vector<ClientInterfaceEvent>() , additionalInfo, false);
           }catch (Exception e){
            e.printStackTrace();
        }
    }
   

    
    public void newsaveClientKeypressToFile(Participant sender, MessageKeypressed mkp){
        String subDialogueID = cC.pp.getSubdialogueID(sender);
        if (subDialogueID ==null) subDialogueID = "";
        this.newsaveClientKeypressToFile(sender, mkp, "");
    }

    
    public void newsaveClientKeypressToFile(Participant sender, MessageKeypressed mkp,String subDialogueID ){
        convIO.saveClientKeypressFromClient(mkp, subDialogueID, subDialogueID);
    }

    //PriorTurnByOther_TimestampOnClientOfReceipt	PriorTurnByOther_ApparentUsername	PriorTurnByOther_Text

       
    
    
    public void newsaveClientDocumentchangeToFile(Participant sender,long timeOfCreationOnClient ,long timestamp, String contentsOfWindow){
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        convIO.saveClientDocumentChange(cC.getID(), subdialogueID, sender, timeOfCreationOnClient, timestamp, contentsOfWindow);
    }

   

    



    
     
    
     
      public void newrelayTurnToParticipant(Participant sender,Participant recipient, MessageChatTextFromClient mct,  MutableAttributeSet style, int windowNumber, String subdialogueID, Vector additionalValues){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
        //Debug.showDebug(sender.getUsername()+": "+ds.getAllInsertsAndRemoves().size());
        
        int window =0;
        Vector pUsernames = new Vector();
        pUsernames.addElement(recipient.getUsername());       
        MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
        ps.sendMessageToParticipant(recipient, mctc);
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(), mct.getClientInterfaceEvents(),   additionalValues,false);
  
      }
      
      public void newrelayTurnToMultipleParticipants(Participant sender,Vector recipients, MessageChatTextFromClient mct,   int windowNumber, String subdialogueID, Vector additionalValues){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
       
        
        Vector pUsernames = new Vector();
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            pUsernames.addElement(recipient.getUsername());  
             MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(),mct.getClientInterfaceEvents(),    additionalValues,false);
      }
      
      
      public void newrelayTurnToMultipleParticipants(Participant sender,Vector recipients, MessageChatTextFromClient mct, Vector additionalValues){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
      
        
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        Vector pUsernames = new Vector();
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            int windowNumber = cC.sm.getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(sender, recipient);
            pUsernames.addElement(recipient.getUsername());  
            MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(),   mct.getClientInterfaceEvents(), additionalValues,false);
      }
      
      public void newrelayTurnToPermittedParticipants(Participant sender, MessageChatTextFromClient mct, Vector additionalValues){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());   
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        Vector pUsernames = new Vector();
        Vector recipients = cC.pp.getRecipients(sender);
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            int windowNumber = cC.sm.getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(sender, recipient);
            pUsernames.addElement(recipient.getUsername());  
            MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
             if( diet.debug.Debug.debugtimers ) mct.saveTime("ServerConversation.newrelayTurnToPermittedParticipantsSENDING:"+recipient.getParticipantID()+","+recipient.getUsername());
              
            ps.sendMessageToParticipant(recipient, mctc);
            if( diet.debug.Debug.debugtimers ) mct.saveTime("ServerConversation.newrelayTurnToPermittedParticipantsSENT:"+recipient.getParticipantID()+","+recipient.getUsername());
            
        }
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(), mct.getClientInterfaceEvents(), additionalValues,false);
        if( diet.debug.Debug.debugtimers ) mct.saveTime("ServerConversation.newrelayTurnToPermittedParticipantsSAVED:");
            
      }
      
      
      public void newrelayTurnToPermittedParticipantsWithSpoofUsername(Participant sender, MessageChatTextFromClient mct, String spoofUsername){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(sender);
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        Vector pUsernames = new Vector();
        Vector recipients = cC.pp.getRecipients(sender);
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            int windowNumber = cC.sm.getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(sender, recipient);
            pUsernames.addElement(recipient.getUsername());  
            MutableAttributeSet style = cC.getStyleManager().getStyleFoOTHER1();
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),spoofUsername,mct.getText(),windowNumber,true,style);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),spoofUsername,mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(), mct.getClientInterfaceEvents(),  additionalInfo,false);
      }
    
      
      public void newrelayTurnToPermittedParticipants(Participant sender, MessageChatTextFromClient mct){
        mct.setChatTextHasBeenRelayedByServer();
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(sender);
           if(additionalInfo==null)additionalInfo = new Vector();
        }catch(Exception e){
            e.printStackTrace();
        }
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        Vector pUsernames = new Vector();
        Vector recipients = cC.pp.getRecipients(sender);
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            int windowNumber = cC.sm.getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(sender, recipient);
            pUsernames.addElement(recipient.getUsername());  
            MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(), mct.getClientInterfaceEvents(),  additionalInfo,false);
      }
      
      
     
      
      
      
      
     public void newRemoveAllDelayedMessages(){
         this.ps.removeAllDelayedMessages();
     }
     
      
      
      long dbgcounterA=0;
      
     
      public void newDelayedRelayTurnToPermittedParticipants(Participant sender, MessageChatTextFromClient mct, long delay){
        
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        
        Vector additionalInfo = new Vector();
        try{
           additionalInfo = cC.getAdditionalInformationForParticipant(sender);
           if(additionalInfo==null)additionalInfo = new Vector();
           additionalInfo = (Vector) additionalInfo.clone();
        }catch(Exception e){
            e.printStackTrace();
        }
        String subdialogueID = cC.pp.getSubdialogueID(sender);
        Vector pUsernames = new Vector();
        Vector recipients = cC.pp.getRecipients(sender);
        AttribVal av2 = new AttribVal("actualdelay",delay);
        additionalInfo.addElement(av2);
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            int windowNumber = cC.sm.getWindowNumberInWhichParticipantBReceivesTextFromParticipantA_Deprecated_SHOULDBEMOVEDTOATRACKORWINDOWMANAGER(sender, recipient);
            pUsernames.addElement(recipient.getUsername());  
            MutableAttributeSet style = cC.getStyleManager().getStyleForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(generateNextIDForClientDisplayConfirm(),sender.getParticipantID(),sender.getUsername(),mct.getText(),windowNumber,true,style);
            ps.sendDelayedMessage(mctc, recipient, delay);   
            Vector uniquerecipient = new Vector(); uniquerecipient.add(recipient.getUsername());
            cH.saveArtificialMessageCreatedByServer(subdialogueID, mct.getTimeOfSending().getTime(),   sender.getUsername(),mct.getText(), uniquerecipient, additionalInfo, false);
       
        }
        dbgcounterA++;
        //cH.saveMessageRelayedToOthers(subdialogueID,mct.getStartOfTypingOnClient(),  mct.getTimeOfSending().getTime(),mct.getTimeOfReceipt().getTime(),sender.getParticipantID(), sender.getUsername(),sender.getUsername(),mct.getText(),pUsernames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(), mct.getClientInterfaceEvents(),  additionalInfo,false);
      }
      
      
      
      
      
      
      
      
    
    
   
    
    
    
    
    
    /**
     * Relays turn from clients to all permitted participants. This should be the default method invoked to relay turns that are
     * not modified / blocked. It ensures that the message is stored and displayed properly in the UI.
     * The prefixFORCSVSpreadsheetIDENTIFIER is so that you can add intervention-specific info to the spreadsheet output..
     * 
     *
     * @param sender
     * @param mct
     */
    public void deprecated_relayTurnToAllOtherParticipants(Participant sender,MessageChatTextFromClient mct, Vector recipients){
         mct.setChatTextHasBeenRelayedByServer(); 
         Conversation.this.deprecated_relayTurnToAllOtherParticipants(sender,mct,"", recipients);
    }
    
    
    

    /**
     * Relays turn from clients to all permitted participants. This should be the default method invoked to relay turns that are
     * not modified / blocked. It ensures that the message is stored and displayed properly in the UI.
     * The prefixFORCSVSpreadsheetIDENTIFIER is so that you can add intervention-specific info to the spreadsheet output..
     * 
     *
     * @param sender
     * @param mct
     * @param subdialogueID
     */
    public void deprecated_relayTurnToAllOtherParticipants(Participant sender,MessageChatTextFromClient mct, String subdialogueID, Vector recipientsOfChatText){
        mct.setChatTextHasBeenRelayedByServer();
        this.newrelayTurnToMultipleParticipants(sender, recipientsOfChatText, mct, 0, subdialogueID, recipientsOfChatText);

       }
    
    
    
    
    

    
    
  
    
    
     
    

    
    
   



    
    
    


   

   
    
    
    
    
    
    
    
    
    
     
    
    

  

    
    
    
    



   

    

    


   
    
    

    



    

    
    

     public void changeClientInterface_clearTextEntryField(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.clearTextEntryField);
           ps.sendMessageToParticipant(recipient, mccip); 
    }

    public void changeClientInterface_clearMainWindows(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.clearMainTextWindows);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_clearMainWindowsExceptWindow0(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.clearAllWindowsExceptWindow0);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    
    public void changeClientInterface_enableTextEntry(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.enableTextEntry);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_setMaxTextEntryCharLength(Participant recipient, int charlength){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.setCharLengthLimit, charlength);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_disableTextEntry(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.disableTextEntry);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    public void changeClientInterface_enableTextDisplay(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.enableTextPane);
           ps.sendMessageToParticipant(recipient, mccip);
    }
     public void changeClientInterface_disableTextDisplay(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.disableTextPane);
           System.err.println("BLOCKING CONVERSATION HISTORY! (3A)");
           ps.sendMessageToParticipant(recipient, mccip);
           System.err.println("BLOCKING CONVERSATION HISTORY! (3B)");
    }
     public void changeClientInterface_disableScrolling(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.disableScrolling);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    public void changeClientInterface_enableScrolling(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.enableScrolling);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    public void changeClientInterface_backgroundColour(Participant recipient, Color newColor){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.changeScreenBackgroundColour, newColor);
           ps.sendMessageToParticipant(recipient, mccip);
            //System.exit(-44444678);
    }
    public void changeClientInterface_doRobotDebug(Participant recipient,  String nameOfDebugScenario, long duration){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.dodebugrobot, nameOfDebugScenario, duration);
           ps.sendMessageToParticipant(recipient, mccip);
            //System.exit(-44444678);
    }
    
    
    
    public void openClientBrowserToWebpage(Participant pRecipient, String webpage){
        MessageOpenClientBrowserWebpage mocbw=new MessageOpenClientBrowserWebpage(webpage);
        
        ps.sendMessageToParticipant(pRecipient, mocbw);
        newsaveAdditionalRowOfDataToSpreadsheetOfTurns("openbrowser",  pRecipient, webpage);
        
    }
    
    
   
    
    
    public void changeClientInterface_EnableDeletes(Participant recipient){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.enableDeletes);
          ps.sendMessageToParticipant(recipient, mccip);
    }
     public void changeClientInterface_DisableDeletes(Participant recipient){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.disableDeletes);
          ps.sendMessageToParticipant(recipient, mccip);
    }
            
            
    public void changeClientInterface_DisplayTextInMazeGameWindow(Participant recipient,String text, long lengthOfTime){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.changeMazeWindow, text, lengthOfTime);
          ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_changeBorderOnChatFrame(Participant recipient,int width, Color colour){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.changeBorderOfChatFrame, width, colour);
         ps.sendMessageToParticipant(recipient, mccip);
         
    }
    
    public void changeClientInterface_changeBorderOnMazeFrame(Participant recipient,int width, Color colour){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(),ClientInterfaceEventTracker.changeBorderOfMazeFrame, width, colour);
         ps.sendMessageToParticipant(recipient, mccip);
         
    }
    
    
    
     
     
    
    
   
    
    
    //Important - you probably shouldn't be calling this method manually. It is called automatically from the IsTyping controller
    // public void changeClientInterface_changeTypingIndicator_IsTyping(Participant recipient, String text){
    //     String id = generateNextIDForClientDisplayConfirm() + "it";  //"istyping"
    //     MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(id,ClientInterfaceEventTracker.displayTextInStatusBar, text);
    //     ps.sendMessageToParticipant(recipient, mccip);
         
   // }
     
    //Important - you probably shouldn't be calling this method manually. It is called automatically from the IsTyping controller
   // public void changeClientInterface_changeTypingIndicator_NOTTyping(Participant recipient, String text){
   //      String id = generateNextIDForClientDisplayConfirm() + "nt"; //"not typing"
   //      MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(id,ClientInterfaceEventTracker.displayTextInStatusBar, text);
   //      ps.sendMessageToParticipant(recipient, mccip);
         
   // }
    
    
    
    
    
    public void typingactivity_GenerateFakeTyping(Participant p){
        cC.itnt.addSpoofTypingInfo(p, new Date().getTime());
    }
    
    
            
    
    //THe functionality below has been deprecated
    /*
    public void changeClientInterface_displayTextOnStatusBarAndDisableWindow(Participant recipient, String text){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(), ClientInterfaceEventTracker.displaytextInStatusBarANDDisableTextentry, text);
         ps.sendMessageToParticipant(recipient, mccip);    
    }
    
    public void changeClientInterface_displayTextOnStatusBarAndEnableWindow(Participant recipient, String text){
         MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(generateNextIDForClientDisplayConfirm(), ClientInterfaceEventTracker.displaytextInStatusBarANDEnableTextentry, text);
         ps.sendMessageToParticipant(recipient, mccip);    
    }
    */
    
   public String generateNextIDForClientDisplayConfirm(){
       this.currentDisplayableID = currentDisplayableID +1;
       return currentDisplayableID+"";  
   }
   long currentDisplayableID =0; 
    
    
    
    
    
    
    
    public void doCountdownToNextPartnerDUTCH(Participant a, Participant b,int steps, String message, String pleasestartmessage, String cvsPREFIX){
         for(int k=steps;k>0;k--){
            try{
                this.changeClientInterface_backgroundColour(a, Color.red);
                this.changeClientInterface_clearMainWindows(a);
                changeClientInterface_DisplayTextInMazeGameWindow(a,message+ " over "+k+" seconden",-1000); 
                this.changeClientInterface_backgroundColour(b, Color.red);
                this.changeClientInterface_clearMainWindows(b);
                changeClientInterface_DisplayTextInMazeGameWindow(b,message+ " over "+k+" seconden",-1000); 
                this.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(a, message+" over "+k+" seconden", 0,cC.getStyleManager().defaultFONTSETTINGSSERVER,cvsPREFIX);
                this.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(b, message+" over "+k+" seconden", 0,cC.getStyleManager().defaultFONTSETTINGSSERVER,cvsPREFIX);
                Thread.sleep(500);
                this.changeClientInterface_backgroundColour(a, Color.white);
                this.changeClientInterface_backgroundColour(b, Color.white);
                Thread.sleep(500);
                this.changeClientInterface_clearMainWindows(a);
                this.changeClientInterface_clearMainWindows(b);
                this.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(a, pleasestartmessage, 0,cC.getStyleManager().defaultFONTSETTINGSSERVER,cvsPREFIX);
                this.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(b,pleasestartmessage, 0,cC.getStyleManager().defaultFONTSETTINGSSERVER,cvsPREFIX);
                
            }catch (Exception e){
                e.printStackTrace();
            }
         }  
    }
    
    
    
    public void doCountdownToNextPartnerSendToAll(int steps, String message){
        
         Vector v = this.getParticipants().getAllParticipants();
        for(int k=steps;k>0;k--){
            try{
               
               
                for(int i=0;i<v.size();i++){
                    Participant p = (Participant)v.elementAt(i);
                    this.changeClientInterface_backgroundColour(p, Color.red);
                    this.changeClientInterface_clearMainWindows(p);
                    changeClientInterface_DisplayTextInMazeGameWindow(p,message+ " IN "+k+" secs",-1000);  
                }
                  this.deprecated_sendArtificialTurnToAllParticipants(message+" in "+k+" secs",0);
                 
                  
                Thread.sleep(500);
                for(int i=0;i<v.size();i++){
                    Participant p = (Participant)v.elementAt(i);
                    
                    this.changeClientInterface_backgroundColour(p, Color.white);
                }
                
                
                Thread.sleep(500);
                
             }catch (Exception e){
                 e.printStackTrace();
             }   
        }  
        for(int i=0;i<v.size();i++){
                    Participant p = (Participant)v.elementAt(i);
                    this.changeClientInterface_clearMainWindows(p);
                   
        }
         this.deprecated_sendArtificialTurnToAllParticipants("Please start",0);
        
        
    } 
    
    
   

    
    

  

   

    

    
    
     
     
    /**
      *
      * Saves the turn that is not being relayed to the log files. It also resets the queue of incoming messages associated with the participant.
      * There is an additional 
      *
      * @param p
      * @param turnNotRelayed
      * @param prefixFORCSVSpreadsheetIDENTIFIER
      * 
      */
     public void setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(Participant p,MessageChatTextFromClient turnNotRelayed, String subdialogueID, Vector additionalValues){
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,new Date().getTime());
        
        cH.saveInterceptedNonRelayedMessage(subdialogueID, turnNotRelayed.getStartOfTypingOnClient(), turnNotRelayed.getTimeOfSending().getTime(), turnNotRelayed.getTimeOfReceipt().getTime(), 
                p.getParticipantID(), p.getUsername(), "", turnNotRelayed.getText(), new Vector(), turnNotRelayed.isHasBeenBlocked(), turnNotRelayed.getKeypresses(), ds.getAllInsertsAndRemoves(),turnNotRelayed.getClientInterfaceEvents() ,additionalValues,false);
			
            
     } 
     
     
     //saveInterceptedNonRelayedMessage(String dtype,long timeOnClientOfStartTyping, long timeOnClientOfSending, long timeOnServerOfReceipt, String senderID, String senderUsername, String apparentSenderUsername, String text,
	//		Vector recipientsNames, boolean wasBlocked, Vector keyPresses, Vector documentUpdates, Vector additionalValues, boolean dummy) {
            
     
     
     
     
     
       

    

     
    


   
    
   
    
    public void deprecated_relayMazeGameTurnToParticipantWithEnforcedColour(Participant sender,Participant recipient,  MessageChatTextFromClient mct,int mazeNo,int moveNo,int indivMveNo,  MutableAttributeSet style){
        Vector  additionalValues = new Vector();
        AttribVal av1 = new AttribVal("mazeno",mazeNo);
        AttribVal av2= new AttribVal("moveno",moveNo);
        AttribVal av3= new AttribVal("indivmveno",indivMveNo);
        additionalValues.addElement(av1);
        additionalValues.addElement(av2);
        additionalValues.addElement(av3);        
        newrelayTurnToParticipant( sender, recipient,  mct,  style,  0, "",  additionalValues);
    
    }
    
    
     public void deprecated_relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(Participant sender,Participant recipient, int windowNo, MessageChatTextFromClient mct,int mazeNo,int moveNo,int indivMveNo,  MutableAttributeSet style, String cvsPREFIX){
         Vector  additionalValues = new Vector();
        AttribVal av1 = new AttribVal("mazeno",mazeNo);
        AttribVal av2= new AttribVal("moveno",moveNo);
        AttribVal av3= new AttribVal("indivmveno",indivMveNo);
        additionalValues.addElement(av1);
        additionalValues.addElement(av2);
        additionalValues.addElement(av3);        
        newrelayTurnToParticipant( sender, recipient,  mct,  style,  windowNo, "",  additionalValues);
     }
    
    

    public void gridImageStimuli_ChangeSelection(Participant recipient, long serverID, String imageName, Color innermostC, Color innerC, String prefixFORCSVSpreadsheet){
        MessageGridImageStimuliSelectionToClient mgsstc= new MessageGridImageStimuliSelectionToClient("server", "server", serverID, imageName,  innermostC,  innerC);
        ps.sendMessageToParticipant(recipient, mgsstc); 
        this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"GRIDSTIMULISET:CHANGING "+imageName+" to RGB: "+innermostC.getRed()+","+innermostC.getGreen()+", ("+innerC.getBlue()+" "+innerC.getRed()+","+innerC.getGreen()+","+innerC.getBlue()+")", ""); 
    }
     public void gridImageStimuli_ChangeSelection(Participant recipient, long serverID, Vector imageNames,Color innermostC, Color innerC, String prefixFORCSVSpreadsheet){
        MessageGridImageStimuliSelectionToClient mgsstc= new MessageGridImageStimuliSelectionToClient("server", "server", serverID, imageNames,  innermostC, innerC);
        ps.sendMessageToParticipant(recipient, mgsstc); 
        this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"GRIDSTIMULISET:CHANGING WHOLE SET"+" to RGB: "+innermostC.getRed()+","+innermostC.getGreen()+", ("+innerC.getBlue()+" "+innerC.getRed()+","+innerC.getGreen()+","+innerC.getBlue()+")", "");  
    }
    
    public void gridImageStimuli_SendSet(Participant recipient, int rows, int columns,Vector serializedImages, int widthheight, long serverID, String prefixFORCSVSpreadsheet){
        MessageGridImagesStimuliSendSetToClient  mssstc = new  MessageGridImagesStimuliSendSetToClient(rows, columns,serializedImages, widthheight, serverID);
        ps.sendMessageToParticipant(recipient, mssstc);     
        this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"GRIDSTIMULISET:SENDING", "");
    }
    
    public void gridImageStimuli_changeImages(Participant recipient, Vector names, long serverID, String prefixFORCSVSpreadsheet){
        MessageGridImagesStimuliChangeImages mgsci = new MessageGridImagesStimuliChangeImages(names,serverID);
        ps.sendMessageToParticipant(recipient, mgsci);
        String imagenames = "";
        for(int i=0;i<names.size();i++){
            imagenames = imagenames + ": "+(String)names.elementAt(i);
        }
        this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient,prefixFORCSVSpreadsheet+"GRIDSTIMULISET:CHANGING",imagenames);
    }
    
    
    public void gridTextStimuli_ChangeSelection(Participant recipient, long serverID, Color[][] innermostC, Color[][] innerC){
        MessageGridTextStimuliSelectionToClient mgsstc= new MessageGridTextStimuliSelectionToClient("server", "server", serverID,   innermostC,  innerC);
        ps.sendMessageToParticipant(recipient, mgsstc); 
        String nameORindexToSave ="ERROR-NOT-SET";
        
        
        String datatobesaved = "innermostcolours: ";    
        for(int i=0;i<innermostC.length;i++){
            for(int j=0;j<innermostC[i].length;j++){
                 datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ innermostC[i][j].getRed()+","+innermostC[i][j].getGreen()+","+innermostC[i][j].getBlue();
                 
            }    
        }
 
        datatobesaved = datatobesaved+" innercolours:";
        for(int i=0;i<innerC.length;i++){
            for(int j=0;j<innerC[i].length;j++){
                  datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ innerC[i][j].getRed()+","+innerC[i][j].getGreen()+","+innerC[i][j].getBlue();    
           }
        }     
            
        this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns("gridtextchangeselection", recipient, datatobesaved);
        
         
    }
     
    
    public void gridTextStimuli_Initialize(Participant recipient, int rows, int columns,String[][] names,Color[][] innermostC, Color[][] innerC, int width, int height, long serverID){
        MessageGridTextStimuliInitialize  mssstc = new  MessageGridTextStimuliInitialize(rows, columns, names, innermostC, innerC, width,height, serverID);
        ps.sendMessageToParticipant(recipient, mssstc); 
        String datatobesaved = "innermostcolours: ";    
        for(int i=0;i<innermostC.length;i++){
            for(int j=0;j<innermostC[i].length;j++){
                 datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ innermostC[i][j].getRed()+","+innermostC[i][j].getGreen()+","+innermostC[i][j].getBlue();
                 
            }    
        }
 
        datatobesaved = datatobesaved+" innercolours:";
        for(int i=0;i<innerC.length;i++){
            for(int j=0;j<innerC[i].length;j++){
                  datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ innerC[i][j].getRed()+","+innerC[i][j].getGreen()+","+innerC[i][j].getBlue();    
           }
        }     
        datatobesaved = datatobesaved+" text:";
        for(int i=0;i<innerC.length;i++){
            for(int j=0;j<innerC[i].length;j++){
                  datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ names[i][j];   
           }
        }  
        
        this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns("gridtextinitialize", recipient, datatobesaved);
    }
    
    public void gridTextStimuli_changeTexts(Participant recipient, String[][] names, long serverID){
        //String subDialogueID = cC.get
        MessageGridTextStimuliChangeTexts mgsci = new MessageGridTextStimuliChangeTexts(names,serverID);
        ps.sendMessageToParticipant(recipient, mgsci);
        String datatobesaved = "text: ";  
        for(int i=0;i<names.length;i++){
            for(int j=0;j<names[i].length;j++){
                 datatobesaved = datatobesaved+ " ["+i+","+j+"]:"+ names[i][j];          
            }    
        }
        this.newsaveAdditionalRowOfDataToSpreadsheetOfTurns("gridtextchangetext", recipient, datatobesaved);   
    }
    
    
    
    
    
    
    
    
     public void subliminalstimuliset_SendSet(Participant recipient, Vector serializedImages, int width,int height, String prefixFORCSVSpreadsheet){
         MessageSubliminalStimuliSendSetToClient  mssstc = new MessageSubliminalStimuliSendSetToClient(serializedImages, width, height);
         ps.sendMessageToParticipant(recipient, mssstc);     
         this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"STIMULISET:SENDING", "");
     }
    
     
     public void subliminalstimuliset_displaySet(Participant recipient, long fixation1time, long stimulus1time
                               ,long blankscreen1time
                               ,long fixation2time, long stimulus2time
                               ,long blankscreen2time,
                               String stimulus1ID, String stimulus2ID, String prefixFORCSVSpreadsheet ){
         
         MessageSubliminalStimuliChangeImage  mssci  = new MessageSubliminalStimuliChangeImage(fixation1time,stimulus1time
                               ,blankscreen1time
                               ,fixation2time,  stimulus2time
                               ,blankscreen2time,
                               stimulus1ID,  stimulus2ID);
         ps.sendMessageToParticipant(recipient, mssci);  
          this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"STIMULISET:Display new set",
                  " fixationtime:"+fixation1time+
                  " stimulus1time:"+stimulus1time+
                  "blankscreen1time:"+blankscreen1time+
                  " fixation2time:"+fixation2time+
                  " stimulus2time:"+stimulus2time+
                  "blankscreen2time:"+blankscreen2time+
                  " stimulus1ID:"+stimulus1ID+
                  " stimulus2ID:"+stimulus2ID);
     }
        
     
     public void subliminalstimuliset_displayText(Participant recipient, String text, String panelName, Color textColour, int positionX, int positionY, long lengthOfTime, String prefixFORCSVSpreadsheet ){
         
          MessageSubliminalStimuliDisplayText mssdt= new MessageSubliminalStimuliDisplayText(text,textColour,panelName,positionX,positionY, lengthOfTime);
          ps.sendMessageToParticipant(recipient, mssdt); 
          
          this.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(recipient, prefixFORCSVSpreadsheet+"STIMULISET: Send text",text+" ("+positionX+","+positionY+")");
     }
     
         
    
    


     /**
      * Sends task move (e.g. move in the maze game) to a participant.
      * @param p
      * @param mt
      */
     public void sendTaskMoveToParticipant(Participant p, MessageTask mt){
        ps.sendMessageToParticipant(p, mt);

    }

     


    


    

     /**
      * Returns the location on the local file system where the data from the experiment is saved
      * @return File representing the enclosing folder/directory containing the data from the experiment
      */
     public File getDirectoryNameContainingAllSavedExperimentalData(){
        return convIO.getFileNameContainingConversationData();
    }

    public ConversationHistory getHistory(){
        return cH;
    }
    public DefaultConversationController getController(){
        return cC;
    }
    
    
    

   



    public Participants getParticipants(){
        return this.ps;
    }
    public DocChangesIncoming getDocChangesIncoming(){
        return this.turnsconstructed;
    }
    public DocChangesIncomingSequenceFIFO getTurnBeingConstructed(Participant p){
        return this.turnsconstructed.getTurnBeingConstructed(p);
    }
   

    public IntelligentIO getConvIO(){
        return convIO;
    }

    
    static public boolean isRunningLocally(){
        if(Conversation.statC==null)return true;
        return false;
    }

    
    
    
    
    
    
    
    public void killClientOnRemoteMachine(Participant p){
        ps.killClient(p);
    }
    
    
    
    
    
    /**
     * Attempts to close down the Conversation and associated resources.
     * @param sendCloseToClients
     */
    public void closeDown(boolean sendCloseToClients){
        try{
            this.conversationIsActive=false;
            
           
                cC.closeDown();
            
            this.cHistoryUIM.closedown();
            this.cH.closeDown();
           
            this.ps.closeDown(sendCloseToClients);
            //this.ps.stop();
            this.ps = null;

            
            
            //this.conversationIsActive=false;
            this.convIO.shutThreadDownAndCloseFiles();
        } catch (Exception e){

        }
    }


    public void showStimulusImageFromJarFile_InitializeWindow(Participant p, int width, int height, String imagename, String[] buttonnames){
         diet.message.MessageStimulusImageDisplayNewJFrame msidjf = new MessageStimulusImageDisplayNewJFrame( width, height, imagename,buttonnames );
         ps.sendMessageToParticipant(p,  msidjf);
         String allButtonnames="Buttons: ";
         for(int i=0;i<buttonnames.length;i++){
              allButtonnames = allButtonnames+ "{"+buttonnames[i]+"}";
         }
         newsaveAdditionalRowOfDataToSpreadsheetOfTurns("stimulusimage_initialize",  p, "Width:"+width+" Height:"+height+ " Filename:"+imagename);
    }
    
    public void showStimulusImageFromJarFile_ChangeImage(Participant p,String imagename, long durationmsecs){
         diet.message.MessageStimulusImageChangeImage msidjf = new MessageStimulusImageChangeImage( imagename, durationmsecs );
         ps.sendMessageToParticipant(p,  msidjf);
         newsaveAdditionalRowOfDataToSpreadsheetOfTurns("stimulusimage_change_instruction",  p, "Filename:"+imagename+ " Duration:"+durationmsecs);
    }
    
    public void showStimulusImageEnableButtons(Participant p,String[] buttonnames, boolean enable){
         diet.message.MessageStimulusImageEnableButtons msieb = new MessageStimulusImageEnableButtons( buttonnames, enable );
         ps.sendMessageToParticipant(p,  msieb);
         String allButtonnames="Buttons: ";
         for(int i=0;i<buttonnames.length;i++){
             allButtonnames = allButtonnames+ "{"+buttonnames[i]+"}";
         }
         
         String type="enable";
         if(!enable) {
             type = "disable";
         }
         
         newsaveAdditionalRowOfDataToSpreadsheetOfTurns("stimulusimage_buttons_"+type,  p,allButtonnames );
    }
    
    
    
    
    
    
    
    
    public void displayNEWWebpage(Participant p, String id, String header,String url, int width, int height,boolean vScrollBar,boolean displayCOURIERFONT){
        ps.displayNEWWebpage(p, id, header, url, width, height, vScrollBar,displayCOURIERFONT);
        
    }

    
    

    public void changeWebpageTextAndColour(Participant p, String id,String text, String colourBackground, String colourText){
        String textToBeSent = "<html><head><style type=\"text/css\">body {color:"+colourText+"; background: "+colourBackground+";}div { font-size: 120%;}</style></head><body><div>"+ text+"</div></body></html>";
        ps.changeWebpage(p, id, textToBeSent);
    }
//public String redbackground = "<html><head><style type=\"text/css\">body {color: white; background: red;}div { font-size: 200%;}</style></head><body><div>INCORRECT </div></body></html>";

    public void changeWebpageImage_OnServer_DEPRECATED(Participant p, String id,  String imageaddressOnServerWebserver){
        //imageaddressOnServerWebserver needs to include a backslash as first character.
        String url = "<html><img src='http://%%SERVERIPADDRESS%%"+          imageaddressOnServerWebserver+"'></img>";
       
       
        ps.changeWebpage(p, id, url);
    }

    public void changeWebpage(Participant p, String id,String url){
        ps.changeWebpage(p, id, url);
    }

     public void changeWebpage(Participant p, String id,String url, String newtext, long lengthOfTime){
        ps.changeWebpage(p, id, url, newtext, lengthOfTime);
    }

    //public void changeWebpage(Participant p, String id,String url, String prepend, String append){
    //    ps.changeWebpage(p, id, url, prepend, append);
    //}

    public void changeJProgressBar(Participant p,String id, String text, Color foreCol, int value){
        ps.changeJProgressBar(p, id, text, foreCol, value);
    }
    public void changeJProgressBarsOfAllParticipants(String id, String text, Color foreCol,int value){
        if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("SINH1");;System.out.flush();}
        Vector v = ps.getAllParticipants();
         if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){ System.out.println("SINH2");;System.out.flush();}
        for(int i=0;i<v.size();i++){
            
            Participant p = (Participant)v.elementAt(i);
            if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){ System.out.println("SINH4 "+p.getUsername());;System.out.flush();}
             ps.changeJProgressBar(p, id, text, foreCol, value);
             if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("SINH5 "+p.getUsername());System.out.flush();}
        }
        if(DefaultConversationController.sett.debug_debugMESSAGEBLOCKAGE){System.out.println("SINH6 ");System.out.flush();}
    }

    
    public void textOutputWindow_ChangeText( String id, String newtext, boolean append,  Participant...prtss){
        for(Participant recipient: prtss){
            ps.changeWebpage(recipient, id, "", newtext,append);
            try{
                 String subdialogueID = cC.pp.getSubdialogueID(recipient);
                 Vector recipientNames = new Vector();
                 recipientNames.addElement(recipient.getUsername());
                 
                 String lineToSave = "TEXTOUTPUTWINDOW:"+" ID:"+id+" TEXT:"+newtext;
                 lineToSave=lineToSave.replace("\n", "((NEWLINE))").replace("\r", "((NEWLINE))");
                 
                 cH.saveArtificialMessageCreatedByServer(subdialogueID, new Date().getTime(), "server",lineToSave, recipientNames, new Vector(), false);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        
    }
    
   
    
    
    
    

     public void textOutputWindow_CloseWindow(Participant p, String id){
        ps.closeWebpageWindow(p, id);
    }



    public ExperimentManager getExpManager() {
        return expManager;
    }

    public void printWln(String windowName, String text){
        EMUI em = this.getExpManager().getEMUI();
        if(em!=null){
            em.println(windowName, text);
        }
        convIO.saveWindowTextToLog(windowName, text);
    }

    public static void printWSln(String windowName, String text){
        System.err.println("PRINTWSLN: "+windowName+": "+text);
        if(statC!=null){
           statC.printWln(windowName, text);
           
        }
    }
    public static void printWSlnLog(String windowName, String text)
    {
    	if(statC!=null)
    		statC.printWlnLog(windowName, text);
                statC.convIO.saveWindowTextToLog(windowName, text);

    }
    public void printWlnLog(String windowName, String text)
    {
    	EMUI em = this.getExpManager().getEMUI();
        if(em!=null){
            em.println(windowName, text);
        }
        this.convIO.saveWindowTextToLog(windowName, text);


    }

    public static void saveErr(Throwable t){
        if(statC!=null){
           statC.saveErrorLog(t);
        }
    }

    public static void saveErr(String s){
        if(statC!=null){
           statC.saveErrorLog(s);
        }
    }

    public void saveErrorLog(Throwable t){
        System.err.println(cC.getID()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
        printWln("Main","There is an ERROR in the Conversation Controller");
        printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
        printWln("Main","the saved experimental data");
        printWln("Main",t.getMessage());
        getConvIO().saveErrorLog(t);
    }
    public void saveErrorLog(String s){
        System.err.println(cC.getID()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
        printWln("Main","There is an ERROR in the Conversation Controller");
        printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
        printWln("Main","the saved experimental data");
        printWln("Main",s);
        getConvIO().saveErrorLog(s);
    }


    public boolean isConversationActive() {
        return conversationIsActive;
    }

    public void setConversationIsActive(boolean conversationIsActive) {
        this.conversationIsActive = conversationIsActive;
    }


    public void printAllP(){
        System.err.println("----------LISTING PARTICIPANTS------------------------");
        for(int i=0;i<this.ps.getAllParticipants().size();i++){
            Participant p = (Participant) ps.getAllParticipants().elementAt(i);
            System.err.println("PARTICIPANT:"+p.getParticipantID()+" "+p.getUsername());
        }
    }
    
    
    
    
    public void wysiwyg_changeFloorStatus(Participant p, int newStatus, boolean deletePending, long serverid){
        MessageWYSIWYGChangeFloorStatus msfs = new MessageWYSIWYGChangeFloorStatus("server","server", newStatus, deletePending, serverid);
        ps.sendMessageToParticipant(p, msfs);
       
    }

    
    
    
    
}
