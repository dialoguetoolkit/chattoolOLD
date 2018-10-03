/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.WhitelistBlacklistFilterTrigger;

import diet.server.ConversationController.ui.CustomDialog;
import diet.utils.VectorToolkit;
import java.io.File;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class WhitelistBlacklistFilterTrigger {
 
    String whitelistTurnFile ; 
    String whitelistWordFile ; 
    String blacklistTurnFile ; 
    String blacklistWordFile ; 
    String blacklistEndOfTurnFile;
    String blacklistPunctuationFile;
    
    
    Vector whitelistTurn = new Vector(); 
    Vector whitelistWord = new Vector(); 
    Vector blacklistTurn = new Vector(); 
    Vector blacklistWord = new Vector(); 
    Vector blacklistEndOfTurn = new Vector(); 
    Vector blacklistPunctuation = new Vector(); 
    
    
    
    
    public   WhitelistBlacklistFilterTrigger(String whitelistTurn, String whitelistWord, String blacklistTurn, String blacklistWord, String blacklistEndOfTurn, String blacklistPunctuation){
       whitelistTurnFile =  whitelistTurn;
       whitelistWordFile = whitelistWord; 
       blacklistTurnFile = blacklistTurn;
       blacklistWordFile = blacklistWord;
       blacklistEndOfTurnFile = blacklistEndOfTurn;
       blacklistPunctuationFile = blacklistPunctuation;
       loadVectOfStrings();
       
       JSimpleWhitelistOfDutchOK jswodo = new JSimpleWhitelistOfDutchOK(this);
       
    }
    
    public void loadVectOfStrings (){
        
       whitelistTurn = VectorToolkit.getVectorTextFromJarTextFile(whitelistTurnFile, this);
       whitelistWord = VectorToolkit.getVectorTextFromJarTextFile(whitelistWordFile, this);
       blacklistTurn = VectorToolkit.getVectorTextFromJarTextFile(blacklistTurnFile, this);
       blacklistWord = VectorToolkit.getVectorTextFromJarTextFile(blacklistWordFile, this);
       blacklistEndOfTurn = VectorToolkit.getVectorTextFromJarTextFile(blacklistEndOfTurnFile, this);
       blacklistPunctuation = VectorToolkit.getVectorTextFromJarTextFile(blacklistPunctuationFile, this);
        
       if(whitelistTurn.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN WHITELIST TURN VECTOR");
       if(whitelistWord.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN WHITELIST WORD VECTOR");
       if(blacklistTurn.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN BLACKLIST TURN VECTOR");
       if(blacklistWord.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN BLACKLIST WORD VECTOR");
       if(blacklistEndOfTurn.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN BLACKLIST END OF TURN VECTOR");
       if(blacklistPunctuation.size()==0) CustomDialog.showDialog("ERROR: DID NOT LOAD IN BLACKLISTPUNCTUATION VECTOR");
       
    }
    
     //("/simplifiedsetsof11/simplified11set1/cl1mzes.v");
    //C    "/discoursepragmatics/whitelistturns.txt"
     
    
     public static void main(String[] args){
        WhitelistBlacklistFilterTrigger wlblft = new WhitelistBlacklistFilterTrigger("/whitelistblacklist/whitelistturns.txt", 
                "/whitelistblacklist/whitelistwords.txt",
                "/whitelistblacklist/blacklistturns.txt",
                "/whitelistblacklist/blacklistwords.txt",
                "/whitelistblacklist/blacklistendofturns.txt",
                "/whitelistblacklist/blacklistpunctuation.txt"
        );
         
       
        wlblft.isContainedInWhitelistsAndNotInBlacklists("I am there row");
          
        
     }
    
     
     public String prepareForFiltering(String s){
         String result = "";
         String sLowerCase = s.toLowerCase();
         char previouscharacter = ' ';
         for(int i=0;i<sLowerCase.length();i++){
             char c = sLowerCase.charAt(i);
             if(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c)  ){
                  if(Character.isWhitespace(previouscharacter) && Character.isWhitespace(c)){
                      //do nothing...it is a run of spaces    
                  }
                  else{
                      result = result + c;
                  }        
             }
             else{
                 result = result + " ";
             }
             previouscharacter = c;
         }
         String secondpass = "";
         for(int i=0;i<result.length();i++){
             char c = result.charAt(i);
             if(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c)  ){
                  if(Character.isWhitespace(previouscharacter) && Character.isWhitespace(c)){
                      //do nothing...it is a run of spaces    
                  }
                  else{
                      secondpass = secondpass + c;
                  }        
             }
             previouscharacter = c;
         }
   
          //convert to lowercase
         //convert all punctuation to spaces
         //replace all multiple whitespace with single whitespace
         //add whitespace at the end
         //remove 
         
         return secondpass.trim();
     }
     
     
      public boolean checkWhitelistForTurn(String turnFILTERED){
            
          for(int i=0;i<whitelistTurn.size();i++){
              String s = ((String)whitelistTurn.elementAt(i)).trim();
              if(s.equalsIgnoreCase(turnFILTERED)){
                   System.err.println(turnFILTERED+" IS DETECTED IN WHITELIST");
                  return true;
                 
              }
          }
          return false;
      }
      public boolean checkWhitelistForWord(String needle){
          for(int i=0;i<whitelistWord.size();i++){
              String s = ((String)whitelistWord.elementAt(i)).trim();
              String sWithAddedSpace = " "+s+" ";
              if((" "+needle+" ").contains(sWithAddedSpace)) {
                   System.err.println(s + " is a word contained in ("+needle+ ")  (WHITELIST)");
                  return true;
              }
          }
          return false;
      }
      
       public boolean checkBlacklistForTurn(String turnFILTERED){
            
          for(int i=0;i<blacklistTurn.size();i++){
              String s = ((String)blacklistTurn.elementAt(i)).trim();
              if(s.equalsIgnoreCase(turnFILTERED)){
                   System.err.println(turnFILTERED+" IS DETECTED IN BLACKLIST");
                  return true;
                 
              }
          }
          return false;
      }
      public boolean checkBlacklistForWord(String needle){
          for(int i=0;i<blacklistWord.size();i++){
              String s = ((String)blacklistWord.elementAt(i)).trim();
              String sWithAddedSpace = " "+s+" ";
              if((" "+needle+" ").contains(sWithAddedSpace)) {
                   System.err.println(s + " is a word contained in ("+needle+ ") (BLACKLIST)");
                  return true;
              }
          }
          return false;
      }
     
     
      public boolean doesStringEndWithBlacklist(String turnRAW){
          String turnRAWLOWER = turnRAW.toLowerCase();
          for(int i=0;i<this.blacklistEndOfTurn.size();i++){
              String sBlackListEndOfTurnLower = ((String)blacklistEndOfTurn.elementAt(i)).toLowerCase();
              if(turnRAWLOWER.endsWith(sBlackListEndOfTurnLower)){
                  return true;
              }
          }
          return false;
      }
      
      public boolean doesStringContainBlacklistPunctuation(String turnRAW){
          String turnRAWLOWER = turnRAW.toLowerCase();
          for(int i=0;i<this.blacklistPunctuation.size();i++){
              String sBlackListPunctuationLower = ((String)blacklistPunctuation.elementAt(i)).toLowerCase();
              if(turnRAWLOWER.contains(sBlackListPunctuationLower)){
                  return true;
              }
          }
          return false;
      }
      
      
      
      public boolean isContainedInWhitelistsAndNotInBlacklists(String turnRAW){
          //this method is lazy!
         
          if(this.doesStringContainBlacklistPunctuation(turnRAW)){
                System.err.println("Turn has bad punctuation");
                return false;
          }
          
          if(this.doesStringEndWithBlacklist(turnRAW)){
              System.err.println("Turn ending is blacklisted!");
              return false;
          }
          
          
          String  turnFILTERED = this.prepareForFiltering(turnRAW);
          boolean detectedInTurnWhitelist = false;
          boolean detectedInWordWhitelist = false;
          boolean detectedInTurnBlacklist = false;
          boolean detectedInWordBlacklist = false;
          
          
          detectedInTurnWhitelist = checkWhitelistForTurn(turnFILTERED);
          if(!detectedInTurnWhitelist){
              detectedInWordWhitelist = checkWhitelistForWord(turnFILTERED);
          } 
          if(!detectedInTurnWhitelist & !detectedInWordWhitelist) {
              System.err.println("Couldn't find: "+turnRAW+" in either whitelist");
              return false;
          }
          
          
          detectedInTurnBlacklist = checkBlacklistForTurn(turnFILTERED);
          if(!detectedInTurnBlacklist){
              detectedInWordBlacklist = checkBlacklistForWord(turnFILTERED);
          } 
          if(detectedInTurnBlacklist || detectedInWordBlacklist){
              return false;
          }
          System.err.println(turnRAW + " was detected in whitelist but not in blacklists");
          return true;
      }
           
     
      
      public void addToWhitelistWord(String s){
          if(s.equalsIgnoreCase(""))return;
          whitelistWord.addElement(s);
      }
      public void addToWhitelistTurn(String s){
            if(s.equalsIgnoreCase(""))return;
          whitelistTurn.addElement(s);
      }
      public void addToBlacklistTurn(String s){
            if(s.equalsIgnoreCase(""))return;
          blacklistTurn.addElement(s);
      }
      public void addToBlacklistWord(String s){
            if(s.equalsIgnoreCase(""))return;
          blacklistWord.addElement(s);
      }
      public void addToBlacklistEndOfTurn(String s){
            if(s.equalsIgnoreCase(""))return;
          blacklistEndOfTurn.addElement(s);
      }
      public void addToBlacklistPunctuation(String s){
            if(s.equalsIgnoreCase(""))return;
          blacklistPunctuation.addElement(s);
      }
     
     public void panicReset(){
         this.loadVectOfStrings();
     }
      
      
      
}
