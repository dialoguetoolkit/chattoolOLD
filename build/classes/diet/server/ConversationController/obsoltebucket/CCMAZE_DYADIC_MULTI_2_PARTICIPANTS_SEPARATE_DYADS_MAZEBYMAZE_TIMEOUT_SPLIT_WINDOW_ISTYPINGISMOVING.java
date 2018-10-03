package diet.server.ConversationController.obsoltebucket;
import diet.server.IsTypingController.IsTypingOrNotTyping;
import diet.task.mazegame.MazeGameController2WAY;
import diet.task.mazegame.MazeGameLoadMazesFromJarFile;
import diet.message.*;
;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import diet.task.TaskControllerInterface;
import diet.task.mazegame.message.MessageCursorUpdate;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomIntGenerator;
import diet.utils.Conversion;
import diet.utils.HashtableWithDefaultvalue;
import diet.server.IsTypingController.IsMovingOrNotMoving;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;




public class CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT_SPLIT_WINDOW_ISTYPINGISMOVING  extends CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT implements TaskControllerInterface{

    public CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT_SPLIT_WINDOW_ISTYPINGISMOVING(Conversation c) {
        super(c);
    }
    
         
    
    
        //IsTypingOrNotTyping itnt = new IsTypingOrNotTyping(this);
        IsMovingOrNotMoving imnm = new IsMovingOrNotMoving(this);
        
        boolean InformWITHTIMEOUT = true;
        
        
        //HashtableWithDefaultvalue htChatFrameColours = new HashtableWithDefaultvalue(Color.white);
        //HashtableWithDefaultvalue htMazeFrameColours = new HashtableWithDefaultvalue(Color.white);
    
         public long timePerMaze =  30000+  (5 * 60 * 1000) + (numberOfCountdownSteps*1000);      
    
          //CyclicRandomIntGenerator crig = new CyclicRandomIntGenerator(0,4);
         // CyclicRandomIntGenerator crig = new CyclicRandomIntGenerator(3,1);
          
        public void startTiming(){
        //if(2<5)return; 
        //numberOfTimers++;
        
        //System.out.println("STARTING TIMING"+numberOfTimers); //This is for debug
        
        Thread t = new Thread(){
         public void run(){
             experimenthasstarted = true;
             while(doTiming){
             try{
                 long timeOfStartOfSleep = new Date().getTime();
                  //System.out.println("TIMEOFSLEEP: "+ timeO   );
                  Thread.sleep(1000);
                  long timeOfEndOfSleep = new Date().getTime();
                  
                   
                  
                  
                  for(int i=0;i<mazegameControllers.size();i++){
                      final MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                     
                      
                      
                      long timeOfLastChange =(Long) mazegameControllerLongTimeOfLatestChange.get(mgc);
                      if(pauseTiming){
                         timeOfLastChange = timeOfLastChange+ (timeOfEndOfSleep-timeOfStartOfSleep); 
                         mazegameControllerLongTimeOfLatestChange.put(mgc,timeOfLastChange);
                      }
                      
                      
                      
                      long timeSinceLastChange = new Date().getTime()-timeOfLastChange;
                      long timeRemaining = timePerMaze - timeSinceLastChange;
                      String timeLeft = Conversion.convertMillisecondsIntoText(timeRemaining);
                      Color background = Color.GREEN;
                      if(timeRemaining<120000) background = Color.ORANGE;
                      if(timeRemaining<60000) background = Color.RED;
                      
                      float proportionleft = ((float) timeRemaining)/timePerMaze;
                      
                      if(!isDisplayingInterimMessage){
                         //Need to do this so that it doesn't display progress bars while displaying the flashing screen
                         c.changeJProgressBar(mgc.pDirector, "CHATFRAME", timeLeft, background, (int)(100*proportionleft));
                         c.changeJProgressBar(mgc.pMatcher, "CHATFRAME", timeLeft, background, (int)(100*proportionleft));
                      }
                      mgc.updateJProgressBar((int)(100*proportionleft), timeLeft, background);
                      if(timeRemaining<=0){
                          c.changeJProgressBar(mgc.pDirector, "CHATFRAME", "Out of time! Please wait...changing the interface", Color.GRAY, 0); 
                          c.changeJProgressBar(mgc.pMatcher, "CHATFRAME", "Out of time! Please wait...changing the interface", Color.GRAY, 0);
                          
                          
                          Thread t = new Thread(){ public void run(){
                              isDisplayingInterimMessage=true;
                              ////c.doCountdownToNextPartner(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "Out of time. Next maze",getCVSPREFIX(mgc));
                              
                              
                              if(mgc.getMazeNo()==7){
                                  c.deprecated_sendArtificialTurnToRecipient(mgc.pDirector, "FIRST HALF COMPLETED - PLEASE TAKE A BREAK", 0);
                                  c.deprecated_sendArtificialTurnToRecipient(mgc.pMatcher, "FIRST HALF COMPLETED - PLEASE TAKE A BREAK", 0);
                                  Conversation.printWSln("Main", "First half completed");
                              }
                              else{
                                  mgc.moveToNextMaze("NEXT MAZE: Ran out of time");
                                  c.changeClientInterface_clearMainWindows(mgc.pDirector);
                                  c.changeClientInterface_clearMainWindows(mgc.pMatcher);
                                  addFillerSpaceInChatWindow(mgc);
                                  isDisplayingInterimMessage=false;    
                              }
                              
                           } }; t.start();
                           
                           
                           mazegameControllerLongTimeOfLatestChange.put(mgc,new Date().getTime());
                      }
                      
                      
                  }
                  
                  
                  
                  
                  
                 
             }catch (Exception e){
                  e.printStackTrace();
                  
             }
         }
         }  
      };
        t.start();
        
        
        
         
    }
    
        
       
        
        
