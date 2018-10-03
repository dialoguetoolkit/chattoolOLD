/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.transcript.ui;

import java.util.Vector;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;

/**
 *
 * @author gj
 */
public class JTranscriptTextPane extends JTextPane{
    
    
       int columnWidth = 10;
    
    public String generateNCharacters(char c, int number){
        String retval="";
        for(int i=0;i<number;i++){
            retval = retval + c;
        }
        return retval;
    }
    
   
    public Vector splitStringIntoSubstringsOfLength(String s, int substringlength){
        //the last String might not be the same length
        Vector retValue = new Vector();
        int startIndex=0;
        
        for(startIndex=0;startIndex < s.length() ; startIndex = startIndex+substringlength){
            int endIndex = startIndex + substringlength;
            if(endIndex>s.length()) endIndex = s.length();
            retValue.addElement(s.substring(startIndex, endIndex)); 
            
            
        }
        if(2<5){
           System.err.println(s+" ...reduced to:");
           System.err.println(retValue);
           //System.exit(-5);
        
    }
        return retValue;  
    }
    
    
    
    
     
     
    public void appendStringOLD(String text,  int columnNumber, MutableAttributeSet mas){
       String leftMostPadding = this.generateNCharacters('*', columnNumber * columnWidth);
       Vector textSplit = this.splitStringIntoSubstringsOfLength(text, columnWidth);
       String textToBeDisplayed ="";
       for(int i=0;i<textSplit.size();i++){
           String s = leftMostPadding +(String)textSplit.elementAt(i);
           textToBeDisplayed = textToBeDisplayed +s ;
           if(i!=textSplit.size()-1)    textToBeDisplayed = textToBeDisplayed +"\n";
       }
       try{
          this.getDocument().insertString(this.getDocument().getLength(), textToBeDisplayed, mas);
       }catch(Exception e){
           e.printStackTrace();
       }
        System.err.println(textToBeDisplayed);
    }
    
    
   
}
      

