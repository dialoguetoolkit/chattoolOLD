/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.server.demomode;

import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author sre
 */
public class DemoModeChecker {
    
    
      
      
    
      public DemoModeChecker(){
           String r = System.getProperty("user.dir");
           System.out.println("DEMOMODECHECKER - USERDIR IS: "+r);
           File f1 = new File(r+File.separator+"data");
           if(!f1.exists()){
              f1.mkdir();
              System.out.println("Making data directory: "+f1.getAbsolutePath());
              
              
              JOptionPane.showMessageDialog(null, "It looks like you're running the chat tool without having downloaded all the files, libraries and utilities.\n"
                      + "This is only an issue if the script you want to run uses one of the following:\n"
                      + "(1) The Stanford Parser / Part of Speech tagger\n"
                      + "(2) Wordnet\n"
                      + "(3) Any custom libraries that you might have added to the chat tool package\n"
                      + "If this is the case, please download the full chat tool, following the instructions on:\n"
                      + "http://cogsci.eecs.qmul.ac.uk/diet/\n"
                      + "\n"
                      + "Otherwise, you can safely press OK\n","",
              JOptionPane.WARNING_MESSAGE);
              
              
           }
           
           
           
           File f2 = new File(r+File.separator+"data"+File.separator+"saved experimental data");
           if(!f2.exists()){
              f2.mkdir();
              System.out.println("Making saved experimental data");
             
           }
           
           
           
           
           
           
           System.out.println("Exiting demo mode checker");
           //System.exit(-23423) ;
           
           
      }
    
    
    
}
