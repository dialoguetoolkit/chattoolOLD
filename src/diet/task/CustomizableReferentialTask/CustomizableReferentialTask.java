/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.CustomizableReferentialTask;

import diet.attribval.AttribVal;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import diet.task.ProceduralComms.JTrialTimerActionRecipientInterface;
import diet.utils.HashtableWithDefaultvalue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 *
 * @author gj
 */
public class CustomizableReferentialTask implements JTrialTimerActionRecipientInterface {

    JPauser jjp =new JPauser();

    DefaultConversationController cC;
    
   // JTrialTimer  jtt;// = new JTrialTimer(this, 10000);
    
   // double probabOfSame= 0.5;//CustomDialog.getDouble("Enter the probability of SAME stimuli", 0, 1, 0.5);
    public Participant pA;
    public Participant pB;
    Random r = new Random();
    final long durationOfStimulus =  CustomDialog.getLong("How long should the stimuli be displayed for?", 5000);
    //boolean blockTextEntryDuringStimulus = true;// = CustomDialog.getBoolean("Block text entry while stimulus is displayed?", "block", "do not block");
    
    //String   pA_Imagename;
    //String   pB_Imagename;
    //String   permittedSelector;
    //String[] validTokens;
    
    String[] currentTrial;
    
    boolean currentsethasbeensolved = false;
    
    public HashtableWithDefaultvalue htscoreCORRECT = new HashtableWithDefaultvalue(0);
    public HashtableWithDefaultvalue htscoreINCORRECT = new HashtableWithDefaultvalue(0);
    
    public HashtableWithDefaultvalue htPOINTS = new HashtableWithDefaultvalue((double)0);
    long gamenumber=0;
    
    double correctscoreinrement = 10;//CustomDialog.getDouble("What is the score increment for correct guesses?", 10);
    double incorrectpenalty = 5;//CustomDialog.getDouble("What is the point penalty for incorrect guesses?", 5);
    
   
    boolean showButtons = CustomDialog.getBoolean("Do you want to show buttons underneath the stimuli on the clients?", "Buttons", "No Buttons");
    
    String displayname = "instructions";     
    
    boolean showFeedbackToDirector = false;
    boolean showFeedbackToMatcher = false;
      
    String option="tangramlist01.txt";
    String directoryname =  "tangramset01";//"tangramset02directortraining";
      
    int stimuluswidth = -1;
    int stimulusheight=-1;   
    
    boolean isinphysicalfolder = false;
    
    public Vector<String[]> vstimuli = new Vector();
    public Vector<String[]> vstimuliFULL = new Vector();
    
    public CustomizableReferentialTask(DefaultConversationController cC) {
        //durationOfStimulus = 5000;//CustomDialog.getLong("How long should the stimuli be displayed for?", 5000);
        //blockTextEntryDuringStimulus = CustomDialog.getBoolean("Block text entry while stimulus is displayed?", "block", "do not block");
        this.cC=cC;
        //jtt = new JTrialTimer(this, 10000);
    }
    public CustomizableReferentialTask(DefaultConversationController cC, long durationOfStimulus){
         //this.durationOfStimulus=durationOfStimulus;
         this.cC=cC;      
         loadFromFile();
         
         //loadStimuliList();
         Dimension d =  getImageHeights();
         if(d.width==-1||d.height==-1) {
             CustomDialog.showDialog("The server can't find the images in the JAR file\nTry recompiling the project!");
         }
         stimuluswidth=d.width;
         stimulusheight=d.height;
    }
 
    
    
