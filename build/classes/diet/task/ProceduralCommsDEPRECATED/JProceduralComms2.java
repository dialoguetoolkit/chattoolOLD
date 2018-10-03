/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.ProceduralCommsDEPRECATED;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author gj
 */
public class JProceduralComms2 extends JFrame{
 
     
     ProceduralComms pc;
     JTextArea jTextArea1 = new JTextArea("");
     JTextArea jTextArea2 = new JTextArea("");
     
     
     
     public JProceduralComms2(ProceduralComms pc) {
        super("JPC");
        
        this.pc=pc;
        
        
        
       
        jTextArea1.setFont(new Font("monospaced", Font.PLAIN, 9) );
        jTextArea2.setFont(new Font("monospaced", Font.PLAIN, 9) );
        
        JScrollPane jsp1 = new JScrollPane(jTextArea1);
        JScrollPane jsp2 = new JScrollPane(jTextArea2);
     
        jsp1.setPreferredSize(new Dimension(300,200));
        jsp2.setPreferredSize(new Dimension(300,200));
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jsp1, BorderLayout.WEST);
        this.getContentPane().add(jsp2, BorderLayout.EAST);
       
        this.pack();
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        
    }
     
     public JProceduralComms2() {
        
    }
     
     
     
     public void displayMOVES(final String s){
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
                jTextArea1.setText(s);
           }
        });
        
    }
  
    
    public void appendEvaluationText(final String s){
         SwingUtilities.invokeLater(new Runnable(){
           public void run(){
                jTextArea2.append(s+"\n");
           }
        });
    }
    
    
    
    
}
