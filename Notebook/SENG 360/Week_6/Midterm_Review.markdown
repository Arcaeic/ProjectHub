# Midterm Review
Created Monday 23 October 2017

### Introduction
**Security Engineering:** Building systems to remain dependable in the face of malice, error, or mischance
**Confidentiality: **Prevent/react to unauthed reading
**Anonymity:** Being unknown or unacknowledged.
**Privacy: **Being free from being observed or disturbed by others
**Personal Identifiable Information **(PII): Information that can be used to identify you or help identify you.
**Non-interferance:** What the listener can do can not affect what user sees.
**Non-deducibility:** the user cannot connect anything the listener does
**Integrity: **Prevent/react to unauthed writing
**Non-interferance**: None of the users actions have an impact on what the pilot sees
**Availability: **Prevent/react to to the prevention or delay of authorized access.
**System:**

* A product, cryptographic protocol, hardware
* OS, comms, organization,
* applications, software,
* IT staff
* internal users/management
* customers/external


### Psychology and Usability
**Pretexing: **Creating a scenario to trick a victim into giving information or performing an act. Done by creating the illusion of legitimacy.
People are likely to help others who are friendly or have helped them in the past.
**Phishing:** Sending emails to individuals and organizations in hopes to trick them into giving away personal information
(See SMShing, Vishing)
**Pharming: **Changes the victim's host file, and uses links in emails. A DNS vulnerability allows the attacker to serve a fake website.
- Humans have limited memory and respond predictably to complexity
- Length is more important than complexity
- Complex passwords are often written down
- **Look-Up secrets: **Recovery keys, a set of secrets physcially or eletronically recorded. Something you have
- **Single-Factor OTP Device: **Software/Hardware based, randomly generating OTPs. Single factor, OTP does not require activation. Something you have.
- **Multi-Factor OTP Devices: **Generates OTPs after additional authentication. Something you know, or are.
- **Multi-Factor Cryptographic Devices: ** Hardware which performs cryptographic operations using cryptokeys and requires multi-factor. The device is something you have, and is activated by something you know or are.
- **Cracking Passwords**
- Brute Force
- Dictionary Attacks
- **CAPTCHAs: **Completely Automated Public Turing test to tell Computers and Humans Apart

### Protocols
Goals: **Authentication and Key Establishment**
**Brute Force:** Trying all possible entries in the keyspace, thru hash chaining, rainbow tables. 
**Replay**: An attacker copies a stream of messages b/w two parties, and replays to one or more parties.
**MITM:** An attacker secrets relays and possible alters the communications b/w two parties who believe talking to each other.
**Reflect: **The attacker connects to a target, the target sends the attacker a challenge. The attacker starts a new connection and sends the challenge as its own. It takes the result and replays it in the original connection
**Needham-Schroeder Protocol SSO:** Has large defect where B believes in the freshness of the key shared by B and A. Which means an attacker could use an old-ass session key and it would work.
**Kerberos:** Uses timestamps to ensure freshness of message
**Needham-Schroeder Publick-Key Protocol: **Uses public keys instead of session keys, a weakness is a replay attack if a key is comprimised.
**Diffie-Hellmann Key Protocol:** the one that uses modulus and shit.
A knows g, p, a -> g^A mod p
B knows g, p, b -> g^B mod p
Suppose p is a prime of around 300 digits, and a and b atleast 100. Discovering the shared secret would take longer than the lifetime of the universe, given the best known algorithm.
			

### Protocol Verification (BAN Logic)
**Jurisdiction Rule:** If P belives that Q has jurisdiction over X, then P trusts Q on the truth of X
**Seeing Rule:** If a principle once sent a message, it can see it. if a principle shares the proper keys, it can see the contents
**Meaning Rule: **P and Q shared a key K, and P can see {x} encrypted by k, therefore P believes Q sent x
**Freshness Rule:** If one piece of the package is fresh, the rest must be as well.
**Nonce-Verification:** A principle knows its nonce to be fresh, and the principle belives the message.


