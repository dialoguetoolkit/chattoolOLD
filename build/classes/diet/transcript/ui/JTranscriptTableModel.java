/*
 * JTurnsListTableModel.java
 *
 * Created on 06 October 2007, 16:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.transcript.ui;

import diet.server.conversation.ui.*;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ParticipantConnection;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import diet.server.conversationhistory.ConversationHistory;
import diet.transcript.Transcript;
import java.util.Date;
import javax.swing.SwingUtilities;

public class JTranscriptTableModel extends AbstractTableModel {

 
  private Conversation c;
  private Transcript tt;
  
  public TableModel thisTableModel;
  JTable jt;
  
  
  
  public JTranscriptTableModel(JTable jt, Transcript tt, Conversation c) {
     super();
     this.tt=tt;
     this.c=c;
   
     
     this.jt = jt;
     this.thisTableModel = this;
     
  }

  
        
        
        //data.addElement(v);
        //refresh();
        
    

  public boolean isCellEditable(int x, int y){
    return false;
  }

   public String getColumnName(int column){
     if(column==0){
         return "Is Text Deleted?";
     }
     else if(column==1){
         return "Text typed by self";
     }
     else if (column==2){
         return "Turns by self";
     }
       else if (column==3){
         return "Turns by other";
     }
    else if (column==4){
         return "Window1";
     }
    
    else{
         return " ";
     }
   }

  public Object getValueAt(int x, int y){
     try{ 
      return ((String[])tt.allRows.elementAt(x))[y];
     }catch (Exception e){
         e.printStackTrace();
     }
     return "ERROR";
  }
    
   
   

  public Class getColumnClass(int column) {
     return String.class; 
  
  }
  
  public int getRowCount(){
      return tt.getRowCount();
  }
  public int getColumnCount(){
     return 5;
  }

  public void updateData(){
       fireTableDataChanged();
       System.err.println("UPDATING THE DATA3");
      
  }

  
  
        
    
    
    
  
}