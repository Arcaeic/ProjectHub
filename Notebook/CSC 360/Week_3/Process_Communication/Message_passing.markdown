# Message passing
Created Friday 19 January 2018

Message passing: an interface
Send message
Send()
Receive message
receive()
Communication link
Physical (e.g memory, bus, network)
logical (e.g., logical propertieS)

![](./Message_passing/pasted_image.png)

#### Direct Communication
Send a message to process C
send(C, message)
receive message from process P
receive(P, message)
Communication links
one link for one pair
one pair needs one link
![](./Message_passing/pasted_image001.png)


#### Indirect communication
Send a message to mailbox A
send(A, message)
Receive a message from mailbox A
receive(A, message)
Communication links and mailboxes
one link by many pairs
many links for one pair
mailbox owner



