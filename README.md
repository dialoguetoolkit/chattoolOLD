# Getting started

This repository is a netbeans project. If opened in netbeans it should run "out of the box"



## Speeding up testing cycle of conversation controller objects

After a while you will probably get bored of going through the process of 
1. Starting the chattool
2. Manually selecting the Conversation Controller object
3. Selecting start
4. Starting the right number of clients

To get round this you can start any Conversation Controller object, along with the required number of clients by using the following syntax:

```
javac -jar "chatool.jar" nogui_ccname_autologin CONVERSATIONCONTROLLERNAME
```

For example:

```java
javac -jar "chatool.jar" nogui_ccname_autologin DyadicTurnByTurnInterface
```

This will automatically start the chattool, load the ConversationController object, initialize the clients and log them in

The default number of clients to start is 2 (which is specified in 
```Configuration.defaultGroupSize = 2```
