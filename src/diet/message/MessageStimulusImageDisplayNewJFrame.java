/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.message;

import java.util.Vector;

/**
 *
 * @author Greg
 */
public class MessageStimulusImageDisplayNewJFrame extends Message{

    public String id;
    public int width;
    public int height;
    public String nameOfImage;
    public String[] buttonsUnderneath;

    
    public MessageStimulusImageDisplayNewJFrame(int width, int height, String nameOfImage , String[] buttonsUnderneath){
        super("server","server");
        this.id=id;
        this.width=width;
        this.height=height;
        this.nameOfImage=nameOfImage;
        this.buttonsUnderneath=buttonsUnderneath;
           
    }

    

   
}
