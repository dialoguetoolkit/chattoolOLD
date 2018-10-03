package diet.server.ConversationController.obsoltebucket;
import diet.task.mazegame.MazeGameController2WAY;
import diet.task.mazegame.MazeGameLoadMazesFromJarFile;
import diet.message.*;

import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import diet.task.TaskControllerInterface;
import diet.task.mazegame.message.MessageCursorUpdate;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomIntGenerator;
import diet.utils.Conversion;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;




public class CC1DISCOURSE_AND_PRAGMATICS_RECHETEREN_SIMPLIFIEDMAZES  extends CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT implements TaskControllerInterface{

        public CC1DISCOURSE_AND_PRAGMATICS_RECHETEREN_SIMPLIFIEDMAZES(Conversation c) {
            super(c);
        }

         
    
         public long timePerMaze =  (270 * 1000) + (numberOfCountdownSteps*1000);      
    
          //CyclicRandomIntGenerator crig = new CyclicRandomIntGenerator(0,4);
          //CyclicRandomIntGenerator crig = new CyclicRandomIntGenerator(3,1);
          
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
                              c.doCountdownToNextPartnerDUTCH(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "De tijd is om. Het volgende doolhof begint over ","Je kunt beginnen!",getCVSPREFIX(mgc));
                              mgc.moveToNextMaze("NEXT MAZE: Ran out of time");
                             
                              
                             // crig.generateNext(mgc);
                              c.changeClientInterface_clearMainWindows(mgc.pDirector);
                              c.changeClientInterface_clearMainWindows(mgc.pMatcher);
                              
                             
                              
                              addFillerSpaceInChatWindow(mgc);
                              showImportantMessages(mgc);
                              
                              isDisplayingInterimMessage=false;
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
    
        public void showImportantMessages(MazeGameController2WAY mgc){
            try{ 
            
            
            
            }catch (Exception e){
                e.printStackTrace();
                
            }
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
    
        public synchronized void processTaskMove(MessageTask mtm, Participant origin) {
        final MazeGameController2WAY mgc = getMazeGameControllerForDyad(origin);
        
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
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(pp.getSubdialogueID(mgc.pDirector)  ,this.getCVSPREFIX(mgc)+"DATA", mgc.pDirector.getParticipantID(), mgc.pDirector.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(pp.getSubdialogueID(mgc.pDirector)  ,this.getCVSPREFIX(mgc)+"DATA", mgc.pMatcher.getParticipantID(), mgc.pMatcher.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
             
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
                           c.doCountdownToNextPartnerDUTCH(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "gefeliciteerd! Volgende doolhof " ,"Je kunt beginnen!",getCVSPREFIX(mgc));
                           mgc.moveToNextMaze();
                           //crig.generateNext(mgc);
                           isDisplayingInterimMessage=false;
                           
                           c.changeClientInterface_clearMainWindows(mgc.pDirector);
                           c.changeClientInterface_clearMainWindows(mgc.pMatcher);
                           addFillerSpaceInChatWindow(mgc);
                           showImportantMessages(mgc);
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
           String interfaceType = "NORMAL";
           cvsPREFIX = "MGC"+mazeControllerNumber+"_"+interfaceType;
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
         
           String info = this.getCVSPREFIX(mgc);
           
           
           
           c.deprecated_relayMazeGameTurnToParticipantWithEnforcedColourInMultipleWindow(sender, pRecipient,0, mct, mazeNo, moveNo, indivMveNo,sm.defaultFONTSETTINGSOTHER1,info);    
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
          c.newsaveClientKeypressToFile(sender, mkp,cvsID);
          }catch(Exception e){};
          
           
          
          
          
          
           
          
    }
    
     public void startExperiment(){
         startExperimentOLD();
         for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pDirector, "Je kunt beginnen! Praat vanaf nu niet meer met degene die naast je zit. Als je een vraag hebt, steek dan je hand op!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pMatcher, "Je kunt beginnen! Praat vanaf nu niet meer met degene die naast je zit. Als je een vraag hebt, steek dan je hand op!", 0,sm.defaultFONTSETTINGSSERVER, ""+this.getCVSPREFIX(mgc));
                      
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
                 // mglmfj.getSetOfSimplifiedMazesFromJar();
                  mglmfj.getSetOfFurtherSimplifiedMazesFromJarDEPRECATED();
                  
                 // mgc = new MazeGameController2WAY(c,MazeGameLoadMazesFromJarFile.cl1MazesRANDOMIZED,MazeGameLoadMazesFromJarFile.cl2MazesRANDOMIZED);
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
                     // c.sendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pDirector, "Please start!\nFrom now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,2, ""+this.getCVSPREFIX(mgc));
                     // c.sendArtificialTurnToRecipientWithEnforcedTextColour(mgc.pMatcher, "Please start!\nFrom now on please do not talk to the person sitting next to you. If you have a question, raise your hand!", 0,2, ""+this.getCVSPREFIX(mgc));
                      
         }         
         
         
                      
                      
         
         
         ////
         this.startTiming();
         System.err.println("STARTING EXPERIMENT");
    }
    
    
   
    //int interfacemode =2;
    
    
    

    
    
    
    
        @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        //if(2<5)return super.requestParticipantJoinConversation(participantID);
        
        
        return true;
         //return super.requestParticipantJoinConversation(participantID);
    }

    @Override
    public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
       // super.processWYSIWYGTextRemoved(sender, mWYSIWYGkp); //To change body of generated methods, choose Tools | Templates.
        this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow(),true);
        
        //System.exit(-4);
    }

    @Override
    public synchronized void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
       //super.processWYSIWYGTextInserted(sender, mWYSIWYGkp); //To change body of generated methods, choose Tools | Templates.
        
        
        
       this.updateInfoAsItIsTyped(sender, mWYSIWYGkp.getAllTextInWindow(),false);
        // System.exit(-5);
    }
    
    public synchronized void updateInfoAsItIsTyped(Participant sender, String textInBoxOLD, boolean updateISREMOVE){
           String textInBox ="";
           try{
              //textInBox = "PRESET"+textInBoxOLD.replaceAll("\n", "").replace("\n", "").replaceAll("\r\n", "").replaceAll("\r", "")+"ENDSET";
               textInBox = textInBoxOLD.replaceAll("\n", "").replace("\n", "").replaceAll("\r\n", "").replaceAll("\r", "");
           }catch(Exception e){e.printStackTrace();}
           
           
           
           
          
           
           MazeGameController2WAY mgc = getMazeGameControllerForDyad(sender);
           if(mgc==null)return;
           
           int mazeControllerNumber = getMazeControllerNumber(mgc);
           
           Participant pRecipient = this.getCurrentInterlocutor(sender);
           int mazeNo = mgc.getMazeNo();
           int moveNo= mgc.getMoveNo();
           int indivMveNo = mgc.getParticipantsMoveNo(sender);
           String info = this.getCVSPREFIX(mgc);
          
           
           
           System.out.println("contents are"+textInBox);
           
           
           
           
           
           
          
          
    }
    
     
    
    
    
    
    
    
      
    
     public static boolean showcCONGUI() {
        return false;
    }   
    

}
