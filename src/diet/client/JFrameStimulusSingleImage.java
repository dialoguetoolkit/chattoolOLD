/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import diet.attribval.AttribVal;
import diet.server.ConversationController.ui.CustomDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author gj
 */
public class JFrameStimulusSingleImage extends JFrame implements WindowListener, ActionListener{

    ConnectionToServer cts;
    JPanelStimulusSingleImage jpssi;
    JPanel jsp = new JPanel();
    Vector jbuttons = new Vector();
    
    
    
    
    public JFrameStimulusSingleImage(ConnectionToServer cts, int width, int height, String[] buttonnames) throws HeadlessException {
        super(cts.getUsername()); 
        this.addWindowListener(this);
         //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         
        this.getContentPane().setLayout(new BorderLayout());
        jpssi = new JPanelStimulusSingleImage(cts);
        jpssi.setPreferredSize(new Dimension(width,height));
        jpssi.setMinimumSize(new Dimension(width,height));
        jpssi.setMaximumSize(new Dimension(width,height));
       
        this.getContentPane().add(jpssi, BorderLayout.CENTER);
        this.cts = cts;
        
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        JPanel southPanelNorth = new JPanel();
        JPanel southPanelSouth = new JPanel();
        
        
        for(int i=0;i<buttonnames.length;i++){
           JButton jb = new JButton(buttonnames[i]);
           jb.setFocusable(false);
           
           southPanelNorth.add(jb);    
           this.jbuttons.add(jb);
           jb.addActionListener(this);
        }
        
        southPanel.add(southPanelNorth,BorderLayout.NORTH);
        southPanel.add(southPanelSouth,BorderLayout.SOUTH);
        
        this.getContentPane().add(southPanel,BorderLayout.SOUTH);
        
        
        this.setResizable(false);
        this.setVisible(true);
        this.pack();    
    }
    
    
    
    public void enableButtons(final String[] buttonnames, final boolean enable){
      
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                for(int i=0;i<buttonnames.length;i++){
                    String buttonname = buttonnames[i];
                    
                    for(int j=0;j<jbuttons.size();j++){
                        JButton jb = (JButton)jbuttons.elementAt(j);
                        
                        
                        if(jb.getText().equalsIgnoreCase(buttonname)){
                            jb.setEnabled(enable);
                            
                            AttribVal av1 = new AttribVal("name",buttonnames[i]);
                            String newstatus = "enable";
                            if(!enable)newstatus="disable";
                            AttribVal av2 = new AttribVal("newstatus",newstatus);
                            cts.cEventHandler.reportInterfaceEvent("button_enable", new Date().getTime(), av1,av2);
                            System.err.println("ENABLING");
                        }
                    }
                }
            }
        
        });
        
        
    }
    
    
    
    
    public void displayBackgroundColour(Color col){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jpssi.changeImage(Color.GRAY);
            }
        });    
    }
    
    
    public void displayImage(final String jarresourcename){
        
        if(jarresourcename==null)return;
        if(jarresourcename.equalsIgnoreCase(""))return;
        
        
        System.err.println("Trying to LOAD "+jarresourcename);
        BufferedImage image =null ;

        try {
             
             ClassLoader cldr = JFrameStimulusSingleImage.class.getClassLoader();
             URL url = cldr.getResource(jarresourcename);
             image = ImageIO.read(url);
             //image = ImageIO.read(new File("C:\\sourceforge\\experimentresources\\gridstimuli\\stimuliset5\\A01.png"));
       } catch (IOException ex) {
            // handle exception...
          
           ex.printStackTrace();
           
       }
        final BufferedImage finalimage = image;
        if(finalimage==null){
            return;
        }
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jpssi.changeImage(finalimage, jarresourcename);
            }
            
        });    
    }
    
    JFrameStimulusSingleImageCountdownThread jfssictCurrent;
    
    
     public void displayImage(final String jarresourcename, final long duration){
        
        System.err.println("Trying to LOAD1 "+jarresourcename);
        BufferedImage image =null ;

        try {
             
             ClassLoader cldr = JFrameStimulusSingleImage.class.getClassLoader();
             URL url = cldr.getResource(jarresourcename);
             image = ImageIO.read(url);
              System.err.println("Trying to LOAD2 "+jarresourcename);
             //image = ImageIO.read(new File("C:\\sourceforge\\experimentresources\\gridstimuli\\stimuliset5\\A01.png"));
       } catch (IOException ex) {
            // handle exception...
           ex.printStackTrace();
           System.err.println("ERROR TRYING TO LOAD3: "+jarresourcename);
           
       }
        final BufferedImage finalimage = image;
        final JFrameStimulusSingleImage jfssi = this;
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                
                
                jpssi.changeImage(finalimage, jarresourcename,duration);
                
                
            }
            
        });
        
    }
    
    
     /*
     Thread t = new Thread(){
                    public void run(){
                         
                        while(new Date().getTime()-startdisplaytime < duration)  {
                          try{
                            long remainingtime = new Date().getTime()-startdisplaytime; 
                            Thread.sleep(remainingtime+1);
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                        }
                       jfssi.displayBackgroundColour(Color.GRAY);
                      
                       }
                       
                };
     
     
     */
    
    
    

    @Override
    public void windowOpened(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
     public void windowClosing(WindowEvent we) {
         String result  = CustomDialog.getString("Please enter the password to close the window\nThis is to prevent 'accidental' closing of the window by participants!\n", "");
         if(result==null)return;
         if(result.equalsIgnoreCase("closedown")){
             cts.sendErrorMessage("THE CLIENT STIMULUS WINDOW OF:"+ cts.getEmail()+","+cts.getUsername() +" WAS CLOSED DOWN! AT" +(new Date().getTime()));
             System.err.println("THE CLIENT STIMULUS WINDOW WAS CLOSED DOWN!");
             System.exit(-1112223);
             
         }
    }
     
     public void actionPerformed(ActionEvent ae){
         if(ae.getSource() instanceof JButton){
             String text = ((JButton)ae.getSource()).getText();
             long timeOfAction = ae.getWhen();
             AttribVal av = new AttribVal("text",text);
             this.cts.cEventHandler.reportInterfaceEvent("button_press", timeOfAction, av);
             this.cts.sendButtonPresed(text,timeOfAction);
         }
     }
     
     
}
