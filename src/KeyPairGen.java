import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import sun.security.x509.*;

import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class KeyPairGen {
	
	public static char[] KEYSTORE_PASS = "abcde".toCharArray();
	public static String KEYSTORE_PATH = System.getProperty("user.dir") + File.separator + "keyStores"+ File.separator;
	public static String CLIENT_KEYSTORE = "clientKeys.store";
	public static String SERVER_KEYSTORE = "serverKeys.store";
	public static String SERVER_ALIAS = "Server";
	public static String CLIENT_ALIAS = "Client";
	
	public static KeyPair generateKeyPair(){
		KeyPair kp = null;
		try {
			KeyPairGenerator factory = KeyPairGenerator.getInstance("RSA");
			factory.initialize(1024);
			kp = factory.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
		}
		return kp;	
	}

	//server keystore: private and public keys
	//client keystore: private and public keys
	//public keystore: server and client public keys
	private static void createKeyStores(KeyPair ckp, KeyPair skp){
		try{
			
			//create the keystore folder
			File keyStoreDir = new File(KEYSTORE_PATH);
			if(keyStoreDir.exists()){
				for(File file: keyStoreDir.listFiles()){
					file.delete();
				}
			}
			keyStoreDir.mkdirs();

			//initialize the keystores
			File cfile = new File(KEYSTORE_PATH + CLIENT_KEYSTORE);
			File sfile = new File(KEYSTORE_PATH + SERVER_KEYSTORE);
			KeyStore client = KeyStore.getInstance(KeyStore.getDefaultType());
			KeyStore server = KeyStore.getInstance(KeyStore.getDefaultType());
	
			client.load(null, null);
			server.load(null, null);
			
			//generate certificates
			X509Certificate clientCert = generateCertificate("CN=Jory, OU=JavaSoft, O=Sun Microsystems, C=CA", ckp, 100, "SHA1withRSA");
			X509Certificate serverCert = generateCertificate("CN=Tim, OU=JavaSoft, O=Sun Microsystems, C=CA", skp, 100, "SHA1withRSA");

			//sign the certificates (server is CA; is root cert)
			serverCert = createSignedCertificate(serverCert,serverCert,skp.getPrivate());
			clientCert = createSignedCertificate(clientCert,serverCert,skp.getPrivate());
			
			//build the certificate chain for verification
			Certificate[] clientCertChain = new Certificate[2];
			Certificate[] serverCertChain = new Certificate[2];

			clientCertChain[0] = clientCert;
			clientCertChain[1] = serverCert;
			
			serverCertChain[0] = serverCert;
			serverCertChain[1] = serverCert;
			
			//fill keystores with necessary data
			char[] privateKeyPass = "".toCharArray();
			client.setKeyEntry("ClientPrivate", ckp.getPrivate(),KEYSTORE_PASS, clientCertChain);
			client.setCertificateEntry("ClientCert", clientCert);
			client.setCertificateEntry("ServerCert", serverCert);

			server.setKeyEntry("ServerPrivate", skp.getPrivate(),KEYSTORE_PASS, serverCertChain);
			server.setCertificateEntry("ClientCert", clientCert);
			server.setCertificateEntry("ServerCert", serverCert);

			//write keystores to disk
			client.store(new FileOutputStream(cfile), KEYSTORE_PASS);
			server.store(new FileOutputStream(sfile), KEYSTORE_PASS);
			
			//verify client cert is signed by server's public key
			Certificate clientCheck = server.getCertificate("ClientCert");
			try{
				clientCheck.verify(server.getCertificate("ServerCert").getPublicKey());				
				System.out.println("Client cert valid. Signed by server's public key.");

			}catch(InvalidKeyException ivky){
				System.out.println("Client cert not valid. Not signed by server's public key.");
			}
			
			//verify server cert is signed by server's public key
			Certificate serverCheck = client.getCertificate("ServerCert");
			try{
				serverCheck.verify(client.getCertificate("ServerCert").getPublicKey());				
				System.out.println("Server cert valid. Signed by server's (the CA) public key.");

			}catch(InvalidKeyException ivky){
				System.out.println("Server cert not valid. Not signed by server's (the CA) public key.");
			}
			
		}catch(Exception e){
			System.out.println("AKeyGen: could not create key stores");
			e.printStackTrace();
		}
	}

	public static KeyStore retrieveKeyStore(String path){
		File ks = new File(path);
		KeyStore keyStore = null;
		
		try{
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(ks), KEYSTORE_PASS);
		}catch(KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e){
			System.out.println("Could not retrieve keyStore: " + path);
		}
		
		return keyStore;
	}
	
	public static KeyStore loadClientKeyStore(){
		return KeyPairGen.retrieveKeyStore(KEYSTORE_PATH + CLIENT_KEYSTORE);
	}
	public static KeyStore loadServerKeyStore(){
		return KeyPairGen.retrieveKeyStore(KEYSTORE_PATH + SERVER_KEYSTORE);
	}
	public static String b64Key(Key key){
		byte[] keyBytes = key.getEncoded();
		return Base64.encode(keyBytes);
	}
	public static String shortb64Key(Key key){
		byte[] keyBytes = key.getEncoded();
		return Base64.encode(keyBytes).substring(0, 25);
	}
	
	/** 
	 * Create a self-signed X.509 Certificate
	 * @param dn the X.509 Distinguished Name, eg "CN=Test, L=London, C=GB"
	 * @param pair the KeyPair
	 * @param days how many days from now the Certificate is valid for
	 * @param algorithm the signing algorithm, eg "SHA1withRSA"
	 */ 
	public static X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
	  throws GeneralSecurityException, IOException
	{
	  PrivateKey privkey = pair.getPrivate();
	  X509CertInfo info = new X509CertInfo();
	  Date from = new Date();
	  Date to = new Date(from.getTime() + days * 86400000l);
	  CertificateValidity interval = new CertificateValidity(from, to);
	  BigInteger sn = new BigInteger(64, new SecureRandom());
	  X500Name owner = new X500Name(dn);
	 
	  info.set(X509CertInfo.VALIDITY, interval);
	  info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
	  info.set(X509CertInfo.SUBJECT, owner);
	  info.set(X509CertInfo.ISSUER, owner);
	  info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
	  info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
	  AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
	  info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));
	 
	  // Sign the cert to identify the algorithm that's used.
	  X509CertImpl cert = new X509CertImpl(info);
	  cert.sign(privkey, algorithm);
	 
	  // Update the algorith, and resign.
	  algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
	  info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
	  cert = new X509CertImpl(info);
	  cert.sign(privkey, algorithm);
	  return cert;
	}   
	
    private static X509Certificate createSignedCertificate(X509Certificate cetrificate,X509Certificate issuerCertificate,PrivateKey issuerPrivateKey){
        try{
            Principal issuer = issuerCertificate.getSubjectDN();
            String issuerSigAlg = issuerCertificate.getSigAlgName();
              
            byte[] inCertBytes = cetrificate.getTBSCertificate();
            X509CertInfo info = new X509CertInfo(inCertBytes);
            info.set(X509CertInfo.ISSUER, (X500Name) issuer);
              
            //No need to add the BasicContraint for leaf cert
            if(!cetrificate.getSubjectDN().getName().equals("CN=TOP")){
                CertificateExtensions exts=new CertificateExtensions();
                BasicConstraintsExtension bce = new BasicConstraintsExtension(true, -1);
                exts.set(BasicConstraintsExtension.NAME,new BasicConstraintsExtension(false, bce.getExtensionValue()));
                info.set(X509CertInfo.EXTENSIONS, exts);
            }
              
            X509CertImpl outCert = new X509CertImpl(info);
            outCert.sign(issuerPrivateKey, issuerSigAlg);
              
            return outCert;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public static boolean verifySignature(Certificate certToVerify, Certificate caCert, Key caPubKey){
		boolean success = false;
		try{
			certToVerify.verify(caCert.getPublicKey());				
			//System.out.println("Cert valid. Signed by ca's public key.");
			success=true;
		}catch(InvalidKeyException ivky){
			//System.out.println("Cert not valid. Not signed by CA's public key.");
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
    
    public static byte[] encrypt(String message, Key key){
    	
    	byte[] encBytes = null;
    	
    	try {
        	Cipher pkCipher = Cipher.getInstance("RSA");
			pkCipher.init(Cipher.ENCRYPT_MODE, key);
			encBytes = pkCipher.doFinal(message.getBytes());
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return encBytes;
    
    }
    
    public static String decrypt(byte[] encBytes, Key key){
    	
    	String decrypted = null;
    	
    	try {
        	Cipher pkCipher = Cipher.getInstance("RSA");
			pkCipher.init(Cipher.DECRYPT_MODE, key);
			decrypted = new String(pkCipher.doFinal(encBytes));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return decrypted;
    
    }
		
    
	public static void main(String[] args){
		
		KeyPair server = KeyPairGen.generateKeyPair();
		KeyPair client = KeyPairGen.generateKeyPair();
		
		System.out.println("Server Private Key:\n" + b64Key(server.getPrivate()));
		System.out.println("Server Public Key:\n" + b64Key(server.getPublic()));
		System.out.println("Client Private Key:\n" + b64Key(client.getPrivate()));
		System.out.println("Client Public Key:\n" + b64Key(client.getPublic()));
		
		KeyPairGen.createKeyStores(client, server);
		System.out.println("Client and Server KeyStores created.");
	}
	
}
	

