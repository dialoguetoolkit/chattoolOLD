
# Introduction

The toolkit is a text-based chat tool for carrying out experiments on dialogue. The basic idea of the chat tool is that it intercepts participants' typed turns and selectively interferes with their content. This allows very fine-grained experimental control over what participants perceive the other participants as having typed. The chat tool has a constantly expanding library of experiments that can be reconfigured. It also provides an extensive API that allows the programmer to design experimental interventions that are sensitive to the conversational context of the participants.

# Equipment required to run the software

The following equipment is required to run experiments using the software:
3 or more computers, which can be any combination of Windows PC, Linux or Apple machines:

* One computer runs as server, and is used by the experimenter to observe the conversation. This computer runs the server software, which is typically scripted by the experimenter to interfere with participants’ turns.

* Two or more computers run the client software. This program runs on each participant’s computer. It provides an interface that displays the conversation and allows the participants to type text to each other. 

# Network connection.

The clients must be able to connect to the server. The server has to be able to accept incoming connections on at least one port (you might need to ask systems admin to "open" a port on the firewall). Any combination of WIFI / ethernet should work as long as the server can accept incoming connections from the clients.

# Software required:

The chat tool is written in java and runs on windows, mac, linux.

It is strongly recommended to use the most recent version of java for your platform. Please avoid using java 1.8  (java 1.7 works ok, and so do versions above 1.8 - this is due to some issues in the graphics libraries in some versions of java 1.8)
