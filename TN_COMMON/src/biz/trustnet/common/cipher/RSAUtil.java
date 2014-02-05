package biz.trustnet.common.cipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;

import javax.crypto.Cipher;

import biz.trustnet.common.cipher.Base64;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;

public class RSAUtil {
	
	private BigInteger modules 			= null; 
	private BigInteger exponent 		= null; 
	private BigInteger d 				= null;

	public RSAUtil(){
		try{
			modules 	= new RSA().modulus; 
			exponent 	= new RSA().exponent; 
			d 			= new RSA().d;
		}catch(Exception e){
			Log.debug("log.day","RSA ERROR "+e.getMessage(),this);
		}
	}
	
	public String decrypt(String input){
		if(input.trim().equals("")){
			return "";
		}
		String decryptText = "";
		byte[] encryptedFileBytes = Base64.base64Decode(input.getBytes());
		try{
			
			KeyFactory factory = KeyFactory.getInstance("RSA"); 
			Cipher cipher = Cipher.getInstance("RSA");
			RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d); 
			PrivateKey privKey = factory.generatePrivate(privSpec); 
			cipher.init(Cipher.DECRYPT_MODE, privKey); 

			
			//RSA need 128 bytes for output
            int encryptedFileBytesChunkLength = 128;
            int numberOfEncryptedChunks = encryptedFileBytes.length / encryptedFileBytesChunkLength;

            //The limit per chunk is 117 bytes for RSA
            int decryptedFileBytesChunkLength = 100;
            int decryptedFileBytesLength = numberOfEncryptedChunks * encryptedFileBytesChunkLength;
            //It looks like we must create the decrypted file as long as the encrypted since RSA need 128 for output

            //Create the decoded byte array
            byte[] decryptedFileBytes = new byte[decryptedFileBytesLength];

            //Counters
            int decryptedByteIndex = 0;
            int encryptedByteIndex = 0;
            
            for(int i = 0; i < numberOfEncryptedChunks; i++){
            	if( i < numberOfEncryptedChunks -1 ){
            		decryptedByteIndex = decryptedByteIndex + cipher.doFinal(encryptedFileBytes, encryptedByteIndex, encryptedFileBytesChunkLength, decryptedFileBytes, decryptedByteIndex);
            		encryptedByteIndex = encryptedByteIndex + encryptedFileBytesChunkLength;
                }else{
                	decryptedByteIndex = decryptedByteIndex + cipher.doFinal(encryptedFileBytes, encryptedByteIndex, encryptedFileBytes.length - encryptedByteIndex, decryptedFileBytes, decryptedByteIndex);
                }
            }
            
            decryptText = new String(decryptedFileBytes,"UTF8").trim();
            
		}catch(Exception e){
			Log.debug("log.day","DECRYPT ERROR "+e.getMessage(),null);
		}
		return decryptText;
	}
	
	public String encrypt(String input){
		if(input.trim().equals("")){
			return "";
		}
		byte[] enText = null;
		try{
			KeyFactory factory = KeyFactory.getInstance("RSA"); 
			Cipher cipher = Cipher.getInstance("RSA"); 
			RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
			PublicKey pubKey = factory.generatePublic(pubSpec);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey); 
			byte[] pText = input.getBytes("UTF8");
			
			//The limit per chunk is 117 bytes for RSA
            int decryptedFileBytesChunkLength = 100;
            int numberenOfDecryptedChunks = (pText.length-1) / decryptedFileBytesChunkLength + 1;

            //RSA need 128 bytes for output
            int encryptedFileBytesChunkLength = 128;
            int encryptedFileBytesLength = numberenOfDecryptedChunks * encryptedFileBytesChunkLength;

            //Create the encoded byte array
            byte[] encryptedFileBytes = new byte[ encryptedFileBytesLength ];

            //Counters
            int decryptedByteIndex = 0;
            int encryptedByteIndex = 0;

            for(int i = 0; i < numberenOfDecryptedChunks; i++){
            	if(i < numberenOfDecryptedChunks - 1){
            		encryptedByteIndex = encryptedByteIndex + cipher.doFinal(pText, decryptedByteIndex, decryptedFileBytesChunkLength, encryptedFileBytes, encryptedByteIndex);
            		decryptedByteIndex = decryptedByteIndex + decryptedFileBytesChunkLength;
            	}else{
            		cipher.doFinal(pText, decryptedByteIndex, pText.length - decryptedByteIndex, encryptedFileBytes, encryptedByteIndex);
            	}
           }

			enText = Base64.base64Encode(encryptedFileBytes);
		}catch(Exception e){
			Log.debug("log.day","ENCRYPT ERROR "+CommonUtil.getExceptionMessage(e),null);
		}
		return new String(enText);
	}
	
	public String getSystemIn(){
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		return input.trim();
	}
	
	public void fileOut(String data){
		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter("RSA_"+CommonUtil.getCurrentDate("yyyyMMdd")+".dat",true)));
			out.println(CommonUtil.getCurrentDate("yyyyMMdd HH:mm:ss")+" :: "+data);
			out.flush();
			out.close();
		}catch(Exception e){
			
		}
	}
	
	
	public static void main(String[] args){
		biz.trustnet.common.cipher.RSAUtil r = new biz.trustnet.common.cipher.RSAUtil();
		System.out.println("ENCRYPT = 1, DECRYPT = 2 를 선택하여 주시기 바랍니다.");
		String input = r.getSystemIn();
		if(input.equals("1")){
			System.out.println("암호화할 평문을 입력하여 주십시오.");
			String text = r.getSystemIn();
			text = "평문 = ["+text+"] 암호화 =["+r.encrypt(text)+"]";
			System.out.println(text);
			r.fileOut(text);
		}else if(input.equals("2")){
			System.out.println("복호화할 암호문을 입력하여 주십시오.");
			String text = r.getSystemIn();
			text = "암호화 = ["+text+"] 평문 =["+r.decrypt(text)+"]";
			System.out.println(text);
			r.fileOut(text);
		}else{
			System.out.println("1 또는 2 를 입력하여 주십시오.");
		}
		System.exit(1);
	}
}
