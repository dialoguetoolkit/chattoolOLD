/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.transcript.ui;

import diet.server.conversation.ui.*;
import diet.server.conversationhistory.ConversationHistory;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author gj
 */
public class JTranscriptPanel extends JPanel {
    
    
    
    
    ConversationHistory cH;
    JTranscriptTable jpt;
    JScrollPane jsp = new JScrollPane();
    JTranscriptPanelSouth jpps = new JTranscriptPanelSouth();
    
    public JTranscriptPanel(ConversationHistory cH){
        this.cH=cH;
        //this.add(new JLabel("THIS"));
        //this.add(new JButton("THIS"));
        this.setLayout(new BorderLayout());
        //jpt = new JTranscriptTable (cH);
        jsp.getViewport().add(jpt);
        this.add(jsp, BorderLayout.CENTER);
        //this.add(new JLabel("SOUTH"), BorderLayout.SOUTH);
        //this.add(new JLabel("NORTH"), BorderLayout.SOUTH);
        this.add(jpps, BorderLayout.SOUTH);
      
    }
    
    
   
}
