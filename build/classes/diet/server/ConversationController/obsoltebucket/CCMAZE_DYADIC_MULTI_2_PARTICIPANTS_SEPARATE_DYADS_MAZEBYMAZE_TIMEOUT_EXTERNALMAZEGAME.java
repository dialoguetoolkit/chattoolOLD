package diet.server.ConversationController.obsoltebucket;
import diet.task.mazegame.MazeGameController2WAY;
import diet.task.mazegame.SetupIO;
import diet.task.mazegame.MazeGameLoadMazesFromJarFile;
import diet.message.*;

import diet.server.ConnectionListener;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.ConversationController.ui.JServerSendMessagesToClients;
import diet.server.Participant;
import diet.server.conversationhistory.turn.Turn;
import diet.task.TaskControllerInterface;
import diet.task.mazegame.message.MessageCursorUpdate;
import diet.task.mazegame.server.JMazeGameControlFrameLimitedControl;
import diet.utils.Conversion;
import java.awt.Color;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;




public class CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT_EXTERNALMAZEGAME  extends DefaultConversationController implements TaskControllerInterface{
    
    Vector participantsQueued = new Vector();
    
    Vector mazegameControllers = new Vector();
    Hashtable mazegameControllerLongTimeOfLatestChange = new Hashtable();  
    
   int numberOfCountdownSteps = 5;
    public long timePerMaze =  (5 * 60 * 1000) + (numberOfCountdownSteps*1000);           //620000;
    //public long timer = 80000;
    boolean doTiming = false;
    boolean pauseTiming = true;
    
    //int numberOfTimers =0;
    
    // JMazeGameControlFrame mgcf ;
     JMazeGameControlFrameLimitedControl mgcflc ;
     
     boolean isDisplayingInterimMessage = false;
    // ClariIntervention ci;
     boolean experimenthasstarted = false;
     
     
     
     String surveyADDRESS = "https://docs.google.com/forms/d/1ug0WfMGZjLm5cy6ScO9JdTCT2hSjz5f4l1_fUVR40Is/viewform?usp=send_form\n";
  
