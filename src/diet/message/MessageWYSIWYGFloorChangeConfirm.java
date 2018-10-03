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
public class MessageWYSIWYGFloorChangeConfirm extends Message {

    public String text;
    long serverid;
    int newState ;
    
    public MessageWYSIWYGFloorChangeConfirm(String email, String username, int newState, long serverID) {
        super(email, username);
        this.text=text;
        this.serverid=serverID;
        this.newState=newState;
    }

    public String getText() {
        return text;
    }
    
    public long getID(){
        return this.serverid;
    }
    public int getNewState(){
      return this.newState;   
    }
}