# Cryptography
Created Thursday 02 November 2017

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

