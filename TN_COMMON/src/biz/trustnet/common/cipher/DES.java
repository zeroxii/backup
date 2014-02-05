/* 
 * Project Name : TN_COMMON
 * File Name	: DES.java
 * Date			: Jan 13, 2009
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :  
 */

package biz.trustnet.common.cipher;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {

	private Cipher cipher = null;
	private SecretKeySpec secretKeySpec = null;
	private IvParameterSpec ivSpec		= null;
	
	public DES(){
	}
	
	public void setKey(byte[] key) throws Exception
	{
		secretKeySpec = new SecretKeySpec(key, "DES") ;
		cipher = Cipher.getInstance("DES/CBC/PKCS5Padding") ;
	}

	public void setIV(byte[] iv) throws Exception
	{
		ivSpec = new IvParameterSpec(iv) ;
	}

	public byte[] encrypt(byte[] data) throws Exception
	{
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec) ;
		byte[] encrypted = cipher.doFinal(data) ;

		return encrypted ;
	}

	public byte[] decrypt(byte[] data) throws Exception
	{
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec) ;
		byte[] decrypted = cipher.doFinal(data) ;

		return decrypted ;
	}
	
	public static void main(String[] args){
		//DES 대칭키 방식으로 8바이트 로 움직인다.
		String key = "12345678";
		String iv = "12345678";
	}
	
}