    String message = "The experiment is now finished.\nCould you please fill in the questionnaire.\nThe questionnaire should open automatically.\n"
            + "If it doesn't, please open a web-browser at the following address:\n\n"+surveyADDRESS;
    
    
    JServerSendMessagesToClients jssmtc = new JServerSendMessagesToClients(this,message,surveyADDRESS);
     
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
                          c.changeJProgressBar(mgc.pDirector, "CHATFRAME", "Out of time! Please wait", Color.GRAY, 0); 
                          c.changeJProgressBar(mgc.pMatcher, "CHATFRAME", "Out of time! Please wait", Color.GRAY, 0);
                          
                          
                          Thread t = new Thread(){ public void run(){
                              isDisplayingInterimMessage=true;
                              ////c.doCountdownToNextPartner(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "Out of time. Next maze","");
                              mgc.moveToNextMaze("NEXT MAZE: Ran out of time");
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
    
    public void addTimeToClock(long timetoAdd){
         for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      long timeOfLastChange =(Long) mazegameControllerLongTimeOfLatestChange.get(mgc);
                      mazegameControllerLongTimeOfLatestChange.put(mgc,timeOfLastChange+timetoAdd);
         }             
    }
    
    
    public void pauseTimer(){
        pauseTiming = !pauseTiming;
    }
    
    
    
    
    
    
    
    public static boolean showcCONGUI(){
       
        return true;
        
    } 

    public CCMAZE_DYADIC_MULTI_2_PARTICIPANTS_SEPARATE_DYADS_MAZEBYMAZE_TIMEOUT_EXTERNALMAZEGAME(Conversation c) {
        super(c);
        this.tc =this;
        //System.exit(-2343);
        String portNumberOfServer = ""+ConnectionListener.staticGetPortNumber();
        mgcflc = new JMazeGameControlFrameLimitedControl(this, "Server: "+portNumberOfServer);
        mgcflc.pack();
        mgcflc.setVisible(true);
       // ci = new ClariIntervention(c);
        this.doTiming = CustomDialog.getBoolean("Do you want to use timers?","Yes","No");
        if(this.doTiming){
            this.timePerMaze = CustomDialog.getLong("How many seconds per maze?",300 )*1000;
        }
        
        loadExternalMazeGame();
        CustomDialog.showModeLessDialog("Reminder: \nValid participantIDs are: ,\n"
                + "AAAAAA, BBBBBB, CCCCCC, DDDDDD, EEEEEE, FFFFFF, GGGGGG....", 10000);
        
    }
    
    
    
    
    

    
    
    Vector[] externalmazes ={new Vector(),new Vector()};
    
    
    public void loadExternalMazeGame(){
         //MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
         //mglmfj.getSetOf14MazesFromJar();         
         SetupIO sIO= new SetupIO();
         externalmazes= sIO.loadExternalPairOfMazeVectors();
        
    }
    
    
    

    @Override
    public boolean requestParticipantJoinConversation(String participantID) {
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
        
        
        if(participantID.equals("AAAAAA"))return true;
        if(participantID.equals("BBBBBB"))return true;
        if(participantID.equals("CCCCCC"))return true;
        if(participantID.equals("DDDDDD"))return true; 
        if(participantID.equals("EEEEEE"))return true;
        if(participantID.equals("FFFFFF"))return true;
        if(participantID.equals("GGGGGG"))return true;
        if(participantID.equals("HHHHHH"))return true;
        if(participantID.equals("IIIIII"))return true;
        if(participantID.equals("JJJJJJ"))return true;
        if(participantID.equals("KKKKKK"))return true;
        if(participantID.equals("LLLLLL"))return true;
        if(participantID.equals("MMMMMM"))return true;
        if(participantID.equals("NNNNNN"))return true;
        if(participantID.equals("OOOOOO"))return true;
        if(participantID.equals("PPPPPP"))return true;
        if(participantID.equals("QQQQQQ"))return true;
        if(participantID.equals("RRRRRR"))return true;
        if(participantID.equals("SSSSSS"))return true;
        if(participantID.equals("TTTTTT"))return true;
        if(participantID.equals("UUUUUU"))return true;
        if(participantID.equals("VVVVVV"))return true;
        if(participantID.equals("WWWWWW"))return true;
        if(participantID.equals("XXXXXX"))return true;
        //if(participantID.equals("YYYYYY"))return true;
        //if(participantID.equals("ZZZZZZ"))return true;
        
        
        return false;
         //return super.requestParticipantJoinConversation(participantID);
    }

    @Override
    public synchronized void participantJoinedConversation(final Participant p) {
        super.participantJoinedConversation(p);
        boolean added =false;
        for(int i=0;i<participantsQueued.size();i++){
            
            Participant pAlready = (Participant)participantsQueued.elementAt(i);
           // System.err.println(i+": "+pAlready.getParticipantID());
            if(pAlready.getParticipantID().compareToIgnoreCase(p.getParticipantID())>0){
                added =true;
                participantsQueued.insertElementAt(p,i );
                break;
            }
        }
        if(!added) participantsQueued.add(p);
        
        
        
        Conversation.printWSlnLog("Main", "------------");
        Conversation.printWSlnLog("Main", "ParticipantID: "+p.getParticipantID()+" Username: "+p.getUsername()+" logged in.");
        Conversation.printWSlnLog("Main", "List of all logged in participants below:");
        
        for(int i=0;i<participantsQueued.size();i++){
            
            Participant pAlready = (Participant)participantsQueued.elementAt(i);
            System.err.println(i+": "+pAlready.getParticipantID());
            Conversation.printWSlnLog("Main", i+") ParticipantID: "+pAlready.getParticipantID()+" Username: "+pAlready.getUsername());
            
            
            
        }
        if(participantsQueued.size()==1){
            Conversation.printWSlnLog("Main", "There is "+participantsQueued.size()+" participant logged in."); 
        }
        else{
           Conversation.printWSlnLog("Main", "There are "+participantsQueued.size()+" participants logged in. (see list immediately above)");
        }
        
        
        //Conversation.printWSln("Main", "ID: "+p.getParticipantID()+" Username:"+p.getUsername()+" is ready to start. Total number: "+participantsQueued.size()+" participants");
        if(2<5)return;
        //if(c.getParticipants().getAllParticipants().size()>7) this.startExperiment();
        /*
        if(queuedA==null){
            queuedA = p;
        }
        else {
             if(mgcBASE==null){
                  mgcBASE =   new MazeGameController2WAY(c,true);
                  mgcBASE.startNewMazeGame(queuedA, p);
                  this.mazegameControllers.addElement(mgcBASE);
                  this.mazegameControllerLongTimeOfLatestChange.put(mgcBASE, new Date().getTime());
             }
             else{
                  MazeGameController2WAY mgcN = new MazeGameController2WAY(c,mgcBASE.getP1Mazes_Cloned(),mgcBASE.getP2Mazes_Cloned());
                  mgcN.startNewMazeGame(queuedA, p);
                  this.mazegameControllers.addElement(mgcN);
                  this.mazegameControllerLongTimeOfLatestChange.put(mgcN, new Date().getTime());
             }
             
             final Participant pA = queuedA;
             final Participant pB = p;
            
             
             queuedA=null;
            
            
        }
        */
    }
 
     //Participant queuedA = null;
     //Participant queuedB = null;
     //MazeGameController2WAY mgcBASER;
     
     
    
             
     
    
    
    
    
     
    public void startExperiment(){
         try{
         MazeGameController2WAY mgc =null;
         System.err.println("STARTING EXPERIMENT1");
         while (this.participantsQueued.size()>1){
             Participant pA = (Participant)this.participantsQueued.elementAt(0);
             Participant pB = (Participant)this.participantsQueued.elementAt(1);
             Conversation.printWSln("Main", "Pairing "+pA.getUsername()+"  with "+pB.getUsername());
             if(mgc==null){
                 
                  //MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
                            
                 
                 // MazeGameLoadMazesFromJarFile mglmfj = new MazeGameLoadMazesFromJarFile();
                  //mglmfj.getSetOf9MazesFromJar();
                  //mglmfj.getSetOf14MazesFromJar();         
                 mgc = new MazeGameController2WAY(c,externalmazes[0],externalmazes[1]);
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
    
    
    
    
    

    @Override
    public void participantRejoinedConversation(Participant p) {
       try{ 
        super.participantRejoinedConversation(p);
        MazeGameController2WAY mgc = this.getMazeGameControllerForDyad(p);
        Participant pPartner = this.getCurrentInterlocutor(p);
        if(pPartner==null)return;
        mgc.reconnectParticipant(p);
        c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(pPartner, "Network error. Your partner has logged back in. Resetting to the start of the maze",0,sm.defaultFONTSETTINGSSERVER);
        c.deprecatedsendArtificialTurnToRecipientWithEnforcedTextColour(p, "Please continue", 0,sm.defaultFONTSETTINGSSERVER);
        mgc.appendToUI("PARTICIPANT: "+p.getUsername()+" IS LOGGING BACK IN. CHECK NETWORK CABLES!");
       }catch (Exception e){
           e.printStackTrace();
           Conversation.printWSlnLog("Main", "Error trying to find Maze Game Controller for participant "+p.getUsername()+" who is trying to log back in. If this happens BEFORE the experiment has started, this is OK, because the participants haven't been assigned a mazegame controller yet.");
       }
    }
    
   
    
    
    
    
    
   public synchronized MazeGameController2WAY getMazeGameControllerForDyad(Participant p){
        for(int i=0;i<mazegameControllers.size();i++){
                      MazeGameController2WAY mgc = (MazeGameController2WAY) mazegameControllers.elementAt(i);
                      if(mgc.pDirector==p) return mgc;
                      if(mgc.pMatcher==p) return mgc;
                  }
       return null;     
   }
   
   
   public synchronized Participant  getCurrentInterlocutor(Participant p){
       
        try{
       MazeGameController2WAY mgc = this.getMazeGameControllerForDyad(p);
       if(mgc==null)return null;
       if(mgc.pDirector==p)return mgc.pMatcher;
       if(mgc.pMatcher==p)return mgc.pDirector;
       
       
      
        
       }catch(Exception e){
            Conversation.printWSln("Main", "Serious error...returning no partner for: "+p.getUsername());
           e.printStackTrace();}  
       
       return null;
       
   }
   
   
  

    public void closeDown() { }
    
   
   
    
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
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns( pp.getSubdialogueID(mgc.pDirector)   ,"DATA", mgc.pDirector.getParticipantID(), mgc.pDirector.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
                 c.deprecated_saveAdditionalRowOfDataToSpreadsheetOfTurns(  pp.getSubdialogueID(mgc.pDirector) ,"DATA", mgc.pMatcher.getParticipantID(), mgc.pMatcher.getUsername(),  finishTime, finishTime, finishTime, "MAZECOMPLETE" +mgc.getMazeNo(), new Vector());
             
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
         
         
         
         if(mazeIsCompleted ){
             
             this.mazegameControllerLongTimeOfLatestChange.put(mgc, new Date().getTime());
              c.changeJProgressBar(mgc.pDirector, "CHATFRAME", "Completed. Please wait", Color.GRAY, 0); 
              c.changeJProgressBar(mgc.pMatcher, "CHATFRAME", "Completed. Please wait", Color.GRAY, 0);
             
             Thread t = new Thread(){ public void run(){
                           isDisplayingInterimMessage=true;
                           ////c.doCountdownToNextPartner(mgc.pDirector, mgc.pMatcher,numberOfCountdownSteps, "Completed the maze. New maze","");
                           mgc.moveToNextMaze();
                           isDisplayingInterimMessage=false;
             } }; t.start();
         }
               
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
           c.deprecated_relayMazeGameTurnToParticipantWithEnforcedColour(sender, pRecipient, mct, mazeNo, moveNo, indivMveNo,sm.defaultFONTSETTINGSSELF);
           //c.relayMazeGameTurnToParticipant(sender, pRecipient, mct, mazeNo, moveNo, indivMveNo);
           //c.sendLabelDisplayToParticipantInOwnStatusWindow(pRecipient, "Status:OK", false);
           mgc.appendToUI(sender.getUsername()+": "+mct.getText());
           
           
           
    }
    
    
    
    
    
    boolean saveKeypress = true;
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
          if(saveKeypress)c.newsaveClientKeypressToFile(sender, mkp);
    }

    public void stopSavingKeypress(){
        saveKeypress=false;
        Conversation.printWSln("Main", "STOPPING the saving of all keypresses");
    }
    public void startSavingKeypress(){
        saveKeypress=true;
        Conversation.printWSln("Main", "STARTING the saving of all keypresses");
    }
    
    @Override
    public synchronized void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
          //c.informIsTypingToAllowedParticipants(sender);
         
         Participant pRecipient = this.getCurrentInterlocutor(sender);
         if(pRecipient==null)return;
         //if(pRecipient==null)System.exit(-5);
         //if(sender==null)System.exit(-3);
         //c.informParticipantBthatParticipantAIsTyping(sender, pRecipient);
       
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        
    }
    
    

   
   

   
    
    
    
    
    
    
   
    
   
    
    
    
    

}