### Access Control
**Access Control (AC): **Selective restriction of access to a place or resource
**Access Control Matrix (ACM): **A 2D Matrix where columns are files and rows are users. The contents of each index are the rights for each subject over each file.
- Slow as fuck, a list would be better
**Access Control List (ACL):  **Stores each column of ACM with the object it represents
file1: {(Andy, rw), (Beth, rwxo), (Charla, r)}
- Can be long, combine users
- UNIX: 3 classes of users: owner, group, rest
**Capabilities-List (C-List): **Andy: {(file1, rx), (file2, r), (file3, rwo)}
- Inverse of ACL
**Differences b/w C-Lists and ACLs**
- ACL: Each file has an access list
 - C-list: Each subject has an access list
- Capabilities act as a ticket
- Possession of capability implies some access right(s)
- Tickets must be unforgable and uncopyable
**Advantages of C-Lists:**
- More Secure (Principle of Least Privilege)
-ACL: Subject will know the file exists, but cannot read it
-C-List: Subjection not given capability to see the file
**Advantages of ACLs:**
- Ease of revoking privileges
- ACLs: Just change ACL that is attached to object
-C-lists: Scan all C-lists, remove relevant capabilities
Expensive (maybe impossible for crypto-tickets)
**Identity Based AC: **Every person has a set of their own permissions
**Role-based AC: **Each person is assigned to a role with pre-determined permissions
**Attribute based AC:** Permissions are given to users through policys that evaluate attributes
**Discretionary AC: **Data owner determines who can access specific resources
**Mandatory AC:** Users do not have freedom to determine who can access their file


### Cryptography
**Terms: **Algorithm, plaintext, cryptext, key
- Cipher = Algorithm + Key
- **Caeser Cipher:** Alphabet modified by offset
- **Scytales: **Used by ancient greeks. Strings of paper, columns the text.
- **Cryptosystem: **5-tuple (E, D, M, K, C)
- M is a set of plaintexts
- K is a set of keys
- C is a set of ciphertexts
- E:		M x K	-> C is a set of enciphering functions
- D:		C x K	-> M is a set of deciphering functions
- **Symmetric Cryptosystems (Classical):** A single key can both encrypt and decrypt a message.
- **Asymmetric Cryptosystems: **A public key encrypts the file to ciphertext, then a private key decrypts it to plaintext.
- **Digital Signatures: ** Message is sent through a hash function, and the result is decrypted with the sender's private key. Which is a digital signature.
- **Block Cipher: ** Cryptographic key and algrotihm are applied to a block of data
- **Stream Cipher:** Encrypts one bit at a time
**Operating Modes for Turning Block Ciphers into Stream Ciphers:**
- Electronic Code Book (ECB)
 Encryption parallelizable: Yes
 Decrypt parallel: Yes
 Random Read: Yes
- Cipher Block Chaining (CBC)
Encryption Parallel: No
Decryption Parallel: Yes
Random Read: Yes
- Propagating Cipher Block Chaning (PCBC)
Encryption parallel: No
Decryption Paralel:  No
Random Read: No
- Cipher Feed Back (CFB)
Encryption Parallel: No
Decryption Parallel: Yes
Random read: Yes
- Output Feed Back (OFB)
Encryotion Parallel: No
Decryption Parallel: No
Random Read: No
- Counter Mode (CTR)
Encryption Paralell: Yes
Decryption Parallel: Yes
Random Read: Yes
----------------------------------------
Protocol Verification:
Write notes on BAN Logic paper
Verify if a protocol is secure
Demonstrate how an assumption might be made
If a principle uses a key, then it believes that key
Likewise, if a principle receives its a fresh, self- nounce, it believes the other party.

Protocols:
Demonstrate a vulnerability in the protocol
Identify different types of attacks that can be made


