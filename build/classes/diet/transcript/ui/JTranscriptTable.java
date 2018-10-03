/*
 * JTurnsListTable.java
 *
 * Created on 06 October 2007, 16:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.transcript.ui;

import diet.server.Conversation;
import diet.transcript.Transcript;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

public class JTranscriptTable extends JTable {///implements JDiETTableRowFilter{

  private JTranscriptTableModel jptm;
 
  

  public JTranscriptTable(Transcript tt,Conversation c) {
    super();
    //this.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
    jptm = new JTranscriptTableModel(this,tt,c);
    this.setModel(jptm);
    this.setGridColor(Color.lightGray);
    setSize();
    
  }

 
  

 private void setSize(){
    

 }
 
 public String getColumnName(int column){
     return this.jptm.getColumnName(column);
 }




 public void updateData(){
       
        final JTranscriptTableModel jptm = (JTranscriptTableModel)this.getModel();
       
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
                jptm.updateData();        
           }
        });
       
        
        
    }

  
 
 //MultiLineTableCellRenderer tcr4 = new MultiLineTableCellRenderer();
 
 public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0 ) {
            return super.getCellRenderer(row, column);
        }
        else {
            return super.getCellRenderer(row, column);
        }
       
    }

}
