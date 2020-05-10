/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.attribval.AttribVal;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.ConversationController.ui.JInterfaceTenButtons;
import diet.server.ConversationController.ui.JInterfaceMenuButtonsReceiverInterface;
import diet.server.Participant;
import diet.task.ProceduralComms.PCTask;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class Dyadic_TurnByTurnInterface_ProceduralComms_FilteringPositiveAndNegativeFeedback extends DefaultWYSIWYGConversationControllerInterface implements JInterfaceMenuButtonsReceiverInterface{

   //PCTask pct;
   long durationFadeout= 90000000;// = CustomDialog.getLong("What is the fadeout?", 90000000);
   JInterfaceTenButtons jisp; 
   
   boolean useNormalInterface = true;//CustomDialog.getBoolean("use normal interface?", "normal", "wysiwyg");
    Vector participantsQueuedLLLL = new Vector();
    Vector participantsQueuedRRRRR = new Vector();
    Vector taskControllers = new Vector();
    
    
    
   
    public Dyadic_TurnByTurnInterface_ProceduralComms_FilteringPositiveAndNegativeFeedback(Conversation c) {
        super(c, 2, 2500);
         DefaultConversationController.sett.login_numberOfParticipants=6;
        this.wysiwygtm.setAllOnSameTrack(false);
        DefaultConversationController.sett.client_MainWindow_height= 800;
        //DefaultConversationController.sett.client_font_OTHER1_size=8;
        //DefaultConversationController.sett.client_font_OTHER2_size=8;
        //DefaultConversationController.sett.client_font_SELF_size=8;
        // DefaultConversationController.sett.client_font_SERVER_fontsize=8;
         
        if(this.useNormalInterface){
           
        }else{
            durationFadeout = CustomDialog.getLong("What is the fadeout?", 90000000);
        }
        /* jisp = new JInterfaceTenButtons(this,
                "START",
                 "SEQUENCEO",
                "Step1_SortByAbilityAndDoFirstSwap_connectM1withM3_____M2withM4",
                "Step2_connectM1withM2_____M3withM4",
                "Step3_connectM1withM3_____M2withM4",
                "Step4_connectM1withM2_____M3withM4",
                "Step5_connectM1withM3_____M2withM4",
                "Step6_connectM1withM2_____M3withM4",
                "Step7_connectM1withM3_____M2withM4",
                "Step8_INTERVENTION: BETWEEN WITHIN_connectM1withM4_____M2withDIFFERENTDYAD"
               
         );*/
        
          jisp = new JInterfaceTenButtons(this,
                "START",
                "ALLOWLANGUAGE",
                "STOPLANGUAGE",
                "CREATEHEXAGON_WITHIN2",
                "HEXWITHIN1",
                "HEXWITHIN2",
                "HEXWITHIN1",
                "HEXWITHIN2",
                "HEXWITHIN1",
                "HEXWITHIN2"
               
         );
        
    }
 
      
    
    
    
   
    
      @Override
    public boolean requestParticipantJoinConversation(String participantID) {    
        
        //This section is only for autologin (i.e. when programming / testing the setup)
         if(DefaultConversationController.sett.login_autologin){
            if(c.getParticipants().findParticipantWithEmail(participantID)!=null) return false; 
             
            if(this.participantsQueuedLLLL.size()>this.participantsQueuedRRRRR.size()){
                  if(participantID.equals("RRRR1"))return true;
                  if(participantID.equals("RRRR2"))return true;
                  if(participantID.equals("RRRR3"))return true;
                  if(participantID.equals("RRRR4"))return true;
                  if(participantID.equals("RRRR5"))return true;
                  if(participantID.equals("RRRR6"))return true;
                  if(participantID.equals("RRRR7"))return true;
                  if(participantID.equals("RRRR8"))return true;
                  if(participantID.equals("RRRR9"))return true;
                  if(participantID.equals("RRRR0"))return true;
                  if(participantID.equals("RRRRX"))return true;
                  if(participantID.equals("RRRRY"))return true;
                  if(participantID.equals("RRRRZ"))return true;
            }
            else if (this.participantsQueuedLLLL.size()<this.participantsQueuedRRRRR.size()){
                  if(participantID.equals("LLLL1"))return true;
                  if(participantID.equals("LLLL2"))return true;
                  if(participantID.equals("LLLL3"))return true;
                  if(participantID.equals("LLLL4"))return true; 
                  if(participantID.equals("LLLL5"))return true;
                  if(participantID.equals("LLLL6"))return true;
                  if(participantID.equals("LLLL7"))return true;
                  if(participantID.equals("LLLL8"))return true;
                  if(participantID.equals("LLLL9"))return true;
                  if(participantID.equals("LLLL0"))return true;
                  if(participantID.equals("LLLLX"))return true;
                  if(participantID.equals("LLLLY"))return true;
                  if(participantID.equals("LLLLZ"))return true;
            }
            else if(this.participantsQueuedLLLL.size()==this.participantsQueuedRRRRR.size()) {
                if(participantID.startsWith("LLLL") || participantID.startsWith("RRRR")) return true;
               
            }
            return false;
         }
        
        //
        
        if(participantID.equals("LLLL1"))return true;
        if(participantID.equals("LLLL2"))return true;
        if(participantID.equals("LLLL3"))return true;
        if(participantID.equals("LLLL4"))return true; 
        if(participantID.equals("LLLL5"))return true;
        if(participantID.equals("LLLL6"))return true;
        if(participantID.equals("LLLL7"))return true;
        if(participantID.equals("LLLL8"))return true;
        if(participantID.equals("LLLL9"))return true;
        if(participantID.equals("LLLL0"))return true;
        if(participantID.equals("LLLLX"))return true;
        if(participantID.equals("LLLLY"))return true;
        if(participantID.startsWith("LLLLZ"))return true;
        
        if(participantID.equals("RRRR1"))return true;
        if(participantID.equals("RRRR2"))return true;
        if(participantID.equals("RRRR3"))return true;
        if(participantID.equals("RRRR4"))return true;
        if(participantID.equals("RRRR5"))return true;
        if(participantID.equals("RRRR6"))return true;
        if(participantID.equals("RRRR7"))return true;
        if(participantID.equals("RRRR8"))return true;
        if(participantID.equals("RRRR9"))return true;
        if(participantID.equals("RRRR0"))return true;
        if(participantID.equals("RRRRX"))return true;
        if(participantID.equals("RRRRY"))return true;
        if(participantID.startsWith("RRRRZ"))return true;
        System.err.println("REJECTINGASLOGIN: "+participantID);
        return false;
    }
    
    
    
    //Vector quad1 = new Vector();
    //Vector quad2= new Vector();
    
   // Participant q1A;
   // Participant q1B;
   // Participant q1C;
   // Participant q1D;
    
    
    
    
    @Override
    public synchronized void participantJoinedConversation(Participant p) {
         // super.participantJoinedConversation(p); //To change body of generated methods, choose Tools | Templates.
         
        
          if(this.useNormalInterface){
              
          }
          else{
               durationFadeout = CustomDialog.getLong("What is the fadeout?", 90000000);
               this.changeClientInterfaceToRightJustified(p,800,100,durationFadeout, 2,2);
          }
          
        
        try{
            Thread.sleep(1000);
        }catch(Exception e){e.printStackTrace();}
        
        this.changeClientInterfaceCharacterWhitelist(p, "");
        c.changeClientInterface_setMaxTextEntryCharLength(p, 1);
        
        c.displayNEWWebpage(p, "instructions", "instructions", "", 500, 600, false, true);
        
         if(p.getParticipantID().startsWith("LLLL")){
            this.participantsQueuedLLLL.addElement(p);
        }
        else if (p.getParticipantID().startsWith("RRRR")){
            this.participantsQueuedRRRRR.addElement(p);
        }
        Conversation.printWSln("Main", "No. of LLLL:"+this.participantsQueuedLLLL.size()+"   No. of RRRR:"+ this.participantsQueuedRRRRR.size());
        if(this.participantsQueuedLLLL.size()!=this.participantsQueuedRRRRR.size()){
            Conversation.printWSln("Main","UNEQUAL GROUP SIZES - DO NOT START THE EXPERIMENT----ALLP:"+ this.c.getParticipants().getAllParticipants().size());
        }
        else{
            Conversation.printWSln("Main","EQUAL SIZES: OK TO START");
        }
        
        
        
        
     }
    
 
    
    
    
    
    
  
    
    
     public void doPairingOfEnqueudDyadsAndStartDyads(){
        this.taskControllers=new Vector(); 
         
        if(this.participantsQueuedLLLL.size()!=this.participantsQueuedRRRRR.size()){
            Conversation.printWSln("Main", "CANNOT START - THERE IS AN UNEQUAL NUMBER OF GROUPS");
            return;
        }
        for(int i=0;i<this.participantsQueuedLLLL.size();i++){
             Participant pL = (Participant)participantsQueuedLLLL.elementAt(i);
             Participant pR = (Participant)participantsQueuedRRRRR.elementAt(i);
             
             pp.createNewSubdialogue(pL,pR);
             PCTask pct=new PCTask(this,pL,pR);
             this.taskControllers.addElement(pct);
             this.changeClientInterfaceCharacterWhitelist(pct.pA, pct.pAWhitelist + pct.sharedWhitelist+pct.allowedMetaChars);
             this.changeClientInterfaceCharacterWhitelist(pct.pB, pct.pBWhitelist+  pct.sharedWhitelist+pct.allowedMetaChars);      
        }
        this.participantsQueuedLLLL.removeAllElements();
        this.participantsQueuedRRRRR.removeAllElements();
    }    



   public void performActionTriggeredByUI(String s) {
        if(s.equalsIgnoreCase("START")){
            doPairingOfEnqueudDyadsAndStartDyads();
        }  
        if(s.equalsIgnoreCase("Step1_SortByAbilityAndDoFirstSwap_connectM1withM3_____M2withM4")){    
           spoofOtherNumber = r.nextInt(1000);
           Vector<Participant[]> bestpairs=this.findBestPairs();
           this.assignToQuads(bestpairs);
           connectM1withM3_____M2withM4();
        }
        else if(s.equalsIgnoreCase("Step2_connectM1withM2_____M3withM4")){
            spoofOtherNumber = r.nextInt(1000);
            connectM1withM2_____M3withM4();
        }
        else if(s.equalsIgnoreCase("Step3_connectM1withM3_____M2withM4")){
            spoofOtherNumber = r.nextInt(1000);
            connectM1withM3_____M2withM4();
        }
        else if(s.equalsIgnoreCase("Step4_connectM1withM2_____M3withM4")){
           spoofOtherNumber = r.nextInt(1000);
           connectM1withM2_____M3withM4();
        }
        else if(s.equalsIgnoreCase("Step5_connectM1withM3_____M2withM4")){
            spoofOtherNumber = r.nextInt(1000);
            connectM1withM3_____M2withM4();
        }
        else if(s.equalsIgnoreCase("Step6_connectM1withM2_____M3withM4")){
            spoofOtherNumber = r.nextInt(1000);
            connectM1withM2_____M3withM4();
        }
        else if(s.equalsIgnoreCase("Step8_INTERVENTION: BETWEEN WITHIN_connectM1withM4_____M2withDIFFERENTDYAD")){
           spoofOtherNumber = r.nextInt(1000);
           connectM1withM4_____M2withOthers();
        }
        
        
         else if(s.equalsIgnoreCase("ALLOWLANGUAGE")){
           this.allowLanguage();
        }
        else if(s.equalsIgnoreCase("STOPLANGUAGE")){
           this.stopLanguage();
        }
        else if(s.equalsIgnoreCase("CREATEHEXAGON_WITHIN2")){
           Vector<Participant[]> bestpairs=this.findBestPairs();
           this.assignToHexagons(bestpairs);
            connectHexagonWithinPosition2();
        }
        else if(s.equalsIgnoreCase("HEXWITHIN1")){
            spoofOtherNumber = r.nextInt(1000);
            connectHexagonWithinPosition1();
        }
        else if(s.equalsIgnoreCase("HEXWITHIN2")){
            spoofOtherNumber = r.nextInt(1000);
            connectHexagonWithinPosition2();
        }
       
   }
   
   
   
   Vector hexagons = new Vector();
   Vector<Participant[]> hexagonLonelyPairs = new Vector();
   
    public void assignToHexagons( Vector<Participant[]>  dyads){
       Vector<Participant[]> copyOfPairs = (Vector<Participant[]>)dyads.clone();
        hexagonLonelyPairs = new Vector<Participant[]>();
       
       while(copyOfPairs.size() % 3 !=0   && copyOfPairs.size()>0 ){
           Participant[]  o = copyOfPairs.lastElement();
           hexagonLonelyPairs.add(o);
           copyOfPairs.remove(o);
       }
      
       for(int i=0;i<copyOfPairs.size();i=i+3){
           Hexagon hx = new Hexagon( 
                   copyOfPairs.elementAt(i)[0], 
                   copyOfPairs.elementAt(i+1)[0],
                   copyOfPairs.elementAt(i+2)[0], 
                   copyOfPairs.elementAt(i)[1], 
                   copyOfPairs.elementAt(i+1)[1],
                   copyOfPairs.elementAt(i+2)[1]);
          hexagons.addElement(hx);
       }   
    }
   
   
    public void connectHexagonWithinPosition1(){
        for(int i=0;i<taskControllers.size();i++){
              PCTask pct = (PCTask)this.taskControllers.elementAt(i);
              pct.kill();
        }
        for(int i=0;i<this.hexagonLonelyPairs.size();i++){
           this.createNewPair( hexagonLonelyPairs.elementAt(i)[0],hexagonLonelyPairs.elementAt(i)[1]);
        }
        
        for(int i=0;i<this.hexagons.size();i++){
            Hexagon hh = (Hexagon)hexagons.elementAt(i);
            this.createNewPair(hh.hmemberASD1, hh.hmemberJKL1);
            this.createNewPair(hh.hmemberASD2, hh.hmemberJKL2);
            this.createNewPair(hh.hmemberASD3, hh.hmemberJKL3);
        }
    }
    
     public void connectHexagonWithinPosition2(){
        for(int i=0;i<taskControllers.size();i++){
              PCTask pct = (PCTask)this.taskControllers.elementAt(i);
              pct.kill();
        }
        for(int i=0;i<this.hexagonLonelyPairs.size();i++){
           this.createNewPair( hexagonLonelyPairs.elementAt(i)[0],hexagonLonelyPairs.elementAt(i)[1]);
        }
        
        for(int i=0;i<this.hexagons.size();i++){
            Hexagon hh = (Hexagon)hexagons.elementAt(i);
            this.createNewPair(hh.hmemberASD1, hh.hmemberJKL2);
            this.createNewPair(hh.hmemberASD2, hh.hmemberJKL3);
            this.createNewPair(hh.hmemberASD3, hh.hmemberJKL1);
        }
    }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   public void allowLanguage(){
        for(int i=0;i<this.c.getParticipants().getAllParticipants().size();i++){
            Participant p = c.getParticipants().getAllParticipants().elementAt(i);
            this.changeClientInterfaceCharacterWhitelist(p, " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYX?1234567890");
            c.changeClientInterface_setMaxTextEntryCharLength(p, 10000);   
            c.sendInstructionToParticipant(p, "You can now type normally for the next few minutes. After that, text will be blocked again!Try and explain to your partner");
        }
       
       
         
   }
   
   public void stopLanguage(){
       for(int i=0;i<this.c.getParticipants().getAllParticipants().size();i++){
            Participant p = c.getParticipants().getAllParticipants().elementAt(i);
            
            
            
            this.changeClientInterfaceCharacterWhitelist(p, "");
            c.changeClientInterface_setMaxTextEntryCharLength(p, 1);   
        }
       for(int i=0;i<this.taskControllers.size();i++){
           PCTask pct = (PCTask)this.taskControllers.elementAt(i);
           c.sendInstructionToParticipant(pct.pA, "Blocked again!");
           c.sendInstructionToParticipant(pct.pB, "Blocked again!");
           this.changeClientInterfaceCharacterWhitelist(pct.pA, pct.pAWhitelist + pct.sharedWhitelist+pct.allowedMetaChars);    
           this.changeClientInterfaceCharacterWhitelist(pct.pB, pct.pBWhitelist+  pct.sharedWhitelist+pct.allowedMetaChars); 
       }
       
       
       
                 
       
       
       
   }
   
   
   
   
   
   
   
   
   
   public void connectM1withM3_____M2withM4(){
        for(int i=0;i<taskControllers.size();i++){
              PCTask pct = (PCTask)this.taskControllers.elementAt(i);
              pct.kill();
        }
       
        this.taskControllers=new Vector();             
        for(int i=0;i<quads.size();i++){
            Quad quad = quads.elementAt(i);
            pp.createNewSubdialogue(quad.member1,quad.member3);
            PCTask pct1=new PCTask(this,quad.member1,quad.member3,true);
            this.taskControllers.addElement(pct1);
            
            pp.createNewSubdialogue(quad.member2,quad.member4);
            PCTask pct2=new PCTask(this,quad.member2,quad.member4,true);
            this.taskControllers.addElement(pct2);      
        }
        if(this.quadLonelyPair!=null){
            pp.createNewSubdialogue(quadLonelyPair[0],quadLonelyPair[1]);
            PCTask pctL=new PCTask(this,quadLonelyPair[0],quadLonelyPair[1],true);
             this.taskControllers.addElement(pctL);      
        }
   }
   
   public void connectM1withM2_____M3withM4(){
        for(int i=0;i<taskControllers.size();i++){
              PCTask pct = (PCTask)this.taskControllers.elementAt(i);
              pct.kill();
        }
       
        this.taskControllers=new Vector();             
        for(int i=0;i<quads.size();i++){
            Quad quad = quads.elementAt(i);
            pp.createNewSubdialogue(quad.member1,quad.member2);
            PCTask pct1=new PCTask(this,quad.member1,quad.member2,true);
            this.taskControllers.addElement(pct1);
            
            pp.createNewSubdialogue(quad.member3,quad.member4);
            PCTask pct2=new PCTask(this,quad.member3,quad.member4,true);
            this.taskControllers.addElement(pct2);      
        }
        if(this.quadLonelyPair!=null){
            pp.createNewSubdialogue(quadLonelyPair[0],quadLonelyPair[1]);
            PCTask pctL=new PCTask(this,quadLonelyPair[0],quadLonelyPair[1],true);
            this.taskControllers.addElement(pctL);      
        }
   }
   
   
   public void connectM1withM4_____M2withOthers(){
         for(int i=0;i<taskControllers.size();i++){
              PCTask pct = (PCTask)this.taskControllers.elementAt(i);
              pct.kill();
        }
        if(quadLonelyPair!=null){
            this.createNewPair(quadLonelyPair[0], quadLonelyPair[1]);
        } 
                 
        if(quads.size()==1){
             Quad quad = quads.elementAt(0);
             this.createNewPair(quad.member1, quad.member4);
             this.createNewPair(quad.member2, quad.member3);
        }
        else if(quads.size()==2){
             this.connectQuads(quads.elementAt(0), quads.elementAt(1));
        }
        else if(quads.size()==3){
              this.connectQuads(quads.elementAt(0), quads.elementAt(1),quads.elementAt(2));
        }
        else if(quads.size()==4){
              this.connectQuads(quads.elementAt(0), quads.elementAt(1));
              this.connectQuads(quads.elementAt(2), quads.elementAt(3));
        }
        else if(quads.size()==5){
              this.connectQuads(quads.elementAt(0), quads.elementAt(1),quads.elementAt(2));
              this.connectQuads(quads.elementAt(3), quads.elementAt(4));
        }
        else if(quads.size()==6){
              this.connectQuads(quads.elementAt(0), quads.elementAt(1));
              this.connectQuads(quads.elementAt(2), quads.elementAt(3));
              this.connectQuads(quads.elementAt(4), quads.elementAt(5));
        }
        
       
       
   }
   
   public void connectQuads(Quad quadA,Quad quadB){
             this.createNewPair(quadA.member1, quadA.member4);
             this.createNewPair(quadB.member1, quadB.member4);
             
             this.createNewPair(quadA.member2, quadB.member2);
             this.createNewPair(quadA.member3, quadB.member3);        
   }
   
   public void connectQuads(Quad quadA,Quad quadB, Quad quadC){
             this.createNewPair(quadA.member1, quadA.member4);
             this.createNewPair(quadB.member1, quadB.member4);
             this.createNewPair(quadC.member1, quadC.member4);
             
             this.createNewPair(quadA.member2, quadB.member2);
             this.createNewPair(quadA.member3, quadC.member3);    
             this.createNewPair(quadB.member3, quadC.member2);          
   }
   
   
   
   
   public void createNewPair(Participant p1, Participant p2){
        pp.createNewSubdialogue(p1,p2);
        PCTask pctN=new PCTask(this,p1,p2,true);
        this.taskControllers.addElement(pctN);     
   }
   

   
   
   
   
    public synchronized Vector<Participant[]> findBestPairs(){
        Vector dyadsUNSORTED = new Vector();
        for(int i=0;i<this.taskControllers.size();i++){
            PCTask pct = (PCTask)this.taskControllers.elementAt(i);
            Participant[] dyad = new Participant[]{pct.pA,pct.pB};
            dyadsUNSORTED.addElement(dyad);
            pct.kill();
            System.err.println("KILLING");
        }
        
        
        
        this.taskControllers.removeAllElements();
        Vector dyadsSORTED = new Vector();
        while(dyadsUNSORTED.size()>0){
            Participant[] highestscoring =(Participant[])dyadsUNSORTED.elementAt(0);
            Object oHighestA = PCTask.htCurrentLevel.getObject(highestscoring[0]);
            int highestALevel = 0;
            if(oHighestA!=null)highestALevel=(int)oHighestA;
            Object oHihestB = PCTask.htCurrentLevel.getObject(highestscoring[1]);
            int highestBLevel = 0;
            if(oHihestB!=null)highestBLevel=(int)oHihestB;
            long highestTotalLevel = highestALevel+highestBLevel;
            
            
            for(int i = 1;i<dyadsUNSORTED.size();i++){
                Participant[] candidate =(Participant[])dyadsUNSORTED.elementAt(i);
                Object oA = PCTask.htCurrentLevel.getObject(candidate[0]);
                int candidateALevel = 0;
                if(oA!=null)candidateALevel=(int)oA;
                Object oB = PCTask.htCurrentLevel.getObject(candidate[1]);
                int candidateBLevel = 0;
                if(oB!=null)candidateBLevel=(int)oB;
                int candidateTotalLevel = candidateALevel+candidateBLevel;
                if(candidateTotalLevel> highestTotalLevel){
                    highestscoring=candidate;
                    highestTotalLevel=candidateTotalLevel;
                   
                }
                
                
            }
           // dyadsSORTED.insertElementAt(highestscoring, 0);
            dyadsSORTED.addElement(highestscoring);
            dyadsUNSORTED.remove(highestscoring);      
        }
        System.err.println("THESE ARE ALL THE DYADS - IN ORDER OF THEIR SCORES...");
        for(int i=0;i<dyadsSORTED.size();i++){
            Participant [] dyad= (Participant[])dyadsSORTED.elementAt(i);
            Participant[] candidate = dyad;
            Object oA = PCTask.htCurrentLevel.getObject(candidate[0]);
            int candidateAScore = 0;
            if(oA!=null)candidateAScore=(int)oA;
            Object oB = PCTask.htCurrentLevel.getObject(candidate[1]);
            int candidateBScore = 0;
            if(oB!=null)candidateBScore=(int)oB;
            int candidateTotalScore = candidateAScore+candidateBScore;    
            System.err.println(i+")+ "+dyad[0].getParticipantID()+","+dyad[0].getUsername()+"----" +dyad[1].getParticipantID()+","+dyad[1].getUsername()+" LEVEL: "+candidateTotalScore);
            
        }
        return dyadsSORTED;
        
    }
    
    //Quad quad1;
    //Quad quad2;
    //Quad quad3;
    //Quad quad4;
    //Quad quad5;
    //Quad quad6;
    
    Participant[] quadLonelyPair = null;
    Vector<Quad> quads = new Vector<Quad>();
    
    
    public void assignToQuads( Vector<Participant[]>  dyadsSORTED){
        
       Vector<Participant[]> copyOfPairs = (Vector<Participant[]>)dyadsSORTED.clone();
       if(copyOfPairs.size() % 2 !=0){
          System.err.println("NEED TO CREATE LONELY PAIR");
          quadLonelyPair = copyOfPairs.lastElement();
          copyOfPairs.remove(quadLonelyPair);
       }
       
       
       for(int i=0;i<copyOfPairs.size();i=i+2){
           Quad qd = new Quad(copyOfPairs.elementAt(i)[0], copyOfPairs.elementAt(i)[1], copyOfPairs.elementAt(i+1)[0], copyOfPairs.elementAt(i+1)[1]);
           quads.addElement(qd);
       }
       
       
       
    }
    
     
     
    

    @Override
    public void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
        if(this.useNormalInterface) return;
        PCTask pct = this.getTask(sender);
        if(pct!=null){
             if(pct!=null  && !   pct.allowedMetaChars.contains( mWYSIWYGkp.getTextTyped()))  pct.evaluate(sender, mWYSIWYGkp.getTextToAppendToWindow());
             super.processWYSIWYGTextInserted(sender, mWYSIWYGkp); //To change body of generated methods, choose Tools | Templates.     
        }
        
        
        
    }

    int spoofOtherNumber = 2;
    
    
    
    
    @Override
    public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
        super.processChatText(sender, mct); //To change body of generated methods, choose Tools | Templates.    
        
        String spoofusername = "PersonID"+this.spoofOtherNumber;
        c.relayTurnToPermittedParticipantsWithSpoofUsername(sender, mct, spoofusername);
        
        
        
        
        
        PCTask pct = this.getTask(sender);
        if(pct!=null  && !   pct.allowedMetaChars.contains( mct.getText())) {
             pct.evaluate(sender, mct.getText());
             System.err.println("--TEXT:"+mct.getText().replace("\n", ""));
             //System.exit(-234);
         }
        else{
            CustomDialog.showModelessDialog("CANT FIND THE TASK");
        }
        
    
        
        
    }

    @Override
    public Vector<AttribVal> getAdditionalInformationForParticipant(Participant p) {
        Vector superGetAdditionalInformation = super.getAdditionalInformationForParticipant(p); //To change body of generated methods, choose Tools | Templates.
        PCTask pct = this.getTask(p);
        if(pct!=null){
             Vector gameAdditionlInformation =  pct.getAdditionalValues(p);  
             superGetAdditionalInformation.addAll(gameAdditionlInformation);
        }
        
       
       
        return superGetAdditionalInformation;
    }

    
    
    
    public PCTask getTask(Participant p){
        for(int i=0;i<this.taskControllers.size();i++){
            PCTask pct = (PCTask)taskControllers.elementAt(i);
            if(pct.pA==p)return pct;
            if(pct.pB==p)return pct;
        }
        return null;
    }
    
    public class Quad{
       
        public Quad(Participant m1,Participant m2,Participant m3, Participant m4){
            this.member1=m1;
            this.member2=m2;
            this.member3=m3;
            this.member4=m4;
            
        }
        
        public Participant member1;
        public Participant member2;
        public Participant member3;
        public Participant member4;
        
       
        
    }
    
    
    public class Hexagon{
       
        public Hexagon(Participant asd1,Participant asd2,Participant asd3, Participant jkl1, Participant jkl2, Participant jkl3){
            this.hmemberASD1=asd1;
            this.hmemberASD2=asd2;
            this.hmemberASD3=asd3;
            this.hmemberJKL1=jkl1;
            this.hmemberJKL2=jkl2;
            this.hmemberJKL3=jkl3;
            
        }
        
        public Participant hmemberASD1;
        public Participant hmemberASD2;
        public Participant hmemberASD3;
        public Participant hmemberJKL1;
        public Participant hmemberJKL2;
        public Participant hmemberJKL3;
       
        
    }
    
    
}
