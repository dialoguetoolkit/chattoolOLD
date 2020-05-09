************************
Programming the chattool  
************************



The chat tool is designed so that all information from the chat clients passes through a ``diet.server.ConversationController`` object.  The ``ConversationController`` sits in the middle between all the participants. 

All turns, keypresses, GUI events, keyboard events and task-information (e.g. game states in a joint referential task) are sent to the ConversationController object.  

When the ``ConversationController`` object receives a turn  from one of the clients, it automatically relays the turn to the other participants in the conversation.

It also automatically takes care of saving the data (i.e. all keypresses), and ensures, e.g. that the participants receive "is typing" notifications.


Think of the ConversationController object as a switchboard operator that controls the wiring of the switchboard. The plugs of the switchboard are almost all located in the ``diet.server.Conversation.java`` object.  

In 99% of experimental designs, you should only have to modify code in your custom subclass of ```diet.server.ConversationController``` which controls the interaction by calling code in ```diet.server.Conversation```


Workflow when programming the chattool
######################################

The standard approach to creating a new experiment is to:

1. Create a subclass of ``diet.server.ConversationController.DefaultConversationController``, e.g. ``MynewConversationController``

2. Make sure that MyNewConversationController.showCCOnGUI() returns true. (this will display your ConversationController class in the main GUI of the chattool.

3. Check that it runs by (select "Run" in netbeans) and then, in the launcher window of the chattool, select "MyNewConversationController" and press "Demo on local machine: Auto login". This should start everything, including the clients. 

4. Customize the ConversationController object, e.g.

   * modify ``processChatText(...)`` This method specifies what happens when a participant sends a message and it is received by the server. The default behaviour is to simply relay the message to all other participants. This method can be modified to, e.g. selectively block (shadowban) messages, or to transform messages (e.g. replace or add text), or to send entirely artificial turns. There is a large set of methods in ``diet.server.Conversation" for doing this, such as ``diet.server.Conversation.sendArtificialTurn(...)````
   * modify ```participantJoinedConversation(...)``` to specify what happens when a participant logs in (e.g. you could make it so that the servers sends some instructions to the participant, using e.g.  ```diet.server.Conversation.sendInstructionToParticipant(...)```
 
5. Test the setup locally (run as demo) 


6. Once the setup works properly, pilot the experiment on separate computers.
   

How to change the properties of chat interfaces 
###############################################

There are two ways of customizing the client interfaces:

The easiest way is to customize  ``DefaultConversationController.participantJoinedConversation(...)`` method so that whenever a participant logs in, the server immediately sends instructions to the client. For example::

   This is code
   so is this code 



You can edit diet.server.Configuration and then recompile the code.

2. You can edit the ``DefaultConversationController.participantJoinedConversation(...)`` method so that whenever a participant logs in, the server immediately sends instructions to the client. There is a whole set of 







   