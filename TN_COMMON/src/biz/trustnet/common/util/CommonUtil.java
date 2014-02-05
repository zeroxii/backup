/*
 * Project Name	:  TN_COMMON
 * File Name		:	CommonUtil.java
 * Date				:	2005. 3. 31. - ���� 2:12:59
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:
 * Comment      	:
 */

package biz.trustnet.common.util;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;


public final class CommonUtil {


	public CommonUtil() {}

	/**
		NULL ���� üũ �Ͽ� �ִ� �Լ� ����.
		@param s null���� Ȯ���ϰ� ���� String ��
		@return null�̸� �����ȯ, �ƴϸ� �״�� ��ȯ
	*/
	public static String nullToBlank(String s) {
		return nullToBlank(s, "");
	}

	public static String nullToBlank(String s, String rtn) {
		if (s == null || s.equals("null"))
			s = rtn;
		return s;
	}

	public static String nToB(String s){
		return nullToBlank(s);
	}
	public static String nullBlankCheck(String s, String rtn) {
		if (s == null || s.equals("null") || s.trim().length() == 0)
			s = rtn;
		return s;
	}

	public static boolean isNull(String str) {
		if (str == null)
			return true;
		else
			return false;
	}

	public static boolean isNullOrSpace(String str) {
		if (str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}

	/**
		String�� ���ڸ� int�� ���ڷ� ��ȯ�Ѵ�. ��ȯ�� �ȵǴ� String�� 0�� ��ȯ�Ѵ�.

		@param s int�� �ٲ� String
		@return int������ �ٲ� String
	*/

	public static int parseInt(String s) {
		try {
			if (s == null || s.equals(""))
				return 0;
			else{
				s = s.replaceAll(" ","").trim();
				return Integer.parseInt(s);
			}
		} catch (Exception exception) {
			return 0;
		}
	}

	public static int parseInt(byte[] b){
		return parseInt(toString(b).trim());
	}

	public static String toDecimal(double num,String pattern){
		if(pattern.equals("")){
			pattern="#########";
		}
        DecimalFormat dformat = new DecimalFormat(pattern);
        return dformat.format(num);

	}


	public static long parseLong(String s) {
		if (s.equals("")) {
			return 0;
		} else {
			try {
				s = s.replaceAll(" ","");
				return Long.valueOf(s).longValue();
			} catch (Exception e) {
				return 0;
			}
		}
	}

	public static long parseLong(byte[] b){
		return parseLong(toString(b));
	}

	public static double parseDouble(String s) {
		if (s.equals("")) {
			return 0.0D;
		} else {
			try {
				s = s.replaceAll(" ","");
				return Double.valueOf(s).doubleValue();
			} catch (Exception e) {
				return 0.0D;
			}
		}
	}

	public static double parseDouble(int b){
		return parseDouble(toString(b));
	}

	public static double parseDouble(long b){
		return parseDouble(toString(b));
	}


	public static double parseDouble(byte[] b){
		return parseDouble(toString(b));
	}

	/**
		��¥ �����Ͱ� ���ú��� �������� �̷����� üũ�Ѵ�.
		@param date yyyy-MM-dd ������ ���ڿ� ��¥ ������
		@return ���Ÿ� false, ���ų� �̷���� true
	*/
	public static boolean isAway(String s) {
		return isAway(s.substring(0, 4), s.substring(5, 7), s.substring(8));
	}


	/**
	 * ���糯¥���� day ��ŭ ���ϰ� �� ���ڸ� �����´�.
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getDay(int day,String format){
		Calendar cal = Calendar.getInstance ( );
		cal.setTime(new Date());
		cal.add(Calendar.DATE,day);
		Date date = cal.getTime();
		SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
		return simpledateformat.format(date);
	}

	public static String getYear(int year,String format){
		Calendar cal = Calendar.getInstance ( );
		cal.setTime(new Date());
		cal.add(Calendar.YEAR,year);
		Date date = cal.getTime();
		SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
		return simpledateformat.format(date);
	}




	/**
		��¥ �����Ͱ� ���ú��� �������� �̷����� üũ�Ѵ�.
		@param yyyy ��(Ҵ)
		@param mm ��(��)
		@param dd ��(��)
		@return ���Ÿ� false, ���ų� �̷��� true
	*/

	public static boolean isAway(String yyyy, String mm, String dd) {
		int y = parseInt(yyyy);
		int m = parseInt(mm);
		int d = parseInt(dd);
		return isAway(y, m, d);
	}

	/**
		��¥ �����Ͱ� ���ú��� �������� �̷����� üũ�Ѵ�.
		@param y ��(Ҵ)
		@param m ��(��)
		@param d ��(��)
		@return ���Ÿ� false, ���ų� �̷��� true
	*/
	public static boolean isAway(int y, int m, int d) {
		boolean dateChk = false;

		GregorianCalendar rightNow = new GregorianCalendar();
		GregorianCalendar fromDate = new GregorianCalendar(y, (m - 1), d);

		if (fromDate.before(rightNow)) {
			dateChk = false; //date�� rightNow���� ���ſ��ִ�.
		} else {
			dateChk = true; //date�� rightNow���� �̷����ִ�.
		}

		return dateChk;
	}


	/**
		DB Query�� ' ���ڸ� ` ���ڷ� ġȯ�Ѵ�. ��� DB Query�� �� �޽�带 ������Ѿ� �� ���̴�.

		@param s ' ���ڸ� ` ���ڷ� ġȯ�ϰ� ���� ����
		@return ' ���ڸ� ` ���ڷ� ġȯ�� ����
	*/

	public static String insertDB(String s) {
		if (s == null || s.equals("")) {
			return "";
		} else {
			String s1 = s.replace('\'', '`');
			return s1;
		}
	}

	public static String spaceToZero(String a){
		a = a.trim();
		if(a == null || a.equals("")){
			return "0";
		}else{
			return a;
		}

	}


	public static String getDB(String s) {
		if (s == null || s.equals("")) {
			return "";
		} else {
			String s1 = s.replace('`','\'');
			return s1;
		}
	}

	/**
		���ڿ��� �涧 � Ư���� ���̿��� ©�� ��ȯ�Ѵ�. ¥�� �ڿ� "..."�� ���δ�.
		��, �ѱ��̳� �����̳� ��� 1���ڷ� ����Ѵ�.

		@param s ¥���� ���� ����
		@param i ¥���� ���� ����
		@return �������̷� ¥�� ���ڿ��� "..."�� �ٿ� ��ȯ�Ѵ�.
	*/

	public static String strCut(String s, int i) {

		if(s == null){
			return "";
		}else{
			String s1 = s;
			if (s1.length() > i)
				s1 = s.substring(0, i) + "..";
			return s1;
		}
	}


	public static String cut(String s,int i){
		if(s == null){
			return "";
		}else{
			String s1 = s;
			if(s1.length() > i)
				s1 = s.substring(0,i);
			return s1;
		}
	}

	/**
		���ڿ��� �涧 � Ư���� ���̿��� ©�� ��ȯ�Ѵ�. ¥�� �ڿ� ".."�� ���δ�.
		��, �ѱ��� 2����Ʈ, ������ 1����Ʈ�� ����Ͽ� �ݿø��ؼ� ¥����.

		@param s ¥���� ���� ����
		@param i ¥���� ���� ����
		@return �������̷� ¥�� ���ڿ��� "..."�� �ٿ� ��ȯ�Ѵ�.
	*/
	public static String strCutKor(String s, int i) {
		if (i < 4)
			return s;
		int j = 0;
		StringBuffer stringbuffer = new StringBuffer();
		for (int k = 0; k < s.length(); k++) {
			char c = s.charAt(k);
			if (c < '\uAC00' || '\uD7A3' < c)
				j++;
			else
				j += 2;
			stringbuffer.append(c);
			if (j <= i - 2)
				continue;
			stringbuffer.append("..");
			break;
		}

		return stringbuffer.toString();
	}


	/**
		��ȭ����ó�� ���� 3�ڸ����� ,(�޸�)�� ��´�. double(int)�� ���ڸ� �޴´�.

		@param money double(int)�� ��ȭ����
		@return 3�ڸ����� ,(�޸�)�� ���� ����
	*/
	public static String moneyFormat(double d) {
		NumberFormat numberformat = NumberFormat.getNumberInstance();
		String s = numberformat.format(d);
		return s;
	}

	/**
		��ȭ����ó�� ���� 3�ڸ��̴� ,(�޸�)�� ��´�. String�� ���ڸ� �޴´�.

		@param money String�� ��ȭ����
		@return 3�ڸ����� ,(�޸�)�� ���� ����
	*/
	public static String moneyFormat(String s) {
		NumberFormat numberformat = NumberFormat.getNumberInstance();
		String s1;
		try {
			Number number = numberformat.parse(s);
			s1 = numberformat.format(number);
		} catch (ParseException parseexception) {
			s1 = "0";
		}
		return s1;
	}

	public static String makeMoneyType(int intMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(intMoney), delimeter));
	}

	public static String makeMoneyType(long lngMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(lngMoney), delimeter));
	}

	public static String makeMoneyType(double dbleMoney, String delimeter) {
		return (makeMoneyType(CommonUtil.toString(dbleMoney), delimeter));
	}

	public static String makeMoneyType(String strMoney, String delimeter) {
		if (strMoney == null || strMoney.equals("") || delimeter == null || delimeter.equals(""))
			return "";


		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();

		dfs.setGroupingSeparator(delimeter.charAt(0));
		df.setGroupingSize(3);
		df.setDecimalFormatSymbols(dfs);

		return (df.format(Double.parseDouble(strMoney))).toString();
	}

	/**
		����ڿ�(strTarget)���� Ư�����ڿ�(strSearch)�� ã�� �������ڿ�(strReplace)��	������ ���ڿ��� ��ȯ�Ѵ�.

		@param strTarget ����ڿ�
		@param strSearch �������� Ư�����ڿ�
		@param strReplace ���� ��Ű�� �������ڿ�
		@return ����Ϸ�� ���ڿ�
	*/
	public static String replace(String strTarget, String strSearch, String strReplace) {
		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch.length();
				strBuf.append(strCheck.substring(0, begin));
				strBuf.append(strReplace);
				strCheck = strCheck.substring(end);
			}
		}
		return new String(strBuf);
	}

	/**
		����ڿ�(strTarget)�� ������ ��ġ(loc)�� �������ڿ�(strInsert)�� �߰��� ���ڿ��� ��ȯ�Ѵ�.

		@param strTarget ����ڿ�
		@param loc �������ڿ��� �߰��� ��ġ�μ� ����ڿ��� ù���� ��ġ�� 0���� ������ ��� ��ġ. loc�� 0 ���� ���� ���� ���� ����ڿ��� ���ڸ��� 0���� ������ ����� ��ġ. �Ǿհ� �ǵڴ� ���ڿ� + �������� ���డ�������� �������� �ʴ´�.
		@param strInsert �߰��� ���ڿ�
		@return �߰��Ϸ�� ���ڿ�
	*/
	public static String insert(String strTarget, int loc, String strInsert) {
		StringBuffer strBuf = new StringBuffer();
		int lengthSize = strTarget.length();
		if (loc >= 0) {
			if (lengthSize < loc) {
				loc = lengthSize;
			}
			strBuf.append(strTarget.substring(0, loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring(loc + strInsert.length()));
		} else {
			if (lengthSize < Math.abs(loc)) {
				loc = lengthSize * (-1);
			}
			strBuf.append(strTarget.substring(0, (lengthSize - 1) + loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring((lengthSize - 1) + loc + strInsert.length()));
		}
		return new String(strBuf);
	}

	/**
		����ڿ�(strTarget)���� �������ڿ�(strDelete)�� ������ ���ڿ��� ��ȯ�Ѵ�.

		@param strTarget ����ڿ�
		@param strDelete ������ ���ڿ�
		@return �����Ϸ�� ���ڿ�
	*/
	public static String delete(String strTarget, String strDelete) {
		return replace(strTarget, strDelete, "");
	}

	/**
		����ڿ�(strTarget)���� ���й��ڿ�(strDelim)�� �������� ���ڿ��� �и��Ͽ�	�� �и��� ���ڿ��� �迭�� �Ҵ��Ͽ� ��ȯ�Ѵ�.

		@param strTarget �и� ��� ���ڿ�
		@param strDelim ���н�ų ���ڿ��μ� ��� ���ڿ����� ���Ե��� �ʴ´�.
		@param bContainNull ���еǾ��� ���ڿ��� ���鹮�ڿ��� ���Կ���. true : ����, false : �������� ����.
		@return �и��� ���ڿ��� ������� �迭�� �ݳ��Ͽ� ��ȯ�Ѵ�.
	*/
	public static String[] split(String strTarget, String strDelim, boolean bContainNull) {
		// StringTokenizer�� �����ڰ� �������� ��ø�Ǿ� ���� ��� ���� ���ڿ��� ��ȯ���� ����.
		// ���� �Ʒ��� ���� �ۼ���.

		if(strTarget.endsWith(strDelim)){
			strTarget = strTarget.substring(0,strTarget.length()-strDelim.length());
		}
		int index = 0;


		String[] resultStrArray = new String[search(strTarget, strDelim) + 1];
		String strCheck = new String(strTarget);
		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strDelim);
			if (begin == -1) {
				resultStrArray[index] = strCheck;
				break;
			} else {
				int end = begin + strDelim.length();
				if (bContainNull) {
					resultStrArray[index++] = strCheck.substring(0, begin);
				}
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					resultStrArray[index] = strCheck;
					break;
				}
			}
		}
		return resultStrArray;
	}


	public static String[] split(String strTarget,String strDelim,boolean bContainNull,int array){
			if(split(strTarget,strDelim,bContainNull) != null)
				return split(strTarget,strDelim,bContainNull);
			else
				return adjustArray(new String[array]);

	}

	/**
	����ڿ�(strTarget)���� �������ڿ�(strSearch)�� �˻��� Ƚ����, �������ڿ��� ������ 0 �� ��ȯ�Ѵ�.

	@param strTarget ����ڿ�
	@param strSearch �˻��� ���ڿ�
	@return �������ڿ��� �˻��Ǿ����� �˻��� Ƚ����, �˻����� �ʾ����� 0 �� ��ȯ�Ѵ�.
 */
	public static int search(String strTarget, String strSearch) {
		int result = 0;
		String strCheck = new String(strTarget);
		for (int i = 0; i < strTarget.length();) {
			int loc = strCheck.indexOf(strSearch);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}
		return result;
	}

	public static boolean isNumeric(byte[] b){
		return isStringIsNumeric(toString(b));
	}

	/**
		����ڿ����� ���� ���� ���ڸ� ǥ���ϸ� true, �ƴϸ� false�� ��ȯ�Ѵ�.

		@param str ����ڿ�
		@return ����ڿ��� ���� ���� �����̸� true, �ƴϸ� false
	*/
	public static boolean isStringIsNumeric(String str) {
		try {
			NumberFormat nf = NumberFormat.getInstance();
			Number number = nf.parse(str);
			return true;
		} catch (ParseException parseexception) {
			return false;
		}
	}

	/**
		����ڿ����� ���� ���ھտ� ���ϴ� size�� �ڸ����� �°� '0'�� ���δ�. ��) 000000000 (�Ͼ��ڸ���)�� 345�� �־��� ��� '000000345'��ȯ

		@param str ����ڿ�
		@param size ���ϴ� �ڸ���
		@return ����ڿ��� �ڸ��� ��ŭ�� '0'�� ���� ���ڿ�
	*/
	public static String zerofill(String str, int size) {
		if(str == null){
			str="";
		}
		if(str.length() > size){
			return str.substring(0,size);
		}
		try {
			NumberFormat nf = NumberFormat.getInstance();
			return zerofill(nf.parse(str), size);
		} catch (Exception e) {
			return zerofill(0,size);
		}

	}

	/**
		int�� ���ھտ� ���ϴ� size�� �ڸ����� �°� '0'�� ���δ�. ��) 000000000 (�Ͼ��ڸ���)�� 345�� �־��� ��� '000000345'��ȯ

		@param str ��� int�� ����
		@param size ���ϴ� �ڸ���
		@return ����ڿ��� �ڸ��� ��ŭ�� '0'�� ���� ���ڿ�
	*/
	public static String zerofill(int num, int size) {
		return zerofill(new Integer(num), size);
	}

	/**
		long�� ���ھտ� ���ϴ� size�� �ڸ����� �°� '0'�� ���δ�. ��) 000000000 (�Ͼ��ڸ���)�� 345�� �־��� ��� '000000345'��ȯ

		@param str ��� long�� ����
		@param size ���ϴ� �ڸ���
		@return ����ڿ��� �ڸ��� ��ŭ�� '0'�� ���� ���ڿ�
	*/
	public static String zerofill(long num, int size) {
		return zerofill(new Long(num), size);
	}

	/**
		Number�� ���ھտ� ���ϴ� size�� �ڸ����� �°� '0'�� ���δ�. ��) 000000000 (�Ͼ��ڸ���)�� 345�� �־��� ��� '000000345'��ȯ

		@param num ��� Number�� ����
		@param size ���ϴ� �ڸ���
		@return ����ڿ��� �ڸ��� ��ŭ�� '0'�� ���� ���ڿ�
	*/
	public static String zerofill(Number num, int size) {
		String zero = "";
		for (int i = 0; i < size; i++) {
			zero += "0";
		}
		DecimalFormat df = new DecimalFormat(zero);
		return df.format(num);
	}

	public static String zerofillDouble(double d,int size){
		return zerofillDouble(toString(d),size);
	}

	public static String zerofill(double d,int size){
		return zerofillDouble(toString(d),size);
	}

	public static String zerofillDouble(String v,int size){
		int zero = size - v.length();
		if(zero > 0){
			String prefix = "";
			for(int i=0 ; i < zero ; i++){
				prefix += "0";
			}
			return prefix+v;
		}else{
			return v.substring(0,size);
		}
	}


	/**
	 * �����̽� ���ڿ��� �����Ѵ�.
	 * @param len
	 * @return
	 */
	public static String setFiller(int len) {
		String filler = "";
		for (int i = 0; i < len; i++) {
			filler += " ";
		}
		return filler;
	}

	public static String setFiller(String oldStr,int len){
		if(oldStr == null){
			oldStr = "";
		}

		int oldLen = oldStr.length();

		if(oldLen >= len)
			return oldStr.substring(0,len);
		else
			return oldStr + setFiller(len - oldLen);
	}

	public static int length(byte[] b){
		if(b == null)
			return 0;
		else
			return b.length;
	}

	public static int length(String s){
		if(s == null)
			return 0;
		else
			return s.length();
	}




	/**
		���ڿ��� ����Ʈ�� ����Ͽ� len��ŭ Filler�� �߰��Ͽ� ��ȯ�Ѵ�..

		@param oldStr ���� ���ڿ�
		@param len ���ڿ��� ����
		@return byte���� len�� ���ڿ��� ��ȯ�Ѵ�.
	*/
	public static String byteFiller(String oldStr,int len) {
		byte[] b 	= oldStr.getBytes();
		int oldLen = b.length;

		if(oldLen >= len)
			return byteTrim(b,len);
		else {
			byte[] nb = new byte[len];
			int i =0 ;
			for( ; i< oldLen ; i++) {
				nb[i] = b[i];
			}
			for( ; i < len ; i++) {
				nb[i] = ' ';
			}

			return new String(nb);
		}
	}

	public static String byteFillerCharSet(String oldStr,int len,String charSet) {
		byte[] b 	= null;
		try{
			b = oldStr.getBytes(charSet);
		}catch(Exception e){
			b = oldStr.getBytes();
		}
		int oldLen = b.length;

		if(oldLen >= len)
			return byteTrim(b,len);
		else {
			byte[] nb = new byte[len];
			int i =0 ;
			for( ; i< oldLen ; i++) {
				nb[i] = b[i];
			}
			for( ; i < len ; i++) {
				nb[i] = ' ';
			}

			return new String(nb);
		}
	}

	/**
		����Ʈ �迭���� len�� ���� ��ŭ�� ��ȯ�Ѵ�.

		@param b ¥���� ���� �迭
		@param len ¥���� ���� ����
		@return byte���� len�� ���ڿ��� ��ȯ�Ѵ�.
	*/
	public static String byteTrim(byte[] b,int len) {
		byte[] nb = null;
		if(b == null || b.length == 0){
			nb = new byte[len];
			for(int i=0 ; i < len ;i++){
				b[i] = ' ';
			}

		}else{
			int oldLen = b.length;
			if(len <= oldLen){
				nb = new byte[len];
				for(int i =0 ; i<len ; i++) {
					nb[i] = b[i];
				}
			}else{
				byte[] l = new byte[len - oldLen];
				for(int i=0 ; i < (len - oldLen);i++){
					l[i] = ' ';
				}
				nb = byteAppend(b,l);
			}
		}

		return new String(nb);
	}

	public static byte[] byteTrim(String str,int len) {
		byte[] b = str.getBytes();

		byte[] nb = byteTrim(b,len).getBytes();
		if(nb.length == len){
			return nb;
		}else{
			byte[] k = new byte[len];
			for(int i =0 ; i< len ; i++){
				k[i] = nb[i];
			}
			return k;
		}

	}

	public static String toString(Object object){

		if(object == null){
			return "";
		}else{
			if(object instanceof Timestamp){
				return timestampToString((Timestamp)object);
			}else if(object instanceof Integer){
				Integer i = (Integer)object;
				return toString(i.intValue());
			}else if( object instanceof Long ){
				Long l = (Long)object;
				return toString(l.longValue());
			}else if( object instanceof Double){
				Double d = (Double)object;
				return toString(d.doubleValue());
			}else if( object instanceof byte[]){
				byte[] b = (byte[])object;
				return toString(b);
			}else{
				return (String)object;
			}
		}

	}

	/**
	 * ����Ʈ �迭�� ��Ʈ������ ��ȯ�Ѵ�.
	 * @param b
	 * @return
	 */
	public static String toString(byte[] b){
		return toString(b,0,length(b));
	}

	public static String toString(byte[] b,String charSet){
		String s = "";
		try{
			s= new String(b,charSet);
		}catch(Exception e){
			s= toString(b);
		}
		return s;
	}

	public static String toString(String s,String charSet){
		try{
			byte[] b = s.getBytes(charSet);
			s= new String(b,charSet);
		}catch(Exception e){
			return "";
		}
		return s;
	}

	public static String toString(byte[] b,int init){
		return toString(b,init,b.length-init);
	}


	/**
	 * ����Ʈ �迭�� ��Ʈ������ ��ȯ�Ѵ�.
	 * @param b
	 * @param start
	 * @param end
	 * @return
	 */
	public static String toString(byte[] b , int start , int end){

		if(length(b) < start || length(b)==0 )
			return "";
		else if(length(b) < start+end)
			return new String(b,start,length(b));
		else
			return new String(b,start,end);
	}

	public static String toStringCharSet(byte[] b , int start , int end,String charSet){

		try{
			b = new String(b,charSet).getBytes();
		}catch(Exception e){
		}

		if(length(b) < start || length(b)==0 )
			return "";
		else if(length(b) < start+end)
			return toString(b,start,length(b));
		else
			return toString(b,start,end);
	}

	public static String toString(int i){
		return Integer.toString(i);
	}

	public static String toString(long i){
		return Long.toString(i);
	}

	public static String toString(double i){
		return Double.toString(i);
	}

	public static long parseLong(double i){
		Double d = new Double(i);
		return d.longValue();
	}

	public static String trimDot(String str){
		if(str == null || str.equals("")){
			return "";
		}
		if(str.indexOf(".") > 0 ){
			str = str.substring(0,str.indexOf("."));
		}

		return str;
	}

	/**
	 * ����Ʈ �迭�� �����Ǿ��� �ִ� �� ������ ���Ͽ� �� ����Ʈ �迭 ���·� ������.
	 * @param b
	 * @param appendByte
	 * @return
	 */
	public static byte[] byteAppend(byte[] b,byte[] appendByte){

		if(b == null && appendByte == null)
			return new byte[0];

		if(b == null)
			return appendByte;

		if(appendByte == null)
			return b;

		if(b == null && appendByte == null)
			return new byte[0];


		int len = b.length + appendByte.length;

		byte[]  nb = new byte[len];

		int i = 0;
		for(;i<b.length;i++){
			nb[i] = b[i];
		}
		for(int j=0 ; j < appendByte.length ; j++){
			nb[i] = appendByte[j];
			i++;
		}
		return nb;
	}

	public static void arrayCopy(Object destBuffer,int destPos,Object srcBuffer,int srcLen){
		System.arraycopy(srcBuffer,0,destBuffer,destPos,srcLen);
	}

	/**
	 * target �� index �� byte[]�� �Է�.
	 * @param target
	 * @param idx
	 * @param argname
	 */
	public static void setBytes(byte[] target, int idx, byte[] argname) {
		int len = argname.length;
		for (int i = idx, j = 0; i < idx + len; i++, j++) {
			target[i] = argname[j];
		}
	}

	public static byte[] leftSpaceFill(String input, int len) { // �������� �����̽� ü��
		String tmpstr = "";
		int temp = input.getBytes().length;
		if (temp == len) {
			return input.getBytes();
		}

		for (int i = 0; i < len - input.length(); i++) {
			tmpstr += " ";
		}
		input = tmpstr + input;
		return input.getBytes();
	}

	public static byte[] leftZeroFill(String input, int len) { // �������� �����̽� ü��
		String tmpstr = "";
		int temp = input.getBytes().length;
		if (temp == len) {
			return input.getBytes();
		}

		for (int i = 0; i < len - input.length(); i++) {
			tmpstr += "0";
		}
		input = tmpstr + input;
		return input.getBytes();
	}

	public static byte[] rightSpaceFill(String input, int len) { // �������� �����̽� ü��
		String tmpstr = "";
		int temp = input.getBytes().length;
		if (temp == len) {
			return input.getBytes();
		}

		for (int i = 0; i < len - input.length(); i++) {
			tmpstr += " ";
		}
		input = input+tmpstr;
		return input.getBytes();
	}

	public static byte[] rightZeroFill(String input, int len) { // �������� �����̽� ü��
		String tmpstr = "";
		int temp = input.getBytes().length;
		if (temp == len) {
			return input.getBytes();
		}

		for (int i = 0; i < len - input.length(); i++) {
			tmpstr += "0";
		}
		input = input + tmpstr;
		return input.getBytes();
	}

	public static String K2E(String s) {
		String s1 = null;
		try {
			s1 = new String(s.getBytes("KSC5601"), "8859_1");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {}
		return s1;
	}

	public static String E2K(String s) {
		String s1 = null;
		try {
			s1 = new String(s.getBytes("8859_1"), "KSC5601");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {} catch (NullPointerException nullpointerexception) {}
		return s1;
	}

	public static String E2EUC(String s) {
		String s1 = null;
		try {
			s1 = new String(s.getBytes("8859_1"), "EUC-KR");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {} catch (NullPointerException nullpointerexception) {}
		return s1;
	}

	public static String EUC2E(String s) {
		String s1 = null;
		try {
			s1 = new String(s.getBytes("EUC-KR"), "8859_1");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {} catch (NullPointerException nullpointerexception) {}
		return s1;
	}

	public static String K2EUC(String s) {
		String s1 = null;
		try {
			s1 = new String(s.getBytes("KSC5601"), "EUC-KR");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {}
		return s1;
	}

	public static String paramEnc(String s, String charset) {
		if(charset != null){
			charset = charset.toUpperCase();
			if(charset.equals("EUC-KR") || charset.equals("EUC_KR"))
				return s;
			else
				return E2EUC(s);
		} else {
			return K2EUC(E2K(s));
		}

	}

	public static boolean containsCharsOnly(String input, String chars) {
		for (int inx = 0; inx < input.length(); inx++) {
			if (chars.indexOf(input.charAt(inx)) == -1)
				return false;
		}
		return true;
	}

	public static boolean containsChars(String input, String chars) {
		for (int inx = 0; inx < input.length(); inx++) {
			if (chars.indexOf(input.charAt(inx)) != -1)
				return true;
		}
		return false;
	}

	public static boolean containsAlphabetOnly(String input) {
		String chars = "abcdefghijklmnopqrstuwxyz";
		return containsCharsOnly(input, chars);
	}

	public static boolean containsNumericOnly(String input) {
		String chars = "1234567890";
		return containsCharsOnly(input, chars);
	}
	public static boolean containsAlphaNum(String input) {
		String chars = "abcdefghijklmnopqrstuwxyz1234567890";
		return containsChars(input, chars);
	}
	public static boolean containsAlphaNumExtraChar(String input) {
		String chars = "abcdefghijklmnopqrstuvwxyz1234567890\\~`!@#$%^&*()-_=+|[{]};:'\",<.>/?";
		return containsChars(input, chars);
	}
	public static boolean containsAlphabet(String input) {
		String chars = "abcdefghijklmnopqrstuwxyz";
		return containsChars(input, chars);
	}

	public static boolean conatinsNumeric(String input) {
		String chars = "1234567890";
		return containsChars(input, chars);
	}

	public static boolean containsExtraChar(String input) {
		String chars = "\\~`!@#$%^&*()-_=+|[{]};:'\",<.>/?";
		return containsChars(input, chars);
	}

	public static boolean checkCitizenNumber(String input) {
		int IDtot = 0;
		String IDAdd = "234567892345";

		int i01 = 0;
		int i02 = 0;
		for (int i = 0; i < 12; i++) {
			i01 = parseInt(input.substring(i, i + 1));
			i02 = parseInt(IDAdd.substring(i, i + 1));
			IDtot = IDtot + (i01 * i02);
		}

		IDtot = 11 - (IDtot % 11);

		if (IDtot == 10) {
			IDtot = 0;
		} else if (IDtot == 11) {
			IDtot = 1;
		}

		//log.debug("Last IDtot: " + IDtot);
		int lastInt = parseInt(input.substring(12, 13));
		//log.debug("lastInt: " + lastInt);

		if (lastInt != IDtot)
			return false;
		else
			return true;
	}


	/**
		"yyyy-MM-dd HH:mm:SS"���� ��¥�� ��ȯ�Ѵ�.
		@return yyyy-MM-dd HH:mm:SS ���� ��¥
	*/

	public static String getCurrentDate() {
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
		���ڷ� ������ ������ ��¥�� ��ȯ�Ѵ�.
		@return ���ڷ� ������ ������ ��¥
	*/

	public static String getCurrentDate(String s) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
		return simpledateformat.format(new Date());
	}



	/**
	* ��¥�� �޾� Timestamp �� ��ȯ�Ѵ�
	* @param  dateString : yyyyMMdd, timeString : HHmmss
	* @return �迭��[2] : ���� OK, ���� :FAIL
	*/
	public static Timestamp stringToTimestamp(String dateString) {
		String totalString = dateString;
		String format = "yyyyMMddHHmmss";
		if(totalString.length() == 19  ){
			format = "yyyy-MM-dd HH:mm:ss";
		}else if(totalString.length() == 17  ){
			format = "yyyy-MM-dd HHmmss";
		}else if(totalString.length() == 14  ){
			format = "yyyyMMddHHmmss";
		}else if(totalString.length() == 12  ){
			format = "yyyyMMddHHmm";
		}else if(totalString.length() == 8){
			format = "yyyyMMdd";
		}else if(totalString.length() == 10){
			format = "yyyy-MM-dd";
		}else if(totalString.length() == 6){
			format = "yyyyMM";
		}else if(totalString.length() == 4){
			format = "yyMM";
		}

		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(totalString);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		Timestamp ts = new Timestamp(date.getTime());

		return ts;

	}



	/**
	* ���� �ð��� Timestamp�� ��ȯ�Ѵ�.
	* @param
	* @return Timestamp Object
	*/
	public static Timestamp getCurrentTimestamp() {
		Date currDt = new Date(System.currentTimeMillis());
		Timestamp timeDt = new Timestamp(currDt.getTime());
		return timeDt;
	}

	/**
	* Timestamp�� �޾� format ������ ��Ʈ������ ��ȯ�Ѵ�
	* @param  Timestamp
	* @return String date format
	*/
	public static String timestampToString(Timestamp timestamp) {
		if (timestamp == null) {
			timestamp = new Timestamp(new java.util.Date().getTime());
		}
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(new java.util.Date(timestamp.getTime()));
	}

	/**
	* Timestamp�� �޾� format ������ ��Ʈ������ ��ȯ�Ѵ�
	* @param  Timestamp
	* @return String date format
	*/
	public static String convertTimestampToString(Timestamp timestamp,String format) {
		if (timestamp == null) {
			timestamp = new Timestamp(new java.util.Date().getTime());
		}
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);

		return df.format(new java.util.Date(timestamp.getTime()));
	}

	/**
	 * Timestamp �� �޾� �����ͺ��̽��� �ð������� ��ȭ��Ű�� ����
	 * @param timestamp
	 * @return
	 */
	public static String convertTimestampToDB(Timestamp timestamp){
		return "TO_DATE('" +convertTimestampToString(timestamp,"MM/dd/yyyy hh:mm:ss")+"' ,'mm/dd/yyyy hh24:mi:ss')" ;
	}

	/**
	* String exmple = "MEMID=kkk&key=value" �� Map���� ��ȯ�Ͽ� �ش�.
	* ginaida
	* @param  String
	* @return Map
	*/
	public static Map stringToMap(String str) {
		Map map = new HashMap();
		StringTokenizer st = new StringTokenizer(str, "&");
		int idx = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			idx = token.indexOf("=");
			if (idx != token.length()) {
				map.put(token.substring(0, idx), token.substring(idx + 1, token.length()));
			} else {
				map.put(token.substring(0, idx), "");
			}
		}
		return map;
	}

	/**
	 * Map �� �ִ� ���� String���� ��ȯ�Ͽ� �ش�.
	 * ginaida
	 * @param argMap
	 * @return Map --> String
	 * @see #mapToString()
	 */
	public static String mapToString(Map argMap) {
		if (argMap.isEmpty()) {
			return "";
		}
		StringBuffer map = new StringBuffer();
		Iterator iter = argMap.keySet().iterator();
		String name = "";
		String value = "";
		while (iter.hasNext()) {
			name = (String) iter.next();
			value = (String) argMap.get(name);
			map.append("[ key = " + name + " value = " + value + "]");
		}
		return map.toString();
	}

	/**
	 * String Value �� FastHashMap ���� ��ȯ
	 * 1�� ������ &  2�� ������ =
	 * @param str
	 * @return
	 */
	public static FastHashMap stringToFMap(String str){
		FastHashMap fMap = new FastHashMap();
		if(str.indexOf("=") < 0 ) {
			return fMap;
		}
		StringTokenizer st = new StringTokenizer(str, "&");
		int idx = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			idx = token.indexOf("=");
			if (idx != token.length()) {
				fMap.put(token.substring(0, idx), token.substring(idx + 1, token.length()));
			} else {
				fMap.put(token.substring(0, idx), "");
			}
		}
		return fMap;
	}

	/**
	 * FastHashMapMap �� �ִ� ���� String���� ��ȯ�Ͽ� �ش�.
	 *
	 * @param argMap
	 * @return Map --> String
	 * @see #mapToString()
	 */
	public static String mapToString(FastHashMap argMap) {
		if (argMap.isEmpty()) {
			return "";
		}
		StringBuffer map = new StringBuffer();
		Iterator iter = argMap.keySet().iterator();
		String name = "";
		String value = "";
		while (iter.hasNext()) {
			name = (String) iter.next();
			value = (String) argMap.get(name);
			map.append(name + "=" + value +"&");
		}
		return map.substring(0,map.length()-1);
	}


	/**
	 * ���ڿ��� ���ڸ��� ���� �� ����ϴ� �޼��� 	��) 1234567890 ==> 12345*****
	 * 2004/1/7, zoo �߰�
	 * @param target ������ ���ڿ�
	 * @param delim ���� �� ����� ��ȣ
	 * @param count ��Ÿ�� ������ ����
	 * @return ������ ���ڿ��� ��ȯ
	 */

	public static String getStrToHide(String target, String delim, int count) {
		String result = null;
		result = target.substring(0, count);
		for (int i = 0; i < target.length() - count; i++) {
			result = result + delim;
		}
		return result;
	}


	/**
	 * �ٳ� ��� ���䰪�� ���� �Ľ� ó�� �κ�
	 * output�� �ٳ����� ���� ������,value�� ��û ���ڰ�.
	 * @param output
	 * @param Value
	 * @return name�� ���� Value
	 * @see #retValue()
	 */
	public static String retValue(String output, String name) {
		int cmdIndex = 0;
		int valIndex = 0;
		int endIndex = 0;
		String temp = new String();
		cmdIndex = output.indexOf(name);
		if (cmdIndex != -1) {
			valIndex = output.indexOf('=', cmdIndex);
			endIndex = output.indexOf('\n', valIndex);
		}
		if (cmdIndex != -1) {
			if (valIndex != -1) {
				if (endIndex != -1)
					temp = output.substring(valIndex + 1, endIndex);
				else
					temp = output.substring(valIndex + 1);
				temp = temp.trim();
				return temp;
			} else {
				return "";
			}
		}
		return "";
	}

	/**
	 * String[] �� null �̳� space�� �� ���� ��� �̸� null ������ �߻����� �ʵ��� ����� �ִ� ��ƿ.
	 * @param oldArray
	 * @return
	 */
	public static String[] adjustArray(String[] oldArray){
		String[] newArray = new String[oldArray.length];
		for(int i = 0 ; i < oldArray.length ; i++){
			if(isNullOrSpace(oldArray[i])){
				newArray[i] = " ";
			}else{
				newArray[i] = oldArray[i];
			}
		}
		return newArray;
	}

	public static String[] adjustArray(String[] oldArray,int len){
		String[] newArray = null;
		if(oldArray == null){
			newArray = new String[len];
			return adjustArray(newArray);
		}else{
			oldArray = adjustArray(oldArray);
			if(oldArray.length < len){
				newArray = new String[len];
				for(int i = 0 ; i < len ; i++){
					if(i < oldArray.length)
						newArray[i] = oldArray[i];
					else
						newArray[i] = " ";
				}
				return newArray;
			}
			return oldArray;
		}
	}


	public static void writeShort(int datum,byte[] dst,int offset){
		dst[offset + 2] = (byte) (datum >> 8);
		dst[offset + 3] = (byte) datum;
	}

	/**
	 *
	 * @param datum
	 * @param dst
	 * @param offset
	 */
	public static void writeInt (int datum, byte[] dst, int offset) {
	  dst[offset] = (byte) (datum >> 24);
	  dst[offset + 1] = (byte) (datum >> 16);
	  dst[offset + 2] = (byte) (datum >> 8);
	  dst[offset + 3] = (byte) datum;
	}

	/**
	 *
	 * @param datum
	 * @param dst
	 * @param offset
	 */
	public static void writeLong (long datum, byte[] dst, int offset) {
	  writeInt ((int) (datum >> 32), dst, offset);
	  writeInt ((int) datum, dst, offset + 4);
	}


	public static short readShort(byte[] bytes, int offset){
		return	(short)(((bytes[offset+0] & 0xFF) << 8) +
						((bytes[offset+1] & 0xFF) << 0));
	}
	/**
	 *
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static int readInt (byte[] bytes, int offset) {
	  return (bytes[offset] << 24) | ((bytes[offset + 1] & 0xff) << 16) |
		((bytes[offset + 2] & 0xff) << 8) | (bytes[offset + 3] & 0xff);
	}

	/**
	 *
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static long readLong (byte[] bytes, int offset) {
	  return ((long) readInt (bytes, offset) << 32) |
		((long) readInt (bytes, offset + 4) & 0xffffffffL);
	}



	/**
	 * ������ ���� ��ȣ ����.
	 * String ���·� ����ϱ� ���ؼ��� CommonUtil.zerofill(����,����) �� ����ϸ� �ȴ�.
	 * @return
	 */
	public static int generateRandomKey() {
		java.util.Random rand = new java.util.Random(System.currentTimeMillis());
		int num = rand.nextInt();
		if(num < 0) {
			num = -num;
		}
		return num;
	}

	public static String getCardData(String trackData){
	    if(trackData.length() > 16){
	        return trackData.substring(0,16);
	    }else{
	        return trackData;
	    }

	}

	public static String[] halveDivision(String str,int ren){
		String[] div = halveDivision(str);
		if(div.length ==0){
			return CommonUtil.adjustArray(new String[ren],ren);
		}else
			return div;
	}

	public static String[] halveDivision(String str){

		String divisionWord = "||";

		String[] div = new String[0];

		//�������� �����ڰ� ���� ��ָ� �߻��� �� �����Ƿ� �����Ѵ�.
		if(str.endsWith(divisionWord))
			str = str.substring(0,str.length()-2);

		if(str.length() == 0 || str == null)
			return div;
		else {
			div = CommonUtil.split(str, divisionWord , true);
			return div;
		}
	}

	public static String[] subDivision(String str,int ren){
		String[] div = subDivision(str);
		if(div.length ==0){
			return CommonUtil.adjustArray(new String[ren],ren);
		}else
			return CommonUtil.adjustArray(div,ren);
	}

	public static String[] subDivision(String str){
		String divisionWord = "::";

		String[] div = new String[0];

		//�������� �����ڰ� ���� ��ָ� �߻��� �� �����Ƿ� �����Ѵ�.
		if(str.endsWith(divisionWord))
			str = str.substring(0,str.length()-2);

		if(str.length() == 0 || str == null)
			return div;
		else {
			div = CommonUtil.split(str, divisionWord , true);
			return div;
		}
	}

	public static String[] commaDivision(String str,int ren){
		String[] div = commaDivision(str);
		if(div.length ==0){
			return CommonUtil.adjustArray(new String[ren],ren);
		}else
			return CommonUtil.adjustArray(div,ren);
	}

	public static String[] commaDivision(String str){
		String divisionWord = ",";

		String[] div = new String[0];

		//�������� �����ڰ� ���� ��ָ� �߻��� �� �����Ƿ� �����Ѵ�.
		if(str.endsWith(divisionWord))
			str = str.substring(0,str.length()-1);

		if(str.length() == 0 || str == null)
			return div;
		else {
			div = CommonUtil.split(str, divisionWord , true);
			return div;
		}
	}

	/**
	 * 	��������� min ������ �������� ����ȣ�� ����..
	 * 	ex) 01102345678 -> 0112345678
	 * @param min
	 * @return
	 */
	public static String getHeaderToPhone(String min){
		String phone = min;

		if(min.length() > 10){
			if(min.substring(3, 4).equals("0")){
				phone = min.substring(0, 3) + min.substring(4, min.length());
			}
		}

		return phone;
	}


	public static String edgeFiller(String dest,byte start,byte end){
		byte[] oldByte = dest.getBytes();
		byte[] newByte = new byte[oldByte.length+2];
		newByte[0] = start;
		arrayCopy(newByte,1,oldByte,oldByte.length);
		newByte[oldByte.length+1] = end;

		return toString(newByte);
	}



	public static ArrayList getArrayList(ArrayList list,int start,int count){
		if(list.size() == 0){
			return list;
		}

		ArrayList nList = new ArrayList();

		for(int i=start ; i<(start+count) ; i++){
			if(i < list.size()){
				nList.add(list.get(i));
			}
		}

		return nList;
	}

	public static String getExceptionMessage(Exception e){
		StringBuffer sb = new StringBuffer();
		if(e instanceof SQLException){
			SQLException sql = (SQLException)e;
			sb.append("SQLState: " + sql.getSQLState ());
			sb.append("Message:  " + sql.getMessage ());
			sb.append("Vendor:   " + sql.getErrorCode ());
			sb.append(sql.getNextException ());  //Adds an SQLException object to the end of the chain.
		}
		sb.append("Message ="+e.getMessage()+"\n");
		StackTraceElement[] trace = e.getStackTrace();
		for(int i=0;i<trace.length;i++){
			sb.append(trace[i].toString()+"\n");
		}



		return sb.toString();
	}



	public static FastHashMap parseQueryString(String queryString){
		FastHashMap fMap = new FastHashMap();
		if(queryString.endsWith("&")){
			queryString = queryString.substring(0,queryString.length()-1);
		}
		queryString = queryString.replaceAll("&", " &");
		String[] resPair =  CommonUtil.split(queryString,"&",true);

		for( int idx = 0 ; idx < resPair.length ; idx++) {
			String[] pair = CommonUtil.split(resPair[idx],"=",true);
			fMap.put(pair[0] , URLEncDec.decode(pair[1].trim()));
		}

		return fMap;
	}

	public static String toQueryString(FastHashMap fMap){
		String name = null;
		String value = null;
		StringBuffer sb = new StringBuffer();
		Iterator it =  fMap.keySet().iterator();
		while (it.hasNext())
		{
			try{
			name = (String)it.next();
			value = fMap.getString(name);
			sb.append(name+"="+value+"&");
			}catch(Exception e){}
		}
		return sb.toString();
	}

	public static String toHiddenTag(String queryString){
		StringBuffer sb = new StringBuffer();
		String[] resPair =  CommonUtil.split(queryString,"&",true);

		for( int idx = 0 ; idx < resPair.length ; idx++) {
			String[] pair = CommonUtil.split(resPair[idx],"=",true);
			sb.append("<input type=\"hidden\" name=\""+pair[0]+"\" value=\""+pair[1]+"\">\n" );
		}

		return sb.toString();
	}

	public static String convertAmount(double amount){
		amount = adjustDoubleHalfUp(0,amount);
		Double d = new Double(amount);
		String amt = CommonUtil.toString(d.longValue());
		return amt;

	}


	public static double adjustDoubleHalfUp(int scale,double d){
		return adjustDouble(scale,d,BigDecimal.ROUND_HALF_UP);
	}

	public static double adjustDoubleHalfDown(int scale,double d){
		return adjustDouble(scale,d,BigDecimal.ROUND_HALF_DOWN);
	}

	public static double adjustDouble(int scale,double d,int roundingMode){
		try{
			BigDecimal bd = new BigDecimal(d);
			d = bd.setScale(scale,roundingMode).doubleValue();
		}catch(Exception e){
		}
		return d;
	}

	public static String enTrim(String str){
		if(str != null){
			for(int i=0;i<str.getBytes().length;i++){
				str = str.replace('��',' ').trim();
			}
			str = str.replaceAll(" ","");
		}

		byte[] oByte = str.getBytes();
		int index = 0;
		for(int i=0 ; i < oByte.length ; i++){
			if( oByte[i]  == 63){
				index = i;
			}
		}

		if(index > 0){
			byte[] nByte = new byte[index];
			for(int i=0; i< index ; i++){
				nByte[i] = oByte[i];
			}
			str = new String(nByte);
		}
		return str;
	}

	public static String[] getStringArray(int size,String fill){
		String[] str = new String[size];
		for(int i=0 ; i<size;i++){
			str[i] = fill;
		}

		return str;
	}

	public static int[] getIntArray(int size,int fill){
		int[] it = new int[size];
		for(int i=0 ; i<size;i++){
			it[i] = fill;
		}

		return it;
	}

	public static String intArrayToString(int[] it){
		String str = "";
		for(int i=0 ; i <it.length ; i++){
			str = str+toString(it[i]);
		}

		return str;
	}

	public static int getArrayMatchNumber(int[] it, int match,int limit){
		int matchNumber = 0;
		if(it == null){
			matchNumber = 0;
		}else{
			for(int i=0 ; i < limit ; i++){
				if(it[i] == match){
					matchNumber++;
				}
			}
		}
		return matchNumber;
	}

	/**
	 * begin �� end �� ��¥ ���̸� ���ϴ� ��
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long diffOfDay(Timestamp begin, Timestamp end){
	    return (end.getTime() - begin.getTime()) / (24 * 60 * 1000);
	}

	public static long diffOfDay(String begin, String end) throws Exception{
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date beginDate = formatter.parse(begin);
	    Date endDate = formatter.parse(end);
	    return (endDate.getTime() - beginDate.getTime()) / (24* 60 * 1000);
	}

	/**
	 * �Էµ� ����� ������ �ϼ��� ���ϴ� ����
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	/**
	 * �Էµ� ����� ������ �ϼ��� ���ϴ� ����
	 * @param yyyyMM
	 * @return
	 */
	public static int getLastDayOfMonth(String yyyyMM){
		return getLastDayOfMonth(CommonUtil.parseInt(yyyyMM.substring(0,4)),CommonUtil.parseInt(yyyyMM.substring(4,6))-1);

	}

	/**
	 * �Էµ� ��¥���� ���̳��� �ϼ� ���
	 * getDifferDays("20080101","20080102")
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDifferDays(String startDate,String endDate){
		GregorianCalendar startCal = getGregorianCalendar(startDate);
		GregorianCalendar endCal = getGregorianCalendar(endDate);

		long differ = (endCal.getTime().getTime() - startCal.getTime().getTime())/86400000;
		return differ;
	}


	public static GregorianCalendar getGregorianCalendar(String yyyyMMdd){
		int yyyy 	= parseInt(yyyyMMdd.substring(0,4));
		int mm 		= parseInt(yyyyMMdd.substring(4,6));
		int dd 		= parseInt(yyyyMMdd.substring(6));

		GregorianCalendar calendar = new GregorianCalendar(yyyy,mm-1,dd,0,0,0);
		return calendar;

	}

	/**
	 * Calendar.YEAR,Calendar.MONTH,Calendar.DATE , Amount , yyyyMMdd
	 * @param field
	 * @param amount
	 * @param date
	 * @return
	 */
	public static String getOpDate(int field,int amount,String date){
		GregorianCalendar calDate = getGregorianCalendar(date);

		if(field == Calendar.YEAR){
			calDate.add(GregorianCalendar.YEAR, amount);
		}else if(field == Calendar.MONTH){
			calDate.add(GregorianCalendar.MONTH, amount);
		}else if(field == Calendar.DATE){
			calDate.add(GregorianCalendar.DATE, amount);
		}else{

		}
		return getYyyyMMdd(calDate);
	}


	public static String getYyyyMMdd(Calendar cal){
		Locale currentLocal = new Locale("KOREAN","KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd",currentLocal);
		return formatter.format(cal.getTime());
	}


	public static int getDayOfWeek(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}







}