   private void loadFromFile(){
       String userdir = System.getProperty("user.dir");
       String directory = (userdir+File.separator+"experimentresources"+ File.separator+ "stimuli");
       
       File stimulisequence = CustomDialog.loadFile(directory, "Choose the text file containing the sequences of stimuli. \nIt must be in the same directory as the stimuli", null);
       String foldername = stimulisequence.getParentFile().getName();
       this.directoryname=foldername;
       if(stimulisequence==null)CustomDialog.showDialog("Couldn't find the file!");
       Vector data = new Vector();
        try{
            BufferedReader br = new BufferedReader(new FileReader(stimulisequence)); 
            String st;
            String header = br.readLine();
            while ((st = br.readLine()) != null){ 
                 System.out.println(st);
                 
                 st= st.replace(" ", "");
                 String[] row =  st.split("¦");
                 data.addElement(row);
                 System.out.println("Split to:");
                 for(int i =0;i<row.length;i++){
                     System.out.print(row[i]+ "|");
                 }
                 System.out.println("");
                  
                 
                 
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
      
        this.vstimuli=(Vector<String[]>)data.clone();
        this.vstimuliFULL=(Vector<String[]>)data.clone();
   }
  
    
      
  
    private void loadStimuliList(){
         String[] round1 = {"D0_A_0.png", "set0.png", "2", "1,2,3,4,5,6,7,8",  "1"};
         String[] round2 = {"set1.png",  "D0_B_1.png","1", "1,2,3,4,5,6,7,8",  "5"};
         String[] round3 = {"D0_C_2.png", "set2.png", "2", "1,2,3,4,5,6,7,8",  "1"};
         String[] round4 = {"D0_A_0.png", "set0.png", "2", "1,2,3,4,5,6,7,8",  "1"};
         String[] round5 = {"set1.png",  "D0_B_1.png","1", "1,2,3,4,5,6,7,8",  "5"};
         String[] round6 = {"D0_C_2.png", "set2.png", "2", "1,2,3,4,5,6,7,8",  "1"};
         
         vstimuli.add(round1);
         vstimuli.add(round2);
         vstimuli.add(round3);
         vstimuli.add(round4);
         vstimuli.add(round5);
         vstimuli.add(round6);
         
         vstimuliFULL = (Vector<String[]>)vstimuli.clone();
         
    }
    
    
    public Dimension getImageHeights(){   
        String firstfilename = directoryname+"/"+vstimuliFULL.elementAt(0)[0];
        System.err.println("Trying to get image heights for: "+firstfilename);
        BufferedImage bimg =null ;
        try {   
             ClassLoader cldr = CustomizableReferentialTask.class.getClassLoader();
             URL url = cldr.getResource(firstfilename);
             bimg = ImageIO.read(url);
             int width = bimg.getWidth();
             int height = bimg.getHeight();
             return (new Dimension(width,height)); 
       } catch (IOException ex) {
            // handle exception...
           ex.printStackTrace();   
       } 
        return new Dimension(-1,-1); 
    }   
    
    
    
    private void loadReferentlist(String option){
      
       InputStream inp = this.getClass().getResourceAsStream("/"+option);  
       try {    
        BufferedReader br = new BufferedReader(new InputStreamReader(inp, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            String[] splitline = line.split(" ");
            this.vstimuli.addElement(splitline);
             
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
            
        }
        String everything = sb.toString();
       }
        catch (Exception e){
        e.printStackTrace();
    }
   }
    
    
    
    
    
    
  
    public boolean participantCanMakeChoice(Participant p){
        //if(2<5)return true;
        if(currentTrial[2].equalsIgnoreCase("B")) return true;
        if(p==pA && currentTrial[2].equalsIgnoreCase("1")) return true;
        if(p==pB && currentTrial[2].equalsIgnoreCase("2")) return true;
        return false;
    }
    
    public String[] getButtonsFromOptions(){
        String validTokens = currentTrial[3];
        validTokens=validTokens.replace(" ", "");
        String[] validTokensArray = validTokens.split(",");
        return validTokensArray;
    }
   
    
    public void  participantRejoined(Participant p){
        if(p==pA){
             cC.c.showStimulusImageFromJarFile_InitializeWindow(pA, this.stimuluswidth, this.stimulusheight, "",false, emptybuttons);
             if(this.showButtons){
                 cC.c.showStimulusImageEnableButtons(pA, this.getButtonsFromOptions(), false);
             }else{
                 cC.c.showStimulusImageEnableButtons(pA,emptybuttons, false);
             }
             
             
             
        }
        else{
              cC.c.showStimulusImageFromJarFile_InitializeWindow(pB, stimuluswidth, stimulusheight, "",false, emptybuttons);
              if(this.showButtons){
                 cC.c.showStimulusImageEnableButtons(pB, this.getButtonsFromOptions(), false);
             }else{
                 cC.c.showStimulusImageEnableButtons(pB,emptybuttons, false);
             }
        }
    }
    
    
    String[] emptybuttons ={};
    
    public void startTask(Participant pA, Participant pB){
            if(pA.getParticipantID().contains("1")){
               this.pA=pA;
               this.pB=pB;    
            }
            else{
               this.pB=pA;
               this.pA=pB; 
            }
        cC.c.showStimulusImageFromJarFile_InitializeWindow(pA, stimuluswidth, stimulusheight, "",this.isinphysicalfolder,emptybuttons);
        cC.c.showStimulusImageFromJarFile_InitializeWindow(pB, stimuluswidth, stimulusheight, "",this.isinphysicalfolder,emptybuttons);
        
        try{
            //Thread.sleep(10000);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //cC.c.showStimulusImageEnableButtons(pB, emptybuttons,  true);
        //cC.c.showStimulusImageEnableButtons(pA, emptybuttons, true);
       
        
//doCountdowntoNextSet_DEPRECATED("Please start!","Next face in " );
        
        Thread t = new Thread(){
            public void run(){
                gameloop();
            }
        };
      
        
        t.start();
    }
    
    
    
   
  
    
     
     
     
     
     public boolean hasLoopedThroughAllFaces = false;
    
     
     
      private void checkIfHasLoopedThroughAll(){
          
          
         while(vstimuli.size()==0){
                      CustomDialog.showDialog("Experiment finished!!");                     
                      //this.hasLoopedThroughAllFaces=true;
                      //vpairs=(Vector)vpairsFULL.clone();
                      System.err.println("HAS LOOPED THROUGH ALL OF THE FACES!");
                      Conversation.printWSln("Main", "Has looped through all the faces");
          }
     }
     
     
      
      
      
      
     
     private void loadNextStimulusSetSet(String directoryname){
                  checkIfHasLoopedThroughAll();   
                  this.currentTrial = this.vstimuli.elementAt(0);
                  vstimuli.remove(currentTrial);
                  
                  if(participantCanMakeChoice(pA)){
                     if(showButtons)cC.c.showStimulusImageReplaceWithNewButtons(pA, this.getButtonsFromOptions(), true );    
                  }
                  else{
                     if(showButtons) cC.c.showStimulusImageReplaceWithNewButtons(pA, emptybuttons, true );  
                  }
                  if(participantCanMakeChoice(pB)){
                      if(showButtons) cC.c.showStimulusImageReplaceWithNewButtons(pB, this.getButtonsFromOptions(), true );
                  }
                  else{
                      if(showButtons) cC.c.showStimulusImageReplaceWithNewButtons(pB, emptybuttons, true );  
                  }
                  Conversation.printWSln("Main", "THE FACES ARE:"  + this.currentTrial[0]+"----"+this.currentTrial[1]);
                  
                  
                  
                  
          
     }
    
    
    
     
     
     //boolean isInTransitionBetweenGames = false;
     
     
     long startOfCurrentGame = new Date().getTime();
     long durationOfGame = CustomDialog.getLong("How long is a game?", 60000);
     
     
     
     public synchronized void gameloop(){
          try{
             //this.doCountdowntoNextSet_Step1_Countdown("Your score is: ","Time till next image: ",false);
             this.doCountdowntoNextSet_Step2_LoadNextSet();
             //this.doCountdowntoNextSet_Step3_StopShowingStimulus();
             this.doCountdowntoNextSet_Step4_ShowMessageAtStartOfTrial();
          }catch(Exception e){
              e.printStackTrace();
          }   
          
          long bonustimethisgame = 0;
          startOfCurrentGame = new Date().getTime();
          while(2<5){
              //
              try{
                  
                 wait(250);
                 
                 long time1 = new Date().getTime();
                 boolean ispaused = this.jjp.isPaused();
                 while(ispaused){
                     ispaused = this.jjp.isPaused();
                     wait(500);
                     bonustimethisgame=bonustimethisgame+500;
                 }
                 
                 
                 long durationOfGameSoFar = new Date().getTime()-startOfCurrentGame;
                 long timeRemaining =  bonustimethisgame +   durationOfGame - durationOfGameSoFar;
                 if(timeRemaining<0)timeRemaining =0;
                 
                 Color progressBar = Color.red;
                 long percentage = (100*timeRemaining)/durationOfGame;
                 
                 System.err.println("durationOfGameSoFar "+durationOfGameSoFar);
                 System.err.println("timeremaining "+timeRemaining);
                 System.err.println("durationofgame "+durationOfGame);
                 
                  int timeRemainingSeconds = (int)timeRemaining/1000;
                  
                  cC.c.changeJProgressBar(pA, "CHATFRAME",  timeRemainingSeconds+"", new Color(150,150,255), (int)percentage);
                  cC.c.changeJProgressBar(pB, "CHATFRAME",  timeRemainingSeconds+"", new Color(150,150,255), (int)percentage);
                 
                 
                 if(this.currentsethasbeensolved){                    
                     this.doCountdowntoNextSet_Step1_Countdown("Your score is: ","Time till next image: ", false, false);
                     this.doCountdowntoNextSet_Step2_LoadNextSet();
                     this.startOfCurrentGame= new Date().getTime();
                      bonustimethisgame=0;
                     //this.doCountdowntoNextSet_Step3_StopShowingStimulus();
                     this.doCountdowntoNextSet_Step4_ShowMessageAtStartOfTrial();
                 }   
                 else if(timeRemaining<=0){
                     this.jjp.addTextln("TIMEOUT");
                     cC.c.sendInstructionToParticipant(pA, "You ran out of time!" );
                     cC.c.sendInstructionToParticipant(pB, "You ran out of time!" );
                     this.doCountdowntoNextSet_Step1_Countdown("Your score is: ","Time till next image: ",false, false);
                     this.doCountdowntoNextSet_Step2_LoadNextSet();
                     this.startOfCurrentGame= new Date().getTime();
                      bonustimethisgame=0;
                     //this.doCountdowntoNextSet_Step3_StopShowingStimulus();
                     this.doCountdowntoNextSet_Step4_ShowMessageAtStartOfTrial();
                 }
                 
                 
                 //check if game is complete
                 //if game is not complete
              }
              catch(Exception e){
                  e.printStackTrace();
              }
                             
          }
         
         
     }
     
     
     
     
     
     
     
     
     
     public void doCountdowntoNextSet_Step1_Countdown(final String  firstmessage,final  String countdownmessageprefix, boolean showTimerInChatWindow  ,boolean blocktextentry) throws Exception{
                  
                  //String imageName1 = directoryname+"/"+currentTrial[0];
                  //String imageName2 = directoryname+"/"+currentTrial[1];
                
                  cC.c.sendInstructionToParticipant(pA, "Loading next set");
                  cC.c.sendInstructionToParticipant(pB, "Loading next set");
                 
                     
                  if(this.showButtons){
                      cC.c.showStimulusImageEnableButtons(pB, this.getButtonsFromOptions(), false);
                      cC.c.showStimulusImageEnableButtons(pA, this.getButtonsFromOptions(), false);
                  }
                  else{
                      cC.c.showStimulusImageEnableButtons(pB, emptybuttons, false);
                      cC.c.showStimulusImageEnableButtons(pA, emptybuttons, false);
                  }
               
                  
                  
               //   cC.c.showStimulusImageFromJarFile_ChangeImage(pA, directoryname+"/"+this.currentTrial[0],this.isinphysicalfolder, 1); //Making the image disappear
               //   cC.c.showStimulusImageFromJarFile_ChangeImage(pB, directoryname+"/"+this.currentTrial[1], this.isinphysicalfolder, 1); //Making the image disappear
                  
                  
                  
                  if(blocktextentry){
                     cC.c.changeClientInterface_clearMainWindows(pA);
                     cC.c.changeClientInterface_clearMainWindows(pB);
                     cC.c.changeClientInterface_disableTextEntry(pA);
                     cC.c.changeClientInterface_disableTextEntry(pB);
                  } 
                  
                
                  
                  
                
                  Thread.sleep(1000);
                  
                  if(blocktextentry)   cC.c.changeClientInterface_clearMainWindows(pA);
                   if(blocktextentry) cC.c.changeClientInterface_clearMainWindows(pB);
                   if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","" ,false, pA,pB );
                  
                  //cC.c.changeClientInterface_backgroundColour(pA, Color.white);
                  //cC.c.changeClientInterface_backgroundColour(pB, Color.white);
                  //cC.c.newsendInstructionToParticipant(pA, "Please " );
                   
                  //cC.c.newsendInstructionToParticipant(pB, messageprefix + "1 seconds" );
                  
                  //cC.c.showStimulusImageFromJarFile_ChangeImage(pA, "faceset1/face1.png", 4000);
                  //cC.c.showStimulusImageFromJarFile_ChangeImage(pB, "faceset1/face1.png", 4000);
                  
                 
         
     }
     
     public void pause(){
         jjp.setPaused();
     }
     
     
     public void doCountdowntoNextSet_Step2_LoadNextSet(){
         loadNextStimulusSetSet(this.directoryname);     
         gamenumber++;
         cC.c.showStimulusImageFromJarFile_ChangeImage(pA, directoryname+"/"+this.currentTrial[0], this.isinphysicalfolder,durationOfStimulus);
         cC.c.showStimulusImageFromJarFile_ChangeImage(pB, directoryname+"/"+this.currentTrial[1], this.isinphysicalfolder,durationOfStimulus);  
        
         this.jjp.addTextln("");
         this.jjp.addTextln("Gamenumber: "+gamenumber + "/"+this.vstimuliFULL.size());
         this.jjp.addTextln(pA.getParticipantID()+","+pA.getUsername()+" "+directoryname+"/"+this.currentTrial[0]);
         this.jjp.addTextln(pB.getParticipantID()+","+pB.getUsername()+" "+directoryname+"/"+this.currentTrial[1]);
         this.jjp.addTextln("Target: "+currentTrial[4]);
         
         currentsethasbeensolved = false;         
     }
     
     
     
     public void doCountdowntoNextSet_Step3_StopShowingStimulus() throws Exception{
                 
                  
                       try{
                            //Thread.sleep(durationOfStimulus);
                            cC.c.changeClientInterface_enableTextEntry(pA);
                            cC.c.changeClientInterface_enableTextEntry(pB);
                            
                             int pAPercentageCorrect=0;
                             int pANumberCorrect = getScoreCORRECT(pA);
                             int pANumberINCorrect = getScoreINCORRECT(pA);
                             int pBPercentageCorrect=0;
                             int pBNumberCorrect = getScoreCORRECT(pB);
                             int pBNumberINCorrect = getScoreINCORRECT(pB);
                             if(gamenumber>1){
                                 pAPercentageCorrect= (100*pANumberCorrect) / (pANumberCorrect+pANumberINCorrect);
                                 pBPercentageCorrect= (100*pBNumberCorrect) / (pBNumberCorrect+pBNumberINCorrect);
                            }
                  
                            //if(blockTextEntryDuringStimulus)cC.c.newsendInstructionToParticipant(pA, "Your success rate is: "+ pAPercentageCorrect+   "%"  );
                            //if(blockTextEntryDuringStimulus)cC.c.newsendInstructionToParticipant(pB, "Your success rate is: "+ pBPercentageCorrect+   "%"  );
                            
                              DecimalFormat myFormatter = new DecimalFormat("############.#");
                             String outputPA = myFormatter.format( (double)  htPOINTS.getObject(pA));
                             String outputPB = myFormatter.format( (double)  htPOINTS.getObject(pB));
                             
                            // cC.c.newsendInstructionToParticipant(pA, "Your score is: "+ outputPA );
                            // cC.c.newsendInstructionToParticipant(pB, "Your score is: "+ outputPB );
                  
                            
                         
                            //cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ outputPA ,true, pA);
                            //cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+  outputPB,true, pB);
                      
                             
                             if(this.showButtons){
                                 cC.c.showStimulusImageEnableButtons(pB, this.getButtonsFromOptions(), false);
                                 cC.c.showStimulusImageEnableButtons(pA, this.getButtonsFromOptions(), false);
                             }
                             else{
                                 cC.c.showStimulusImageEnableButtons(pB, emptybuttons, false);
                                 cC.c.showStimulusImageEnableButtons(pA, emptybuttons, false);
                             }
                             
                             
                            
                                 //cC.c.showStimulusImageEnableButtons(pB, buttons2, true);
                                 //cC.c.showStimulusImageEnableButtons(pA, buttons, true);
                                 //if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ (double)  htPOINTS.getObject(pA) +"\n",true, pA );
                                 //if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ (double)  htPOINTS.getObject(pB) +"\n",true, pB );
                                 //if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Choose same or different:"+"\n",false, pA,pB );
                            
                           // cC.c.newsendInstructionToParticipant(pA,"enter '/s' if you saw the same face");
                           // cC.c.newsendInstructionToParticipant(pA,"enter '/d' if you saw different faces");
                           //  cC.c.newsendInstructionToParticipant(pB,"enter '/s' if you saw the same face");
                           // cC.c.newsendInstructionToParticipant(pB,"enter '/d' if you saw different faces");
                            
                            
                       }catch(Exception eee){
                           eee.printStackTrace();
                       }
                    
                  
     }
     
     
     
     
      public void doCountdowntoNextSet_Step4_ShowMessageAtStartOfTrial() throws Exception{
                 
                  
                            
                             int pAPercentageCorrect=0;
                             int pANumberCorrect = getScoreCORRECT(pA);
                             int pANumberINCorrect = getScoreINCORRECT(pA);
                             int pBPercentageCorrect=0;
                             int pBNumberCorrect = getScoreCORRECT(pB);
                             int pBNumberINCorrect = getScoreINCORRECT(pB);
                        
                            
                              DecimalFormat myFormatter = new DecimalFormat("############.#");
                             String outputPA = myFormatter.format( (double)  htPOINTS.getObject(pA));
                             String outputPB = myFormatter.format( (double)  htPOINTS.getObject(pB));
                             
                            // cC.c.newsendInstructionToParticipant(pA, "Your score is: "+ outputPA );
                            // cC.c.newsendInstructionToParticipant(pB, "Your score is: "+ outputPB );
                  
                            
                         
                            cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ outputPA ,true, pA);
                            cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+  outputPB,true, pB);
                      
                             
                             if(this.showButtons){
                                 cC.c.showStimulusImageEnableButtons(pB, this.getButtonsFromOptions(), true);
                                 cC.c.showStimulusImageEnableButtons(pA, this.getButtonsFromOptions(), true);
                             }
                             else{
                                 cC.c.showStimulusImageEnableButtons(pB, emptybuttons, false);
                                 cC.c.showStimulusImageEnableButtons(pA, emptybuttons, false);
                             }
                             
                             
                            
                                  
                                // if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ (double)  htPOINTS.getObject(pA) +"\n",true, pA );
                                // if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Your score is: "+ (double)  htPOINTS.getObject(pB) +"\n",true, pB );
                                // if(displayname!=null) cC.c.textOutputWindow_ChangeText("instructions","Choose same or different:"+"\n",false, pA,pB );
                            
                             //cC.c.newsendInstructionToParticipant(pA,"enter '/s' if you saw the same face");
                            // cC.c.newsendInstructionToParticipant(pA,"enter / followed by the number");
                            //cC.c.newsendInstructionToParticipant(pB,"enter '/s' if you saw the same face");
                            //cC.c.newsendInstructionToParticipant(pB,"enter '/d' if you saw different faces");
                            
                            
                    
                  
     }
     
     
     
     
     
       
     
    
    
      
    
    public void dbg(Participant sender, String text){
        if(2<5)return;
        cC.c.sendInstructionToParticipant(sender,text);
    }
    
    
    public synchronized void processButtonPress(Participant p, String choice){
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("buttonpress", p, choice);
        this.processChatText(p, "/"  + choice);
    }
    
    public synchronized void processChatText(Participant sender, String txt){
        if(!txt.startsWith("/"))return;     
        if(this.currentsethasbeensolved){
            cC.c.sendInstructionToParticipant(sender,"The current set has already been solved");
            return;
            
        }
        String command=txt.replace("\n", "");
        command=command.replace(" ", "");
        command=command.toUpperCase();
        command = command.replace("/", "");
        
        this.jjp.addTextln(sender.getParticipantID()+" "+ sender.getUsername() + " "+ "Selected: "+command);
        
        String permittedSender = currentTrial[2];
        
        //First check if participant is allowed to make the selection
        
        this.dbg(sender, "Permittedsender: "+permittedSender);
        this.dbg(sender,"command: "+command);
         
        if(permittedSender.equalsIgnoreCase("1") && sender==pB) {
            cC.c.sendInstructionToParticipant(sender,"The other participant needs to make the selection!");
            return;
        }
        if(permittedSender.equalsIgnoreCase("2") && sender==pA) {
            cC.c.sendInstructionToParticipant(sender,"The other participant needs to make the selection!");
            return;
        }
        
        
        
        //Next check if the command is permitted
        
        String validTokens = currentTrial[3];
        validTokens=validTokens.replace(" ", "");
        String[] validTokensArray = validTokens.split(",");
        boolean isValidToken = false;
        for(int i=0;i<validTokensArray.length;i++){
            if(validTokensArray[i].equalsIgnoreCase(command))isValidToken = true;
            this.dbg(sender, "validtokens: "+validTokensArray[i]);
        }  
       
        
        if(isValidToken){   
            boolean selectionCorrect=false;
            if(command.equalsIgnoreCase(currentTrial[4])){
                 selectionCorrect=true;
            }
            if(selectionCorrect){
                
                
                 this.updateScores(true);
                  String scoreA = " Score: "+(Double)this.htPOINTS.getObject(pA);
                  String scoreB = " Score: "+(Double)this.htPOINTS.getObject(pB);
                  if(showFeedbackToDirector) cC.c.sendInstructionToParticipant(pA, "CORRECT! " + scoreA);
                  if(showFeedbackToMatcher)cC.c.sendInstructionToParticipant(pB, "CORRECT! " + scoreB);
                 
                 this.currentsethasbeensolved=true;
                 //doCountdowntoNextSet_DEPRECATED("CORRECT! They are the SAME", "Next face in "  );
                
            }
            else{
                this.updateScores(false);
                 String scoreA = " Score: "+(Double)this.htPOINTS.getObject(pA);
                 String scoreB = " Score: "+(Double)this.htPOINTS.getObject(pB);
                 if(showFeedbackToDirector)cC.c.sendInstructionToParticipant(pA, "INCORRECT! " + scoreA);
                 if(showFeedbackToMatcher)cC.c.sendInstructionToParticipant(pB, "INCORRECT! " + scoreB);
                this.currentsethasbeensolved=true;
                //doCountdowntoNextSet_DEPRECATED("INCORRECT! They are  DIFFERENT","Next face in " );
                
            }
        
        }
        else{
            cC.c.sendInstructionToParticipant(sender,"Invalid command.");
            cC.c.sendInstructionToParticipant(sender,"Please reread the experiment istructions");
                   
        }
    }
    
    
    
    
   
    
    
    public void updateScores(boolean success){
        if(success){
            this.jjp.addTextln("CORRECT");
            
            int scorepAsuccess = this.getScoreCORRECT(pA);
            this.htscoreCORRECT.putObject(pA, scorepAsuccess+1);
            
            int scorepBsuccess = this.getScoreCORRECT(pB);
            this.htscoreCORRECT.putObject(pB, scorepAsuccess+1);
            
            
            double score = (double)  this.htPOINTS.getObject(pA);
            score=score+correctscoreinrement;
            
            this.htPOINTS.putObject(pA,score);
            score = (double)  this.htPOINTS.getObject(pB);
            score=score+correctscoreinrement;
            this.htPOINTS.putObject(pB,score);
            
        }
        else{
            this.jjp.addTextln("INCORRECT");
             int scorepAsuccess = this.getScoreINCORRECT(pA);
            this.htscoreINCORRECT.putObject(pA, scorepAsuccess+1);
            
            int scorepBsuccess = this.getScoreINCORRECT(pB);
            this.htscoreINCORRECT.putObject(pB, scorepAsuccess+1);
            
            double score = (double)  this.htPOINTS.getObject(pA);
            score=score-this.incorrectpenalty;
            if(score<0)score=0;
            this.htPOINTS.putObject(pA,score);
            score = (double)  this.htPOINTS.getObject(pB);
            score=score-this.incorrectpenalty;
            if(score<0)score=0;
            this.htPOINTS.putObject(pB,score);
            
            
        }
        
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_correct", pA, ""+this.getScoreCORRECT(pA));
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_correct", pB, ""+this.getScoreCORRECT(pB));
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_incorrect", pA, ""+this.getScoreINCORRECT(pA));
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_incorrect", pB, ""+this.getScoreINCORRECT(pB));
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_points", pA, ""+(double)  this.htPOINTS.getObject(pA));
        cC.c.saveAdditionalRowOfDataToSpreadsheetOfTurns("gamedata_score_points", pB, ""+(double)  this.htPOINTS.getObject(pB));
        
        Conversation.printWSln("Main", pA.getUsername()+" has "+(double)  this.htPOINTS.getObject(pA));
        Conversation.printWSln("Main", pB.getUsername()+" has "+(double)  this.htPOINTS.getObject(pB));
        
    }
    
   
    
    
    
    public int getScoreCORRECT(Participant p){
        return (int)this.htscoreCORRECT.getObject(p);
    }
     public int getScoreINCORRECT(Participant p){
        return (int)this.htscoreINCORRECT.getObject(p);
    }
    
    public Vector getAdditionalValues(Participant p){
        
         String stimulusself ="";
         String stimulusother ="";
         Vector avs = new Vector();
         if(pA==p){
             stimulusself=directoryname+"/"+this.currentTrial[0];
             stimulusother=directoryname+"/"+this.currentTrial[1];
             AttribVal av0 = new AttribVal("gamenumber", ""+this.gamenumber);
             AttribVal av1 = new AttribVal("correct", ""+this.getScoreCORRECT(pA));
             AttribVal av2 = new AttribVal("incorrect", ""+this.getScoreINCORRECT(pA));
             AttribVal av3 = new AttribVal("stimulusself",stimulusself  );
             AttribVal av4 = new AttribVal("stimulusother",stimulusself  );
             AttribVal av5 = new AttribVal("points", ""+(double)  this.htPOINTS.getObject(pA));
             AttribVal av6 =null; 
             if(hasLoopedThroughAllFaces) {
                 av6 = new AttribVal("haslooped","YES")  ;
             } 
             else {
                 av6 = new AttribVal("haslooped","NO");
             }
             avs.addElement(av0);avs.addElement(av1); avs.addElement(av2); avs.addElement(av3); avs.addElement(av4); avs.addElement(av5);avs.addElement(av6);
         }
         if(pB==p){
             stimulusself=directoryname+"/"+this.currentTrial[0];
             stimulusother=directoryname+"/"+this.currentTrial[1];
             AttribVal av0 = new AttribVal("gamenumber", ""+this.gamenumber);
             AttribVal av1 = new AttribVal("correct", ""+this.getScoreCORRECT(pB));
             AttribVal av2 = new AttribVal("incorrect", ""+this.getScoreINCORRECT(pB));
             AttribVal av3 = new AttribVal("stimulusself",""+stimulusself  );
             AttribVal av4 = new AttribVal("stimulusother",""+stimulusself  ); 
             AttribVal av5 = new AttribVal("points", ""+(double)  this.htPOINTS.getObject(pB));
             AttribVal av6 =null;
             if(hasLoopedThroughAllFaces) {
                 av6 = new AttribVal("haslooped","YES")  ; 
             }else {
                 av6 = new AttribVal("haslooped","NO");
             }
             avs.addElement(av0);avs.addElement(av1); avs.addElement(av2); avs.addElement(av3); avs.addElement(av4);avs.addElement(av5);avs.addElement(av6);
         }
    
         return avs;
    } 
     
     
    @Override
    public void processNotification(String nameOfEvent) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(nameOfEvent.equalsIgnoreCase("timeout")){
            //this.jjp.addTextln("CORRECT");
        }
    }

    @Override
    public void changeClientProgressBars(int value, String text) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     
    
    
    
    
    
    
     
     
     
     
     
     
}
