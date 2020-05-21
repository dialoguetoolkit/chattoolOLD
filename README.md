There are two sets of docs:

If you want to use the toolkit without programming, see the usermanual in the /docsusermanual folder

The docs for programming the toolkit are available at: https://dialoguetoolkit.readthedocs.io/en/latest/



# Getting started

This repository is a netbeans (https://netbeans.org/) project. If opened in netbeans it should run "out of the box"

If the jar file is run without any command line parameters it will show a GUI that lets you choose whether to run the server or the client.


## Starting the server

To start the server, use ```java -jar "chattool.jar SERVER``` 

The server will listen for incoming connections on port 20000

To change this, use ```java -jar "chattool.jar SERVER  %PORTNUMBER%```  where %PORTNUMBER% is the portnumber


## Starting the client

To start a client from the commandline you need to specify the IP address and port number to connect to

```java -jar "chattool.jar" CLIENT localhost 20000```

It is also possible to start clients (locally) from the GUI by selecting the button "Additional Client" 



## Speeding up testing cycle of conversation controller objects

The process of starting the server and clients and then logging in is quite tedious when developing. To get round this you can start Conversation Controller objects, along with the required number of clients by using the following syntax:

```
javac -jar "chatool.jar" nogui_ccname_autologin CONVERSATIONCONTROLLERNAME
```

For example:

```java
javac -jar "chatool.jar" nogui_ccname_autologin DyadicTurnByTurnInterface
```

This will automatically start the chattool, load the ConversationController object, initialize the clients and log them in.

The default number of clients to start is 2 (which is specified in ```Configuration.defaultGroupSize```

These can be saved in netbeans project configurations


# Programming the chat-tool

## ConversationController object

The main part of the chattool is the ConversationController object. The chattool is designed so that when you program an experiment you only need to modify code in the ConversationController object. 

The ConversationController sits in the middle between all the participants. All chattext, GUI events, keyboard events are sent to the ConversationController object.



## Workflow when programming the chattool

The standard approach to creating a new experiment is to:

1. Create a subclass of ```DefaultConversationController``` , e.g. ```MyNewConversationController```
2. make sure that ```showCCOnGUI()``` returns true (this will display your ConversationController class in the main GUI of the chat-tool)
3. To speed up debugging/testing, there is a command-line option that starts the server, loads your ConversationController object, starts 2 clients (no. of clients can be changed in the settings), and logs the clients into the server. Assuming your new ConversationController object is ```MyConversationController```, use the following command: ```javac -jar "chatool.jar" nogui_ccname_autologin MyNewConversationController```
4. Customize the conversation controller object, e.g. 
   * modify ```participantJoinedConversation(...)``` to specify what happens when a conversation logs in
   * modify ```processChatText(....)```. This method specifies what happens when a participant sends a message. The default behaviour is to simply relay the message to all other participants. This method can be modified to, e.g. selectively block (shadowban) messages, or to transform messages (e.g. replace or add text), or to send entriely artificial turns. There is a large set of methods in ```Conversation```, such as ```Conversation.newsendArtificialTurn(....)``` for doing this.
5. Test the setup locally (run as demo) or by using the method above (Speeeding up testing cycle of conversation controller objects)

### 




# The data collected 

All the data collected from an experiment is stored in a subfolder /data/saved/experimental data


## Turn-By-Turn interface
The main file is turns.txt, which is a CSV file with the "¦" character as separator. It contains the following columns:

**ExperimentID**
An identifier of the type of experiment (This is automatically generated by the ConversationController object)


**ServerTimestampOfSavingToFile**
This is the timestamp, recorded on the server,  when the row of data was saved to the CSV file


**SubdialogueID**
This identifies the "subdialogue" in the interaction. This is only relevant for experiments which involve multiparty interactions where multiple groups speak with each other simultaneously. Each group is assigned a separate subdialogueID.


**Turntype**
This identifies what kind of data is stored in that particular role in the CSV file. Turns produced by participants are saved as "NormalTurn". There are other types of data, e.g. "servermessage" - which are messages that were sent to the clients from the server.


**SenderID**
This is the Participant ID of the Participant who sent the message.


**SenderUsername**
This is the username of the Participant.


**ApparentSender**
This is who the participant appears to be to the recipient of the message. This is only relevant for turns that are spoofed. For example, if Participant C receives a message that was created by Participant A, but appears to be sent from Participant B.
(This shows who the recipient thinks sent the message)


**Text**
This is the text that was sent.


**Recipient(s)**
The participant(s) that received the message.


**NoOfDocumentDeletes**
This is the number of characters that were deleted in the text formulation window. Usually this is the same as NoOfKeypressDeletes (below) - but some people select a large chunk of text and delete or replace it with other text - this captures these deletions. 


**NoOfKeypressDeletes**
This is the number of times the participant pressed the physical Delete key on the keyboard while formulating the turn.


**ClientTimestampONSET**
This is the time (in msecs) of the first keypress, recorded on each client. 


**ClientTimestampENTER**
This is the time (in msecs), also recorded on the client, when the participant pressed ENTER and sent the message. 


**ServerTimestampOfReceiptAndOrSending**
This is the time (in msecs), recorded on the server, when the message from the client was received on the server.


**TextAsformulatedTIMING**
This represents all the keypresses that were typed when producing the turn. Subscripts indicate the number of milliseconds since the preceding keypress. For example: 


 ⁰H ¹¹⁰e ²⁸⁹l ¹⁸²l ³⁴⁸o ⁹⁹³  ³⁵⁴h ¹⁴²o ¹⁶¹w ¹⁴³  ²¹²a ¹⁹⁹r ⁹²e ⁵⁴¹  ²⁴³y ²²⁴o ²⁴¹u ⁵⁷⁷? ⁶⁸²¹ENTER

means "Hello how are you?" 


This means:
⁰⊆ ¹⁶⁰⁶⊇ ⁴⁴⁵⊆ ³⁷²³⊇ ⁶³⁰⁷【Participant1: Hello how are you?】 ¹⁶⁹³I ¹³⁶  ¹⁴⁸a ²¹⁹m ¹⁷⁹  ²³⁰f ¹²⁹i ³⁰⁹n ²⁰⁵e ¹³⁸³ENTER

⁰⊆ ¹⁶⁰⁶⊇   means that the participant received the "is typing" indicator from 0 to 1606 ms

⁴⁴⁵⊆ ³⁷²³⊇  means that the participant received the "is typing" indicator 445 msecs later which lasted for 3723 msecs. 

⁶³⁰⁷【Participant1: Hello how are you?】 means that the participant received the text "Participant1: Hello how are you?"

¹⁶⁹³I ¹³⁶  ¹⁴⁸a ²¹⁹m ¹⁷⁹  ²³⁰f ¹²⁹i ³⁰⁹n ²⁰⁵e ¹³⁸³ENTER  means that the participant typed "I am fine"




**TextAsFormulatedLOGSPACE**

This prettifies the output of TextAsFormulatedTIMING


**istypingtimeout**
The parameter that determines how long the "is typing" indicator when a participant presses a key.



## WYSIWYG Interface

See \docs\wysiwyg_interface.docx 

