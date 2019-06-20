package diet.server.ConversationController;
///THIS IS A MAIN CLASS
import diet.attribval.AttribVal;
import diet.client.JChatFrameMultipleWindowWithSendButtonWidthByHeight.StyledDocumentStyleSettings;
import java.util.Date;
import java.util.Random;


import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.stylemanager.StyleManager;
import diet.message.*;
import diet.server.ParticipantPartnering.ParticipantPartnering;
import diet.server.IsTypingController.IsTypingOrNotTyping;
import diet.server.Configuration;
import diet.server.io.IntelligentIO;
import diet.task.TaskControllerInterface;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomParticipantIDGeneratorGROOP;
import java.awt.Color;
import java.io.File;

import java.util.Vector;
import javax.swing.text.MutableAttributeSet;

/**
 * This is the main (preferably only) class that should be changed when creating
 * a new experimental design. Every message sent from the clients passes through
 * the methods provided by this class. This includes each keypress, each turn
 * typed and sent. <p>On receiving a message, this class determines what is to
 * be done with the message. In normal operation it relays the messages to the
 * other participants. However, to create interventions this behaviour can be
 * replaced with commands to modify the turn or create artificial turns.
 *
 * <p> Most of the methods of this class are called by {@link diet.server.Conversation}.
 * It is expected that the methods of this class will do necessary detection of
 * targets, transforming of turns, and on this basis call methods in {@link diet.server.Conversation}
 * to send the artificially created messages to the participants.
 *
 *
 * @author user
 */
public abstract class DefaultConversationController  {
 
    public Conversation c;
    
    public static Configuration sett = new Configuration();
    //The confguration file for the chat-tool
  
    public static CyclicRandomParticipantIDGeneratorGROOP autologinParticipantIDGenerator = new CyclicRandomParticipantIDGeneratorGROOP(); 
    //Used to generate ParticipantIDs when using the autologin function when developing
    
    public IsTypingOrNotTyping itnt; //= new IsTypingOrNotTyping(this, param_isTypingTimeOut);
    //This object is used to generate the "is tpying" status indicator messages - note that these can be manipulated    
    
    public ParticipantPartnering pp;
    //This is used to store who speaks with whom
    
    public StyleManager sm = new StyleManager(this);
    //This is used to make sure that each person sees other participants' text with consistent colours / fonts
    
    public TaskControllerInterface tc;
    //Many experiments also involve some dialogue task
    
     public Random r = new Random(new Date().getTime());
    
     
     
     
     
    public boolean experimentHasStarted = false; 
    
    private Vector participantJoinedConversationButNotAssignedToGroup = new Vector();
    
    
    
    public DefaultConversationController(Conversation c){
        this.c=c;
        String parentDirectory = System.getProperty("user.dir");
        if(parentDirectory.endsWith(File.separator)){
            parentDirectory = parentDirectory + "data"+File.separator+"saved experimental data";
        }
        else{
            parentDirectory = parentDirectory + File.separator+ "data"+File.separator+"saved experimental data";
        }
        
        
        c.convIO = new IntelligentIO(c,parentDirectory,this.getID());
        pp = new ParticipantPartnering(c);
        itnt = new IsTypingOrNotTyping(this, sett.client_TextEntryWindow_istypingtimeout);
      // itnt = new IsTypingOrNotTyping(this, 2500);
       
    }
    
    public DefaultConversationController(Conversation c, long istypingtimeout){
        this.c=c;
         String parentDirectory = System.getProperty("user.dir");
        if(parentDirectory.endsWith(File.separator)){
            parentDirectory = parentDirectory + "data"+File.separator+"saved experimental data";
        }
        else{
            parentDirectory = parentDirectory + File.separator+ "data"+File.separator+"saved experimental data";
        }
        c.convIO = new IntelligentIO(c,parentDirectory,this.getID());
        pp = new ParticipantPartnering(c);
        itnt = new IsTypingOrNotTyping(this, istypingtimeout);
        
    }
    
    /**
    * This is called after the object has been created and connected with the GUI and filesystem. 
    */
    public void  initializePostSetup(){
        
    }
    
    public static boolean showcCONGUI() {
        return false;
    }

    public Conversation getC() {
        return c;
    }
   
    
    
   
   
    public StyleManager getStyleManager() {
        return sm;
    }

    
    
     /**
    * Called by the client when the client attempts to connect to the server
    */   
    public boolean requestParticipantJoinConversation(String participantID) {
        return true;
    }
    
    
            
    /**
    * Sends the setup information to the client interface
    */
   public MessageClientSetupParameters processRequestForInitialChatToolSettings(){      
               boolean alignmentIsVertical = true;
               boolean deletesPermitted =true;
               
               
               
               Color background =    new Color(sett.client_backgroundcolour_rgb[0],sett.client_backgroundcolour_rgb[1], sett.client_backgroundcolour_rgb[2]);
               Vector othersColors = new Vector();
               Color selfColor = Color.black;
               StyledDocumentStyleSettings styleddocsettings;
               int ownWindowNumber =0;
               try{
                   MutableAttributeSet masSELF =  this.getStyleManager().getStyleForSelf();  
                   styleddocsettings = new StyledDocumentStyleSettings(background, selfColor, masSELF  );
                   return new MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight("server","servername2",
                                                this.sett.client_MainWindow_width, this.sett.client_MainWindow_height,
                                                alignmentIsVertical,
                                                this.sett.client_numberOfWindows,
                                                ownWindowNumber,    //This needs to be loaded from the Permissions File //Get x,y
                                                false,
                                                true,
                                                "Setting up",
                                                true,
                                                this.sett.client_TextEntryWindow_width, this.sett.client_TextEntryWindow_height,
                                                this.sett.client_TextEntryWindow_maximumtextlength,
                                                styleddocsettings);
               }catch (Exception e){
                    Conversation.printWSln("Main", "Could not find parameters for chat tool client interface...attempting to use defaults" );
                    
                e.printStackTrace();

                }
       return null;          
    }
    
  
    
