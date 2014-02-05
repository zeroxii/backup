/* 
 * Project Name	:   TN_COMMON
 * File Name	:	MDEncoder.java
 * Date			:	2006. 5. 22. - 오후 9:44:48
 * History		:	2006. 5. 22.
 * Version		:	1.0
 * Author		:   임주섭	
 * Comment      :    
 */
package biz.trustnet.common.cipher;

import java.security.MessageDigest;

import biz.trustnet.common.util.CommonUtil;


public class MDEncoder {
	
	/**
	 * MoveOn Password Encode 방식 SHA1
	 * @param 인코드 되어야 하는 String
	 * @return 결과 String
	 */
	public String encode(String str) throws Exception {
		return encode(str,"SHA1");
	}
	
	/**
	 * 인코드 방식을 지정하여 인코드하여 반환
	 * @param str
	 * @param algorism
	 * @return
	 * @throws Exception
	 */
	public String encode(String str,String algorism)throws Exception {
		MessageDigest sha1 = MessageDigest.getInstance(algorism);

		sha1.update(str.getBytes());
		byte[] b = sha1.digest();

		return CommonUtil.toString(Base64.base64Encode(b));
	}
	
	public String encodeSHA1(String str){
		try{
			str = encode(str);
		}catch(Exception e){
			
		}
		return str;
	}
	
	
	public static void main(String [] args){
		biz.trustnet.common.cipher.MDEncoder md = new biz.trustnet.common.cipher.MDEncoder();
		String algorism = "SHA1";
		try{
			System.out.println(md.encode("ktt7447",algorism));
		}catch(Exception e){System.out.println(e.getMessage());}
		
	}
}


