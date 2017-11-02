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
import sun.security.x509.*;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Date;


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
	private static void createKeyStores(KeyPair ckp, KeyPair skp){
		
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
			KeyStore pub = KeyStore.getInstance("JCEKS");
	
			client.load(null, null);
			server.load(null, null);
			pub.load(null, null);
			
			Certificate[] nullChain = {null, null};

			X509Certificate clientCert = generateCertificate("CN=Dave, OU=JavaSoft, O=Sun Microsystems, C=US", ckp, 100, "SHA1withRSA");
			X509Certificate serverCert = generateCertificate("CN=Tim, OU=JavaSoft, O=Sun Microsystems, C=US", skp, 100, "SHA1withRSA");
			
			clientCert.getPublicKey();
			serverCert.getPublicKey();
			

			if(client == null || server == null){
				System.out.println("null");
			}else if(clientCert == null || serverCert == null){
				System.out.println("nul null");
			}
			
			//server is the Certificate Authority --> will sign all other certs
			serverCert = createSignedCertificate(serverCert,serverCert,skp.getPrivate());
			clientCert = createSignedCertificate(clientCert,serverCert,skp.getPrivate());
			Certificate[] clientCertChain = new Certificate[2];
			Certificate[] serverCertChain = new Certificate[2];

			clientCertChain[0] = clientCert;
			clientCertChain[1] = serverCert;
			
			serverCertChain[0] = serverCert;
			serverCertChain[1] = serverCert;
			
			client.setKeyEntry("ClientPrivate", ckp.getPrivate(), "".toCharArray(), clientCertChain);
			client.setCertificateEntry("Client - Cert", clientCert);
			client.setCertificateEntry("Server - Cert", serverCert);

			server.setKeyEntry("ServerPrivate", skp.getPrivate(), "".toCharArray(), serverCertChain);
			server.setCertificateEntry("Client - Cert", clientCert);
			server.setCertificateEntry("Server - Cert", serverCert);
			
			//is the public keystore necessary?
			
			client.store(new FileOutputStream(cfile), "abcde".toCharArray());
			server.store(new FileOutputStream(sfile), "abcde".toCharArray());
			pub.store(new FileOutputStream(pfile), "abcde".toCharArray());
			
			//verify client cert is signed by server's public key
			Certificate clientCheck = server.getCertificate("Client - Cert");
			try{
				clientCheck.verify(server.getCertificate("Server - Cert").getPublicKey());				
				System.out.println("Client cert valid. Signed by server's public key.");

			}catch(InvalidKeyException ivky){
				System.out.println("Client cert not valid. Not signed by server's public key.");
			}
			
			//verify server cert is signed by server's public key
			Certificate serverCheck = client.getCertificate("Server - Cert");
			try{
				serverCheck.verify(client.getCertificate("Server - Cert").getPublicKey());				
				System.out.println("Server cert valid. Signed by server's (the CA) public key.");

			}catch(InvalidKeyException ivky){
				System.out.println("Server cert not valid. Not signed by server's (the CA) public key.");
			}
				
				
			
			
			//verify client cert is signed by server's private key
			//client.getCertificate("Client")
			
		}catch(Exception e){
			System.out.println("AKeyGen: could not create key stores");
			e.printStackTrace();
		}
	}
	
	
	private static void createKeyStore(String filepath, String filename, String password, KeyPair kp){
		
		
	}
	
	
	
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
		
		/*
		PrivateKey serverPrk = server.getPrivate();
		byte[] sPrk = serverPrk.getEncoded();
		System.out.println("Server Private Key:\n"+ Base64.encode(sPrk));
		*/
		
		KeyPairGen.createKeyStores(client, server);
		
		/*
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
		*/

		
		

		
		
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

}
