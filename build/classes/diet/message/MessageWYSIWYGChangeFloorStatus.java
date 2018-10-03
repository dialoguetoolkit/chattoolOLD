/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.message;

/**
 *
 * @author gj
 */
public class MessageWYSIWYGChangeFloorStatus extends Message {

    public int newStatus;
    public boolean deletePendingInserts = false;
    public long serverID;
    
    public MessageWYSIWYGChangeFloorStatus(String email, String username, int newStatus, boolean deletePendingInserts, long serverID) {
        super(email, username);
        this.newStatus=newStatus;
        this.deletePendingInserts=deletePendingInserts;
        this.serverID=serverID;
    }
    
}