    public void participantJoinedConversation(Participant p){
        participantJoinedConversationButNotAssignedToGroup.add(p);
        if(participantJoinedConversationButNotAssignedToGroup.size()== Configuration.defaultGroupSize){
             pp.createNewSubdialogue(participantJoinedConversationButNotAssignedToGroup);
             participantJoinedConversationButNotAssignedToGroup.removeAllElements();
       }
      
    }
    
     
    
    
    
    public void participantRejoinedConversation(Participant p) {
    }
   
   
    /**
     *
     * This method is invoked by {@link diet.server.Conversation} whenever a
     * participant presses a key while typing text in their chat window. The
     * default behaviour is to inform the other participants that the
     * participant is typing.
     *
     * @param sender Participant who has pressed a key
     * @param mkp message containing the keypress information
     */
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
        if (!mkp.isENTER()){
          this.itnt.processDoingsByClient(sender);
        }
    }

    /**
     * This method is invoked by {@link diet.server.Conversation} whenever the
     * text in a participant's text entry window changes by having one or more
     * characters inserted.
     *
     * @param sender participant who inserted text
     * @param mWYSIWYGkp message containing information about the text inserted
     */
    public void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
    }

    /**
     * This method is invoked by {@link diet.server.Conversation} whenever the
     * text in a participant's text entry window changes by having one or more
     * characters deleted. This is separate from Keypresses (a user might delete
     * a whole segment of text by highlighting the text and pressing delete
     * once).
     *
     * @param sender participant who deleted text
     * @param mWYSIWYGkp message containing information about the text deleted
     */
    public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
    }

    
    
    
    
    
    
   
        
    
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever a
     * participant has typed a message The default behaviour is to relay the
     * message to the other participants. This is the main locus for programming
     * interventions in the chat tool.
     *
     * @param sender the participant who typed the turn
     * @param mct the message typed by the participant
     */
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
        
        
        
        if(!this.experimentHasStarted){
            c.newsendInstructionToParticipant(sender, "Please wait until the experiment has started");
        }
        
        itnt.processTurnSentByClient(sender);
        if (sett.debug_allow_client_to_send_debug_commands) {
            cmnd(sender, mct.getText());
        }
    }
    
     
    public void processTaskMove(MessageTask mt, Participant p){
         if(tc!=null){
           tc.processTaskMove(mt, p);
        }
    }
    public void closeDown(){
        if(tc!=null){
           tc.closeDown();
        }
    }
    
   

    public void processPopupResponse(Participant origin, MessagePopupResponseFromClient mpr) {
    }

    public void processClientEvent(Participant origin, MessageClientInterfaceEvent mce){
       
        
    }
    
    
    
   public void startExperiment(){
       this.experimentHasStarted=true;
   }
    
    
    
    

     public Vector<AttribVal> getAdditionalInformationForParticipant(Participant p){
         try{
             AttribVal av = new AttribVal("istypingtimeout",""+itnt.getIsTypingTimeout());
             Vector v = new Vector(); v.addElement(av); 
             return v;
         } catch (Exception e){
             e.printStackTrace();
         }
         
         return new Vector();
    }
    

    

    public void cmnd(String command) {
        if (command.equalsIgnoreCase("////d")) {
            Vector v = c.getParticipants().getAllParticipants();
            Participant p = (Participant) v.elementAt(0);
            p.getConnection().dispose();
        }
    }

    public void cmnd(Participant p, String command) {
        if (command.equalsIgnoreCase("////d")) {
            if (p != null) {
                p.getConnection().dispose();
            }
        }
    }
    
    
    public void setIsTypingTimeout(int n){
        this.itnt.setInactivityThreshold(n);
    }
    
    
    public void processWYSIWYGFloorRequestDEPRECATED(Participant p, MessageWYSIWYGFloorRequest mwysiwygfr){
       
    }
                        
    
    public void processWYSIWYGFloorChangeConfirmDEPRECATED(Participant sender, diet.message.MessageWYSIWYGFloorChangeConfirm mwysiwygfcc){
        
    }
    
    public void processButtonPress(Participant sender, MessageButtonPressFromClient mbfc){
        c.newsaveAdditionalRowOfDataToSpreadsheetOfTurns("buttonpress", sender, mbfc.buttonname);
    }
            
    
    public void debugDoRobot(String scenarioname, long duration){
        Vector allParticipants = new Vector();
        for(int i=0;i<allParticipants.size();i++){
             
        }
        
        
    }
           
    public Configuration getSettings(){
        return DefaultConversationController.sett;
    }
    
    String id = getClass().getSimpleName();
    public void setID(String s){
        
        this.id=s;
    }
    public String getID(){
        if(id==null)return "notset";
        if(id.length()==0) return "notset";
        if(id.equalsIgnoreCase("")) return "notset";
        return id;
    }
    
    
    
}