        public void addFillerSpaceInChatWindow(MazeGameController2WAY mgc2w){
            try{
                  String info = this.getCVSPREFIX(mgc2w);
                  c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc2w.pDirector, "\n\n\n\n\n\n\n\n\n",0, info +"PREFILLER", sm.defaultFONTSETTINGSSELF);
                  c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc2w.pMatcher, "\n\n\n\n\n\n\n\n\n",0, info +"PREFILLER", sm.defaultFONTSETTINGSSELF);
                  
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    
        
        
        
        
      public  void processTaskMove(MessageTask mtm, Participant origin) {
       
        final MazeGameController2WAY mgc = getMazeGameControllerForDyad(origin);
        int mazeNo = mgc.getMazeNo();
        String interfaceName = this.getInterface(mazeNo);
        
        try{
          if(this.InformWITHTIMEOUT & interfaceName.equalsIgnoreCase(interface_showMoves)){
              this.imnm.processDoingsByClient(origin);
          }
          else{
             // Participant pRecipient = this.getCurrentInterlocutor(origin);
             // Color cCHATFRAME = (Color)this.htMazeFrameColours.getObject(pRecipient);
             // c.changeClientInterface_changeBorderOnMazeFrame(pRecipient, 6, cCHATFRAME);
             // if(cCHATFRAME==Color.BLACK)htMazeFrameColours.putObject(pRecipient, Color.WHITE);
             // else if(cCHATFRAME==Color.WHITE)htMazeFrameColours.putObject(pRecipient, Color.BLACK);
          }
        
        }catch (Exception e){
            e.printStackTrace();
            Conversation.printWSln("Main", "There was an error..");
            Conversation.saveErr(e);
        }
        
         boolean mazeIsCompleted = mgc.isCompleted();
         if(mazeIsCompleted){
             Conversation.printWSln("Main", "Possible network lag and/or error. Experiment is receiving task moves from "+origin.getUsername()+"..."+"the current maze is: "+mgc.getMazeNo());  
             return;
         }
         if( mtm instanceof MessageCursorUpdate){
             MessageCursorUpdate mcu = (MessageCursorUpdate)mtm;
             if(mcu.getMazeNo() != mgc.getMazeNo()){
                  Conversation.printWSln("Main", "Possible network lag and/or error. Experiment is receiving maze cursor updates from "+origin.getUsername()+"...from maze number "+mcu.getMazeNo()+"..."+"but the current maze is: "+mgc.getMazeNo());
                  return;
             }
         }

         mazeIsCompleted = mgc.processMazeMove(mtm, origin,false);
         
         if(mazeIsCompleted){
             long finishTime = new Date().getTime();
             try{
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(pp.getSubdialogueID(mgc.pDirector)    ,this.getCVSPREFIX(mgc)+"DATA", mgc.pDirector.getParticipantID(), mgc.pDirector.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(pp.getSubdialogueID(mgc.pDirector),  this.getCVSPREFIX(mgc)+"DATA", mgc.pMatcher.getParticipantID(), mgc.pMatcher.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
             
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
         
         
         
         if(mazeIsCompleted ){
             
             this.mazegameControllerLongTimeOfLatestChange.put(mgc, new Date().getTime());
              c.changeJProgressBar(mgc.pDirector, "CHATFRAME", "Please wait...changing the interface settings", Color.GRAY, 0); 
              c.changeJProgressBar(mgc.pMatcher, "CHATFRAME", "Please wait...changing the interface settings", Color.GRAY, 0);
             
              
              
             Thread t = new Thread(){ public void run(){
                           isDisplayingInterimMessage=true;
                           ////c.doCountdownToNextPartner(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "Completed the maze. New maze",getCVSPREFIX(mgc));
                            if(mgc.getMazeNo()==7){
                                  c.deprecated_sendArtificialTurnToRecipient(mgc.pDirector, "FIRST HALF COMPLETED - PLEASE TAKE A BREAK", 0);
                                  c.deprecated_sendArtificialTurnToRecipient(mgc.pMatcher, "FIRST HALF COMPLETED - PLEASE TAKE A BREAK", 0);
                            }
                            else{
                                  mgc.moveToNextMaze();
                                 //crig.generateNext(mgc);
                                 isDisplayingInterimMessage=false;
                           
                                c.changeClientInterface_clearMainWindows(mgc.pDirector);
                                c.changeClientInterface_clearMainWindows(mgc.pMatcher);
                                addFillerSpaceInChatWindow(mgc);      
                            }
                           
                          
                          //c.sendArtificialTurnToRecipient(null, null, interfacemode);
                          
                           
             } }; t.start();
         }
               
    }
    
    
    public synchronized int getMazeControllerNumber(MazeGameController2WAY mgc2){
       try{
        return this.mazegameControllers.indexOf(mgc2);
       }catch (Exception e){
           System.err.println("OUT");
           return -2;
       } 
    }
    
    
    public String getCVSPREFIX(MazeGameController2WAY mgc){
           String cvsPREFIX="";  
           try{
           int mazeControllerNumber = getMazeControllerNumber(mgc);
           String interfaceName = getInterface(mgc.getMazeNo());
           cvsPREFIX = "MGC"+mazeControllerNumber+"_"+interfaceName;
           }catch(Exception e){}
           return cvsPREFIX;
    }
   
    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct){    
          
           if(!this.experimenthasstarted){
                
           
               c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(sender, "Please wait for the experiment to start before typing! Thanks!", 0,sm.defaultFONTSETTINGSSERVER);
               return;
           }
        
         
     
   
           MazeGameController2WAY mgc = getMazeGameControllerForDyad(sender);
           if(mgc==null){
               Conversation.saveErr("ERROR: could not find mgc for "+sender.getUsername()+"..."+mct.getText()+"This is not critical if the experiment hasn't started yet");
               Conversation.printWSln("Main", "ERROR: could not find mgc for "+sender.getUsername()+"..."+mct.getText());
               //c.relayTurnToParticipant(sender, sender, mct);
               return;
           }
           
           int mazeNo = mgc.getMazeNo();
           int moveNo= mgc.getMoveNo();
           int indivMveNo = mgc.getParticipantsMoveNo(sender);
           Participant pRecipient = this.getCurrentInterlocutor(sender);
           if(pRecipient==null)return;
           
            
           //c.relayMazeGameTurnToParticipantWithEnforcedColour(sender, pRecipient, mct, mazeNo, moveNo, indivMveNo, moveNo);
           
          
           //interfacemode = mazeNo %3; 
           
           
           int mazeControllerNumber = getMazeControllerNumber(mgc);
           String interfaceName = this.getInterface(mazeNo);
           String info = this.getCVSPREFIX(mgc);
           
           
           if(interfaceName.equalsIgnoreCase(interface_showMoves)||interfaceName.equalsIgnoreCase(interface_showTyping)){
               c.deprecated_relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(sender, pRecipient,0, mct, mazeNo, moveNo, indivMveNo,sm.defaultFONTSETTINGSOTHER1,info);    
           }
           else if(interfaceName.equalsIgnoreCase(interface_showCharactersONLY_ANDNOHISTORY) || interfaceName.equalsIgnoreCase(interface_showASTERISKS_ANDNOHISTORY)){
               c.changeClientInterface_clearMainWindows(pRecipient);
               c.changeClientInterface_clearMainWindows(sender);
               
               c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(sender, "\n\n\n\n\n\n\n\n\n",0, info +"PREFILLER", sm.defaultFONTSETTINGSSELF);
               c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(sender, sender.getUsername()+": "+mct.getText(),0, info+"FILLER", sm.defaultFONTSETTINGSSELF);
               c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(pRecipient, "\n\n\n\n\n\n\n\n\n",0, info +"PREFILLER", sm.defaultFONTSETTINGSSELF);
               c.deprecated_relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(sender, pRecipient,0, mct, mazeNo, moveNo, indivMveNo,sm.defaultFONTSETTINGSOTHER1,info);  
              
           }
            
           
           //(sender, pRecipient, indivMveNo, mct, mazeNo, moveNo, indivMveNo, moveNo);
           //c.relayMazeGameTurnToParticipant(sender, pRecipient, mct, mazeNo, moveNo, indivMveNo);
           //c.sendLabelDisplayToParticipantInOwnStatusWindow(pRecipient, " ", false);
           
           
           mgc.appendToUI(sender.getUsername()+": "+mct.getText());
           
           //c.changeClientInterface_clearMainWindow(pRecipient);
           //c.changeClientInterface_clearMainWindows(sender);
           
    }
    
    
     @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
          String cvsID="_unset_";
          try{
             cvsID = this.getCVSPREFIX(this.getMazeGameControllerForDyad(sender));
             c.newsaveClientKeypressToFile(sender, mkp, cvsID);
            // c.saveKeypressToFile(sender, mkp,cvsID);
          }catch(Exception e){e.printStackTrace();};
          //if(saveKeypress)c.saveKeypressToFile(sender, mkp,cvsID);
          
          
          
          
           
          
    }
    
    
    
    
    public void loadMazesAndAssignParticipantsToControl(){
    try{
         MazeGameController2WAY mgc =null;
         System.err.println("STARTING EXPERIMENT1");
         while (this.participantsQueued.size()>1){
             Participant pA = (Participant)this.participantsQueued.elementAt(0);
             Participant pB = (Participant)this.participantsQueued.elementAt(1);
             Conversation.printWSln("Main", "Pairing "+pA.getUsername()+"  with "+pB.getUsername());
             if(mgc==null){
                  //MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
                  MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
                  //mglmfj.getSetOf9MazesFromJar();
                  
                  
                  mglmfj.getSetOf18MazesFromJarASTEXT();         
                //  mgc = new MazeGameController2WAY(c,MazeGameLoadMazesFromJarFile.cl1MazesRANDOMIZED,MazeGameLoadMazesFromJarFile.cl2MazesRANDOMIZED);
                 //mgc =   new MazeGameController2WAY(c,true);
                 mgc.startNewMazeGame(pA, pB);
                 this.mazegameControllers.addElement(mgc);
                 this.mazegameControllerLongTimeOfLatestChange.put(mgc, new Date().getTime());
             } 
             else{
                  MazeGameController2WAY mgcN = new MazeGameController2WAY(c,mgc.getP1Mazes_Cloned(),mgc.getP2Mazes_Cloned());
                  mgcN.startNewMazeGame(pA, pB);
                  this.mazegameControllers.addElement(mgcN);
                  this.mazegameControllerLongTimeOfLatestChange.put(mgcN, new Date().getTime());
             }
             this.participantsQueued.remove(pA);
             this.participantsQueued.remove(pB);
             
         }
         if(this.participantsQueued.size()>0){
             Conversation.printWSln("Main", "THERE IS AN ODD NUMBER OF PARTICIPANTS! ");
             System.err.println("THERE IS AN ODD NUMBER OF PARTICIPANTS");
         }
         }catch (Exception e){
             e.printStackTrace();
         }
         this.pauseTiming=false;
         ////
         
         for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pDirector, "Please start!", 0,sm.defaultFONTSETTINGSSERVER);
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pMatcher, "Please start!", 0,sm.defaultFONTSETTINGSSERVER);
         }         
         
         
                      
                      
         
         
         ////
         this.startTiming();
         System.err.println("STARTING EXPERIMENT");
    }

    
    
    
    
    
    
     
     public void partnerParticipants(){
         for(int i=0;i<mazegameControllers.size();i++){
             MazeGameController2WAY mgc = (MazeGameController2WAY)mazegameControllers.elementAt(i);
             this.itnt.addPairWhoAreMutuallyInformedOfTyping(mgc.pDirector, mgc.pMatcher);
             this.imnm.addPairWhoAreMutuallyInformedOfTyping(mgc.pDirector, mgc.pMatcher);
             this.itnt.initializeClientInterfacesWithInactiveState(mgc.pDirector);
             this.itnt.initializeClientInterfacesWithInactiveState(mgc.pMatcher);
             this.imnm.initializeClientInterfacesWithInactiveState(mgc.pDirector);
             this.imnm.initializeClientInterfacesWithInactiveState(mgc.pMatcher);
             
         }
         
     }
     
    
      public void startExperiment(){
         loadMazesAndAssignParticipantsToControl();    
         partnerParticipants();
         for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pDirector, "From now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pMatcher, "From now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      
         } 
     }
     
     
    
     public void startExperimentOLD(){
         try{
         MazeGameController2WAY mgc =null;
         System.err.println("STARTING EXPERIMENT1");
         while (this.participantsQueued.size()>1){
             Participant pA = (Participant)this.participantsQueued.elementAt(0);
             Participant pB = (Participant)this.participantsQueued.elementAt(1);
             Conversation.printWSln("Main", "Pairing "+pA.getUsername()+"  with "+pB.getUsername());
             if(mgc==null){
                  MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
                  mglmfj.getSetOf14MazesFromJar();    
                //  mgc = new MazeGameController2WAY(c,MazeGameLoadMazesFromJarFile.cl1MazesRANDOMIZED,MazeGameLoadMazesFromJarFile.cl2MazesRANDOMIZED);
                 //mgc =   new MazeGameController2WAY(c,true);
                 mgc.startNewMazeGame(pA, pB);
                 this.mazegameControllers.addElement(mgc);
                 this.mazegameControllerLongTimeOfLatestChange.put(mgc, new Date().getTime());
             } 
             else{
                  MazeGameController2WAY mgcN = new MazeGameController2WAY(c,mgc.getP1Mazes_Cloned(),mgc.getP2Mazes_Cloned());
                  mgcN.startNewMazeGame(pA, pB);
                  this.mazegameControllers.addElement(mgcN);
                  this.mazegameControllerLongTimeOfLatestChange.put(mgcN, new Date().getTime());
             }
             this.participantsQueued.remove(pA);
             this.participantsQueued.remove(pB);
             
         }
         if(this.participantsQueued.size()>0){
             Conversation.printWSln("Main", "THERE IS AN ODD NUMBER OF PARTICIPANTS! ");
             System.err.println("THERE IS AN ODD NUMBER OF PARTICIPANTS");
         }
         }catch (Exception e){
             e.printStackTrace();
         }
         this.pauseTiming=false;
         ////
         
         for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pDirector, "Please start!\nFrom now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pMatcher, "Please start!\nFrom now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      
         }         
         
         
                      
                      
         
         
         ////
         this.startTiming();
         System.err.println("STARTING EXPERIMENT");
    }
    
    
   
    //int interfacemode =2;
    
    
    

    
    
    
    
        @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        //if(2<5)return super.requestParticipantJoinConversation(participantID);
        
        if(experimenthasstarted){
            boolean foundOriginalParticipant = false;
            Vector vParticipants = c.getParticipants().getAllParticipants();
            for(int i=0;i<vParticipants.size();i++){
                Participant p = (Participant)vParticipants.elementAt(i);
                if(p.getParticipantID().equalsIgnoreCase(participantID)){
                    return true;
                }
            }
            Conversation.printWSlnLog("Main", "A participant tried to log in with ID: "+participantID+" during the experiment.");
            return false;
        }
        if(participantID.startsWith("1111A"))return true;
        if(participantID.startsWith("1111B"))return true;
        
        if(participantID.startsWith("2222A"))return true;
        if(participantID.startsWith("2222B"))return true;
        
        if(participantID.startsWith("3333A"))return true;
        if(participantID.startsWith("3333B"))return true;
        
        if(participantID.startsWith("4444A"))return true;
        if(participantID.startsWith("4444B"))return true;
        
        if(participantID.startsWith("5555A"))return true;
        if(participantID.startsWith("5555B"))return true;
        
        if(participantID.startsWith("6666A"))return true;
        if(participantID.startsWith("6666B"))return true;
     
       if(participantID.startsWith("7777A"))return true;
       if(participantID.startsWith("7777B"))return true;
        
       if(participantID.startsWith("8888A"))return true;
       if(participantID.startsWith("8888B"))return true;
         
       if(participantID.startsWith("9999A"))return true;
       if(participantID.startsWith("9999B"))return true;
       
       if(participantID.startsWith("0000A"))return true;
       if(participantID.startsWith("0000B"))return true;
       
       
        //if(participantID.equals("YYYYYY"))return true;
        //if(participantID.equals("ZZZZZZ"))return true;
        
        
        return false;
         //return super.requestParticipantJoinConversation(participantID);
    }

    @Override
    public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
       // super.processWYSIWYGTextRemoved(sender, mWYSIWYGkp); //To change body of generated methods, choose Tools | Templates.
        this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow());
        //System.exit(-4);
    }

    @Override
    public synchronized void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
        //super.processWYSIWYGTextInserted(sender, mWYSIWYGkp); //To change body of generated methods, choose Tools | Templates.
        
       this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow());
        // System.exit(-5);
    }
    
    boolean colourchoice=true;
    
    public synchronized void updateInfoAsItIsTyped(Participant sender, String textInBoxOLD){
           String textInBox ="";
           try{
              //textInBox = "PRESET"+textInBoxOLD.replaceAll("\n", "").replace("\n", "").replaceAll("\r\n", "").replaceAll("\r", "")+"ENDSET";
               textInBox = textInBoxOLD.replaceAll("\n", "").replace("\n", "").replaceAll("\r\n", "").replaceAll("\r", "");
           }catch(Exception e){e.printStackTrace();}
           
           MazeGameController2WAY mgc = getMazeGameControllerForDyad(sender);
           if(mgc==null)return;
           
           int mazeControllerNumber = getMazeControllerNumber(mgc);
           //int interfaceNumber = crig.getCurrentValue(mgc);
           Participant pRecipient = this.getCurrentInterlocutor(sender);
           int mazeNo = mgc.getMazeNo();
           int moveNo= mgc.getMoveNo();
           int indivMveNo = mgc.getParticipantsMoveNo(sender);
           String info = this.getCVSPREFIX(mgc);
           String interfaceName = this.getInterface(mazeNo);
           
           try{
               if(this.InformWITHTIMEOUT && interfaceName.equalsIgnoreCase(interface_showTyping) ){
                   itnt.processDoingsByClient(sender);
               }
               else{
                 // Color cCHATFRAME = (Color)this.htChatFrameColours.getObject(pRecipient);
                 // c.changeClientInterface_changeBorderOnChatFrame(pRecipient, 6, cCHATFRAME);
                  //c.changeClientInterface_changeBorderOnMazeFrame(pRecipient, 6, cCHATFRAME);
                 // if(cCHATFRAME==Color.BLACK)htChatFrameColours.putObject(pRecipient, Color.WHITE);
                 // else if(cCHATFRAME==Color.WHITE)htChatFrameColours.putObject(pRecipient, Color.BLACK);
               }
           }catch(Exception e){
               e.printStackTrace();
           }
           
           
           System.out.println("contents are"+textInBox);
           
          if(interfaceName.equalsIgnoreCase(interface_showCharactersONLY_ANDNOHISTORY)) {
                c.changeClientInterface_clearMainWindowsExceptWindow0(pRecipient);
                c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(pRecipient, textInBox, 1, sm.defaultFONTSETTINGSOTHER1, info+"UPDATEINFOASTYPED");
                
          }
          else if (interfaceName.equalsIgnoreCase(interface_showASTERISKS_ANDNOHISTORY)){
               c.changeClientInterface_clearMainWindowsExceptWindow0(pRecipient);
               int textLength = textInBox.length();
               String textAsAsterisk ="";
               for(int i=0;i<textInBox.length();i++){
                    textAsAsterisk=textAsAsterisk+"*";
               }
               c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(pRecipient, textAsAsterisk, 1, sm.defaultFONTSETTINGSOTHER1, info+"UPDATEINFOASTYPED");
          }
    }
    
     
    final static String interface_showMoves = "SHOWMOVES";
    final static String interface_showTyping = "SHOWTYPING";
    final static String interface_showCharactersONLY_ANDNOHISTORY = "SHOWCHARACTERS";
     final static String interface_showASTERISKS_ANDNOHISTORY = "SHOWASTERISKS";
    
    
    public String getInterface(int mazeNo){
        
        if(mazeNo ==16){
            return interface_showCharactersONLY_ANDNOHISTORY;   
        }
        if(mazeNo ==17){
            return interface_showASTERISKS_ANDNOHISTORY;
        }
        else if(mazeNo ==0  ||
                mazeNo ==1  ||
                mazeNo ==4  ||
                mazeNo ==5  ||
                mazeNo ==8  ||
                mazeNo ==9  ||
                mazeNo ==12  ||
                mazeNo ==13   
                ){
             return interface_showMoves; 
           
        }
        else {
             return interface_showTyping;
        }
    }

    
    
    
    
    
    
    
    
      
    
    
      
      
      
    

}
