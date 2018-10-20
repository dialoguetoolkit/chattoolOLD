# About

The toolkit is an instant-messaging platform for conducting research on dialogue. The software consists of a chat server and chat clients, written in Java.

The chat-tool makes data-collection and preparation much easier and quicker. Configuration of the server and clients allows experimental designs that are much more standardized and reproducible.
The toolkit can be used "off-the-shelf" to collect and prepare instant-messaging conversations. It is also a powerful, programmable toolkit for conducting experiments on live interaction.

All participants' turns are routed via the server which can be scripted to detect, for example target words, phrases or syntactic constructions. This information can then be used, in real-time to trigger experimental interventions that are sensitive to the interactional context.

The toolkit provides an extensive API for scripting these manipulations, as well as a constantly expanding library of experimental setups that can be reconfigured

# Features

## Recorded data

<table>
   <tr>
    <td>The chattool automatically records all keypresses, words and turns, notifications (including typing notifications and read receipts), number of edits, typing speed, typing overlap.

Instead of costly and time-intensive transcription, all data is immediately available for analysis. The data is saved in a variety of formats - to help both qualitative and quantitative analyses (e.g. loading into SPSS, R, Excel, MATLAB).
</td>
    <td><img align="right" src="https://raw.githubusercontent.com/gjmills/dialoguetoolkit/master/documentation/imgs/feature-image-2.png"></td>
  </tr>
</table>

## Manipulating the interface

Existing software (e.g. whatsapp, viber, line, wechat) use subtly different interfaces that have undocumented, ad-hoc and constantly changing features. The effect of these interface features on interaction are currently poorly understood.
The toolkit allows configuration of all aspects of the interface, including the screen dimensions, text (colour, font, positioning), typing synchrony and interleaving of turns, typing status, read receipts, as well as simulating network conditions, e.g. latency. 



## Manipulating the interaction

Possible kinds of experimental intervention include: 

* **Conceptual & linguistic coordination**

  * Manipulation of lexical, syntactic and semantic constituents (e.g. by inserting "spoof" clarification requests into the  interaction that target a specific element - see image to the right).
  * Manipulation of the specificity of referring expressions.
  * Manipulation of priming & levels of alignment.

* **Procedural coordination**

  * Manipulation of the timing and sequencing of turns.

* **Group-membership and identity:**

  * Manipulation of participatory status (e.g overhearer vs. bystander).
  * Manipulation of (apparent) identity of participant.
  * Assigning participants to different sub-groups, e.g. to allow group-specific conventions to emerge.


<A href = "/docs/mazegame.md">Maze game</A>
