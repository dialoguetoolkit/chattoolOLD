/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.keylogging;

import java.io.*;
import java.util.Date;

/**
 *
 * @author sre
 */
public class KeyLoggingOutput {
    
     File dataFile;
     BufferedWriter bfDATA;
     BufferedWriter bfREADABLENOUSERNAME;
     BufferedWriter bfREADABLEWITHUSERNAME;
     
     File readableFileNOUSERNAME;
     File readableFileUSERNAME;
     
     String username = "unset";
     
     
     public KeyLoggingOutput(String s){
           username = s;
           String directory = System.getProperty("user.dir");
           System.out.println(directory);
           long datetime = new Date().getTime();
           dataFile = new File(directory+File.separator+s+(datetime)+"DATA"+".txt");  
           this.readableFileNOUSERNAME = new File(directory+File.separator+s+datetime+"READABLE_NO_USERNAME"+".txt");
           this.readableFileUSERNAME = new File(directory+File.separator+s+datetime+"READABLE"+".txt");
           
           
           try{
             FileWriter fw = new FileWriter(dataFile);
             bfDATA =new BufferedWriter(fw);
             bfDATA.write("EventType"+"|"+"Time"+"|"+"Character"+"|"+"Keycode"+"|"+"ContentsOfTextArea"+"\n");
             bfDATA.flush();
             
             FileWriter fw2 = new FileWriter(readableFileNOUSERNAME);
             FileWriter fw3 = new FileWriter(readableFileUSERNAME);
             bfREADABLENOUSERNAME =new BufferedWriter(fw2);
             bfREADABLEWITHUSERNAME = new BufferedWriter(fw3);
            
            
           
             
             
           }catch (Exception e){
               e.printStackTrace();
               System.err.println("ERROR!");
           }  
          
           
     }
    
    
    
     public void saveKeypress(String event, long timeOfEvent, String character, String keycode, String contentsOfTextArea){
          try{
              if(character.equalsIgnoreCase("\n")&&event.equals("Keyreleased")){
                   bfREADABLENOUSERNAME.write(contentsOfTextArea+"\n");
                   bfREADABLEWITHUSERNAME.write(username+": "+ contentsOfTextArea+"\n");
                   bfREADABLENOUSERNAME.flush();
                   bfREADABLEWITHUSERNAME.flush();
              }
              
              
              bfDATA.write(event+"|"+timeOfEvent+"|"+character.replace("\n", "(NEWLINE)") +"|"+keycode +"|"+contentsOfTextArea.replace("\n", "")+"\n");
              bfDATA.flush();
          }catch (Exception e){
              e.printStackTrace();
          }  
     }
     
     
     public void saveSynchronizationData(long startTime, int hertz, long finishTime){
         
        try{    
          bfDATA.write("SYNC_IMMEDIATELY_PRESOUND"+"|"+startTime+"|"+hertz +"|"+"" +"|"+"\n");
          bfDATA.write("SYNC_IMMEDIATELY_POSTSOUND"+"|"+finishTime+"|"+hertz +"|"+"" +"|"+"\n");
          bfDATA.flush();
        }catch (Exception e){
            e.printStackTrace();
        }  
             
     }
}
