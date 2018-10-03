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
public class SimpleWhitelistOfDutchOK {
 
    
    
    
    String[] okWhitelistARRAY = {"ok", "okay", "oke","okee","okee", "okeee", "okey", "mkay", "mmmkay", 
        "yes", 
        "jes",
        "yeah", "yeahh", "yeahhh",
        "jeah","jeahh","jeahhh",
        "yeh","yehh","yehhh",
         "yea","yeaa","yeaaa",
         "ja", "jaa", "jaaa",
         "yees",
         "yeeh","yeehh"
    };
    
   
    Vector okWhitelist = new Vector();
    
    
    
    
    public   SimpleWhitelistOfDutchOK(){
         for(int i=0;i<okWhitelistARRAY.length;i++){
             String s = okWhitelistARRAY[i];
             okWhitelist.addElement(s);
         }
    }
    
     
      public boolean checkWhitelistForTurn(String turn){
            
          for(int i=0;i<okWhitelist.size();i++){
              String s = ((String)okWhitelist.elementAt(i)).trim();
              if(s.equalsIgnoreCase(turn)){
                   System.err.println(turn+" IS DETECTED IN WHITELIST");
                  return true;
                 
              }
          }
          return false;
      }
     
           
     
     
}
