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

    public static Configuration sett = new Configuration();
    //public Configuration actualSettings = (Configuration)defaultsettings.clone();
    
    
    //Below are some debug parameters - they are not essential.
    //public static int param_minParticipantIDLength = 4; //This has to be static, because it is looked up by ExperimentManager before any Conversation Controller objects have been instantiated
    //public static boolean debugMESSAGEBLOCKAGE = false; //true;//false;//true;//true;//false;//true;
    //public static boolean allowCHATCLIENTTOSENDDEBUGCOMMANDS = true;
    //public static boolean doAUTOLOGIN = false;
    public static CyclicRandomParticipantIDGeneratorGROOP autologinParticipantIDGenerator = new CyclicRandomParticipantIDGeneratorGROOP(); 
    //public static boolean debugTime= true;
    //public long loggingOverrunTime = 500;  //time in milliseconds
    
    
    
    /*
         Below are experiment parameters. It is (strongly!) advisable not to edit them here, but instead override in a subclass
         These parameters are intended primarily for initializing the experiment, not for changing the chat tool behaviour while the experiment is running
         There is no guarantee that changing these parameters during an experiment will have the desired effect! Please write custom code instead.
    
    */
    //public String param_experimentID = "NEEDS TO BE SET";
    //public int param_numberOfParticipants = 2;
    //public int param_widthOdMainWindow = 600;
    //public int param_heightOfMainWindow = 350;
    //public int param_widthOfTextEntryWindow =300;
    //public int param_heightOfTextEntryWindow = 150;
    //public int param_maximumCharLengthOfTextEntry = 500;
    //public int param_isTypingTimeOut = 1000;
    
    
    //public int param_numberOfWindows = 1;
    
    
    //These settings below are the default - however they might be overriden by custom implementations of the StlyeManager
    
    //public Color param_backgroundcolor = Color.white;
    
    
    //How text from other participants appears by default:
    //public String param_fontfamily_OTHER = "Monospace";
    //public int param_fontsize_OTHER = 22;
    //public boolean param_fontisbold_OTHER = false;
    //public boolean param_fontisitalic_OTHER = false;
    //public Color param_fontforegroundcolor_OTHER = Color.BLUE;
    
    //How own text appears:
    //public String param_fontfamily_SELF = "Monospace";
    //public int param_fontsize_SELF = 22;
    //public boolean param_fontisbold_SELF = false;
    //public boolean param_fontisitalic_SELF = false;
    //public Color param_fontforegroundcolor_SELF = Color.BLACK;
    
    
    //How text from the server (i.e. instructions) appears:
    //public String param_fontfamily_SERVER = "Monospace";
    //public int param_fontsize_SERVER = 22;
    //public boolean param_fontisbold_SERVER = false;
    //public boolean param_fontisitalic_SERVER = false;
    //public Color param_fontforegroundcolor_SERVER = Color.RED;
    
    
    
    //Settings for interventions:
    //public static String defaultSpoofErrorMessage = "Network error - please wait";
    
    
    //Settings for output to CSV spreadsheet
    //static public String spreadsheetOutputSeparator = "|I|";
    //static public String spreadsheetOutputNEWLINESTRING = "((NEWLINE))";
    
    
    
    //static public String fakeTypingByClientSTARTTrigger = "/startdebug"; //This is used by the client to generate fake "is typing events"
    //static public String fakeTypingByClientSTOPTrigger = "/stopdebug"; //This is used by the client to generate fake "is typing events"
    
    
    ///Be very! vareful modifying this. If it is modified without checking all its uses - can lead to crash!
   // static public String[] headerForSpreadsheetOutputFile = {"ExperimentID", "ServerTimestampOfSavingToFile", "SubdialogueID", "Turntype", "SernderID", "SenderUsername", "ApparentSender", "Text", 
     //    "Recipient(s)", "NoOfDocumentDeletes", "NoOfKeypressDeletes", "ClientTimestampONSET", "ClientTimestampENTER", "ServerTimestampOfReceiptAndOrSending"};
         
    
    //Below are settings for the ExperimentManager GUI
    //static public String experimentmanager_gui_instructionToClient = "Please wait for further instructions. Thankyou!";
    //static public String experimentmanager_gui_websiteURL = "http://www.qualtrics.com";
    
    //static public String tcontact = "+"+   "31" + "XXX" + "YYY" + "ZZZ";
    //static public String econtact = "g (dot) j (dot) mills (at) rug (dot) nl";
    //static public String econtactname = "Gregory Mills";
    
    
    //Below is versioning information
    //static final public double softwareversion = 5.00;
    //static final public String aboutstring = 
    //"This is the 4th incarnation of the chat tool. It grew out of the ROSSINI project (P.G.T. Healey, Queen Mary University)\n"
    //+ "It was used by Matt Purver for his thesis, was rebuilt for G. Mills's thesis, and then programmed as a stand-alone toolkit\n"
    //+ "as part of the DiET project (EPSRC). It has been modified and extended as part of the DynDial project (Kings College & QMUL)\n"
    //+ "and also as part of ERIS (FP7 EU Project, G.J Mills, Stanford and University of Edinburgh).\n\n"+

     //"Contributors to the design, coding and documentation of the chat tool include:\n"
     //       + "Pat Healey, Jonathan Ginzburg, James King, Matt Purver, Arash Eshghi, Chris Howes and Eleni Gregoromichelaki."+
            
     //       "\n\nIf you would like to know more, please email:\n"+econtactname+"\n"+econtact+"\n";
    
    
    
    
    
    
    //end of paramater settings.
    
    public Conversation c;
    public IsTypingOrNotTyping itnt; //= new IsTypingOrNotTyping(this, param_isTypingTimeOut);
    public ParticipantPartnering pp;// = new ParticipantPartnering(c);
    public StyleManager sm = new StyleManager(this);
    public TaskControllerInterface tc;
    public Random r = new Random(new Date().getTime());
    public boolean experimentHasStarted = false; 
   
    
    
    
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
    
 
    public void  initializePostSetup(){
        //Called after everything has been set up and initialized on the GUI and chattool side
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

   
    public boolean requestParticipantJoinConversation(String participantID) {
        return true;
    }
    
    
            
    
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
    
    private Vector participantJoinedConversationButNotAssignedToGroup = new Vector();
    
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
    
    
    public void processWYSIWYGFloorRequest(Participant p, MessageWYSIWYGFloorRequest mwysiwygfr){
       
    }
                        
    
    public void processWYSIWYGFloorChangeConfirm(Participant sender, diet.message.MessageWYSIWYGFloorChangeConfirm mwysiwygfcc){
        
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
