/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ParticipantPartnering.ui;

import diet.server.Conversation;
import diet.server.ParticipantPartnering.ParticipantPartnering;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author gj
 */
public class JPanelParticipantPartnering extends JPanel implements ListSelectionListener{

    ParticipantPartnering pp;
    JParticipantPartneringTable jppt;
    JScrollPane jsp = new JScrollPane();
    public JPanelParticipantPartneringSouth jppps ;//= new JParticipantsPanelSouth(cH.getConversation());
    
    
    
    public JPanelParticipantPartnering(ParticipantPartnering pp)  {
        this.pp=pp;
        
        
        
        jppps = new JPanelParticipantPartneringSouth(pp, this);
       
        this.setLayout(new BorderLayout());
        jppt = new JParticipantPartneringTable (pp.c.getHistory());
        jsp.getViewport().add(jppt);
        this.add(jsp, BorderLayout.CENTER);
       
        this.add(jppps, BorderLayout.SOUTH);
        
        jppt.getSelectionModel().addListSelectionListener(this);
        
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        jppps.validateButtonEnabled();
    }
    
    
    
    
     public void updateData(){
       
        final JParticipantPartneringTableModel jpptm = (JParticipantPartneringTableModel)jppt.getModel();
       
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
                jpptm.updateData();        
           }
        });
       
        
        
    }
   
    
}
