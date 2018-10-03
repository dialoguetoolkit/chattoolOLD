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
public class MessageWYSIWYGFloorRequest extends Message {

    public String text;
    
    public MessageWYSIWYGFloorRequest(String email, String username, String text) {
        super(email, username);
        this.text=text;
    }

    public String getText() {
        return text;
    }
    
    
    
}
