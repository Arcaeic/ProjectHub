import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Generate keypairs and write to file.
 * @author Timothy
 *
 */
public class KeyPairGen {
	
	private static char[] KEYSTORE_PASS = "abcde".toCharArray();
	public static String KEYSTORE_PATH = System.getProperty("user.dir") + File.separator + "keyStores"+ File.separator;
	public static String CLIENT_KEYSTORE = "clientKeys.store";
	public static String SERVER_KEYSTORE = "serverKeys.store";
	public static String PUBLIC_KEYSTORE = "publicKeys.store";
	public static String SERVER_ALIAS = "The Server";
	public static String CLIENT_ALIAS = "The Client";
	
	
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
	
	private static void createKeyStore(String filepath, String name, String password){
		
	}
	
	//server keystore: private and public keys
	//client keystore: private and public keys
	//public keystore: server and client public keys
	private static void createKeyStores(){
		
		try{

			String path = System.getProperty("user.dir") + File.separator + "keyStores";
			File keyStoreDir = new File(path);
			if(keyStoreDir.exists()){
				for(File file: keyStoreDir.listFiles()){
					file.delete();
				}
			}
			keyStoreDir.mkdirs();

			String clientFilename = path + File.separator + "clientKeys.store";
			String serverFilename = path + File.separator + "serverKeys.store";
			String publicFilename = path + File.separator + "publicKeys.store";
			File cfile = new File(clientFilename);
			File sfile = new File(serverFilename);
			File pfile = new File(publicFilename);
			
			KeyStore client = KeyStore.getInstance(KeyStore.getDefaultType());
			KeyStore server = KeyStore.getInstance(KeyStore.getDefaultType());
			KeyStore pub = KeyStore.getInstance(KeyStore.getDefaultType());
	
			client.load(null, null);
			server.load(null, null);
			pub.load(null, null);
			
			client.store(new FileOutputStream(cfile), "abcde".toCharArray());
			server.store(new FileOutputStream(sfile), "abcde".toCharArray());
			pub.store(new FileOutputStream(pfile), "abcde".toCharArray());
		}catch(Exception e){
			System.out.println("AKeyGen: could not create key stores");
			e.printStackTrace();
		}
	}
	
	/*
	private static KeyStore createKeyStore(String filepath, String password ){
		
	}
	*/
	
	
	public static KeyStore retrieveKeyStore(String path) throws FileNotFoundException{
		File ks = new File(path);
		KeyStore keyStore = null;
		
		try{
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(ks), KEYSTORE_PASS);
		}catch(IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e){
			System.out.println("Could not retrieve keyStore: " + path);
		}
		
		return keyStore;
	}
	
	public static void main(String[] args){
		
		KeyPair server = KeyPairGen.generateKeyPair();
		KeyPair client = KeyPairGen.generateKeyPair();
		
		PrivateKey serverPrk = server.getPrivate();
		byte[] sPrk = serverPrk.getEncoded();
		System.out.println("Server Private Key:\n"+ Base64.encode(sPrk));
		
		KeyPairGen.createKeyStores();
		
		KeyStore serverKs = null;
		try {
			serverKs = KeyPairGen.retrieveKeyStore(KEYSTORE_PATH + SERVER_KEYSTORE);
			System.out.println("Retreived key store.");
			//Certificate cert = KeyPairGen.generateCertificate(KEYSTORE_PATH + "serverCert");
			Certificate cert = null;
			Certificate[] chain = {cert};
			serverKs.setKeyEntry("The Server", serverPrk , "".toCharArray(), chain);
			System.out.println("added server privatekey to store");
			PrivateKey serverPrkStore = (PrivateKey) serverKs.getKey(SERVER_ALIAS,"".toCharArray());
			System.out.println("Server Private Key from Store:\n" + Base64.encode(serverPrkStore.getEncoded()));
		} catch (FileNotFoundException | KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		

		
		
	}
	
	
	public static X509Certificate generateCertificate(String filePath){
		 InputStream inStream = null;
		 X509Certificate cert = null;
		 try {
		     inStream = new FileInputStream(filePath);
		     CertificateFactory cf = CertificateFactory.getInstance("X.509");
		     cert = (X509Certificate)cf.generateCertificate(inStream);
		 } catch (FileNotFoundException | CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 
		 return cert;
		
	}

}
