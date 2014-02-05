/*
 * Project Name	:  TN_COMMON
 * File Name		:	CommonUtil.java
 * Date				:	2005. 3. 31. - 오후 2:12:59
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
		NULL 값을 체크 하여 주는 함수 모음.
		@param s null인지 확인하고 싶은 String 값
		@return null이면 공백반환, 아니면 그대로 반환
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
		String형 숫자를 int형 숫자로 변환한다. 변환이 안되는 String은 0로 반환한다.

		@param s int로 바꿀 String
		@return int형으로 바뀐 String
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
		날짜 데이터가 오늘보다 과거인지 미래인지 체크한다.
		@param date yyyy-MM-dd 형식의 문자열 날짜 데이터
		@return 과거면 false, 같거나 미래라면 true
	*/
	public static boolean isAway(String s) {
		return isAway(s.substring(0, 4), s.substring(5, 7), s.substring(8));
	}


	/**
	 * 현재날짜에서 day 만큼 더하고 뺀 날자를 가져온다.
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
		날짜 데이터가 오늘보다 과거인지 미래인지 체크한다.
		@param yyyy 년(年)
		@param mm 월(月)
		@param dd 일(日)
		@return 과거면 false, 같거나 미래면 true
	*/

	public static boolean isAway(String yyyy, String mm, String dd) {
		int y = parseInt(yyyy);
		int m = parseInt(mm);
		int d = parseInt(dd);
		return isAway(y, m, d);
	}

	/**
		날짜 데이터가 오늘보다 과거인지 미래인지 체크한다.
		@param y 년(年)
		@param m 월(月)
		@param d 일(日)
		@return 과거면 false, 같거나 미래면 true
	*/
	public static boolean isAway(int y, int m, int d) {
		boolean dateChk = false;

		GregorianCalendar rightNow = new GregorianCalendar();
		GregorianCalendar fromDate = new GregorianCalendar(y, (m - 1), d);

		if (fromDate.before(rightNow)) {
			dateChk = false; //date가 rightNow보다 과거에있다.
		} else {
			dateChk = true; //date가 rightNow보다 미래에있다.
		}

		return dateChk;
	}


	/**
		DB Query중 ' 문자를 ` 문자로 치환한다. 모든 DB Query는 이 메쏘드를 적용시켜야 할 것이다.

		@param s ' 문자를 ` 문자로 치환하고 싶은 문장
		@return ' 문자를 ` 문자로 치환한 문장
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
		문자열이 길때 어떤 특정한 길이에서 짤라서 반환한다. 짜른 뒤에 "..."를 붙인다.
		단, 한글이나 영문이나 모두 1글자로 취급한다.

		@param s 짜르고 싶은 문장
		@param i 짜르고 싶은 길이
		@return 일정길이로 짜른 문자열에 "..."를 붙여 반환한다.
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
		문자열이 길때 어떤 특정한 길이에서 짤라서 반환한다. 짜른 뒤에 ".."를 붙인다.
		단, 한글은 2바이트, 영문은 1바이트로 취급하여 반올림해서 짜른다.

		@param s 짜르고 싶은 문장
		@param i 짜르고 싶은 길이
		@return 일정길이로 짜른 문자열에 "..."를 붙여 반환한다.
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
		통화형식처럼 숫자 3자리마다 ,(콤마)를 찍는다. double(int)형 인자를 받는다.

		@param money double(int)형 통화형식
		@return 3자리마다 ,(콤마)가 찍힌 형식
	*/
	public static String moneyFormat(double d) {
		NumberFormat numberformat = NumberFormat.getNumberInstance();
		String s = numberformat.format(d);
		return s;
	}

	/**
		통화형식처럼 숫자 3자리미다 ,(콤마)를 찍는다. String형 인자를 받는다.

		@param money String형 통화형식
		@return 3자리마다 ,(콤마)가 찍힌 형식
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
		대상문자열(strTarget)에서 특정문자열(strSearch)을 찾아 지정문자열(strReplace)로	변경한 문자열을 반환한다.

		@param strTarget 대상문자열
		@param strSearch 변경대상의 특정문자열
		@param strReplace 변경 시키는 지정문자열
		@return 변경완료된 문자열
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
		대상문자열(strTarget)의 임의의 위치(loc)에 지정문자열(strInsert)를 추가한 문자열을 반환한다.

		@param strTarget 대상문자열
		@param loc 지정문자열을 추가할 위치로서 대상문자열의 첫문자 위치를 0으로 시작한 상대 위치. loc가 0 보다 작은 값일 경우는 대상문자열의 끝자리를 0으로 시작한 상대적 위치. 맨앞과 맨뒤는 문자열 + 연산으로 수행가능함으로 제공하지 않는다.
		@param strInsert 추가할 문자열
		@return 추가완료된 문자열
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
		대상문자열(strTarget)에서 지정문자열(strDelete)을 삭제한 문자열을 반환한다.

		@param strTarget 대상문자열
		@param strDelete 삭제할 문자열
		@return 삭제완료된 문자열
	*/
	public static String delete(String strTarget, String strDelete) {
		return replace(strTarget, strDelete, "");
	}

	/**
		대상문자열(strTarget)에서 구분문자열(strDelim)을 기준으로 문자열을 분리하여	각 분리된 문자열을 배열에 할당하여 반환한다.

		@param strTarget 분리 대상 문자열
		@param strDelim 구분시킬 문자열로서 결과 문자열에는 포함되지 않는다.
		@param bContainNull 구분되어진 문자열중 공백문자열의 포함여부. true : 포함, false : 포함하지 않음.
		@return 분리된 문자열을 순서대로 배열에 격납하여 반환한다.
	*/
	public static String[] split(String strTarget, String strDelim, boolean bContainNull) {
		// StringTokenizer는 구분자가 연속으로 중첩되어 있을 경우 공백 문자열을 반환하지 않음.
		// 따라서 아래와 같이 작성함.

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
	대상문자열(strTarget)에서 지정문자열(strSearch)이 검색된 횟수를, 지정문자열이 없으면 0 을 반환한다.

	@param strTarget 대상문자열
	@param strSearch 검색할 문자열
	@return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
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
		대상문자열안의 논리적 값이 숫자를 표현하면 true, 아니면 false를 반환한다.

		@param str 대상문자열
		@return 대상문자열의 논리적 값이 숫자이면 true, 아니면 false
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
		대상문자열안의 논리적 숫자앞에 원하는 size의 자릿수에 맞게 '0'를 붙인다. 예) 000000000 (일억자리수)에 345를 넣었을 경우 '000000345'반환

		@param str 대상문자열
		@param size 원하는 자릿수
		@return 대상문자열에 자릿수 만큼의 '0'를 붙인 문자열
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
		int형 숫자앞에 원하는 size의 자릿수에 맞게 '0'를 붙인다. 예) 000000000 (일억자리수)에 345를 넣었을 경우 '000000345'반환

		@param str 대상 int형 숫자
		@param size 원하는 자릿수
		@return 대상문자열에 자릿수 만큼의 '0'를 붙인 문자열
	*/
	public static String zerofill(int num, int size) {
		return zerofill(new Integer(num), size);
	}

	/**
		long형 숫자앞에 원하는 size의 자릿수에 맞게 '0'를 붙인다. 예) 000000000 (일억자리수)에 345를 넣었을 경우 '000000345'반환

		@param str 대상 long형 숫자
		@param size 원하는 자릿수
		@return 대상문자열에 자릿수 만큼의 '0'를 붙인 문자열
	*/
	public static String zerofill(long num, int size) {
		return zerofill(new Long(num), size);
	}

	/**
		Number형 숫자앞에 원하는 size의 자릿수에 맞게 '0'를 붙인다. 예) 000000000 (일억자리수)에 345를 넣었을 경우 '000000345'반환

		@param num 대상 Number형 숫자
		@param size 원하는 자릿수
		@return 대상문자열에 자릿수 만큼의 '0'를 붙인 문자열
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
	 * 스패이스 문자열을 리턴한다.
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
		문자열을 바이트로 취급하여 len만큼 Filler를 추가하여 반환한다..

		@param oldStr 기존 문자열
		@param len 문자열의 길이
		@return byte기준 len인 문자열을 반환한다.
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
		바이트 배열에서 len의 길이 만큼을 반환한다.

		@param b 짜르고 싶은 배열
		@param len 짜르고 싶은 길이
		@return byte기준 len인 문자열을 반환한다.
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
	 * 바이트 배열을 스트링으로 변환한다.
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
	 * 바이트 배열을 스트링으로 변환한다.
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
	 * 바이트 배열로 구성되어져 있는 두 변수에 대하여 재 바이트 배열 형태로 생성됨.
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
	 * target 의 index 에 byte[]를 입력.
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

	public static byte[] leftSpaceFill(String input, int len) { // 왼쪽으로 스페이스 체움
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

	public static byte[] leftZeroFill(String input, int len) { // 왼쪽으로 스페이스 체움
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

	public static byte[] rightSpaceFill(String input, int len) { // 왼쪽으로 스페이스 체움
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

	public static byte[] rightZeroFill(String input, int len) { // 왼쪽으로 스페이스 체움
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
		"yyyy-MM-dd HH:mm:SS"형의 날짜를 반환한다.
		@return yyyy-MM-dd HH:mm:SS 형의 날짜
	*/

	public static String getCurrentDate() {
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
		인자로 들어오는 형식의 날짜를 반환한다.
		@return 인자로 들어오는 형식의 날짜
	*/

	public static String getCurrentDate(String s) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
		return simpledateformat.format(new Date());
	}



	/**
	* 날짜를 받아 Timestamp 로 변환한다
	* @param  dateString : yyyyMMdd, timeString : HHmmss
	* @return 배열값[2] : 성공 OK, 실패 :FAIL
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
	* 현재 시간의 Timestamp를 반환한다.
	* @param
	* @return Timestamp Object
	*/
	public static Timestamp getCurrentTimestamp() {
		Date currDt = new Date(System.currentTimeMillis());
		Timestamp timeDt = new Timestamp(currDt.getTime());
		return timeDt;
	}

	/**
	* Timestamp를 받아 format 형태의 스트링으로 변환한다
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
	* Timestamp를 받아 format 형태의 스트링으로 변환한다
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
	 * Timestamp 를 받아 데이터베이스의 시간형으로 변화시키는 쿼리
	 * @param timestamp
	 * @return
	 */
	public static String convertTimestampToDB(Timestamp timestamp){
		return "TO_DATE('" +convertTimestampToString(timestamp,"MM/dd/yyyy hh:mm:ss")+"' ,'mm/dd/yyyy hh24:mi:ss')" ;
	}

	/**
	* String exmple = "MEMID=kkk&key=value" 를 Map으로 변환하여 준다.
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
	 * Map 에 있는 값을 String으로 변환하여 준다.
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
	 * String Value 를 FastHashMap 으로 변환
	 * 1차 구분자 &  2차 구분자 =
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
	 * FastHashMapMap 에 있는 값을 String으로 변환하여 준다.
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
	 * 문자열의 뒷자리를 감출 때 사용하는 메서드 	예) 1234567890 ==> 12345*****
	 * 2004/1/7, zoo 추가
	 * @param target 가공할 문자열
	 * @param delim 감출 때 사용할 기호
	 * @param count 나타낼 문자의 길이
	 * @return 가공된 문자열을 반환
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
	 * 다날 통신 응답값에 대한 파싱 처리 부분
	 * output은 다날에서 오는 데이터,value는 요청 인자값.
	 * @param output
	 * @param Value
	 * @return name에 대한 Value
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
	 * String[] 에 null 이나 space가 들어가 있을 경우 이를 null 에러가 발생하지 않도록 만들어 주는 유틸.
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
	 * 랜덤한 숫자 번호 생성.
	 * String 형태로 사용하기 위해서는 CommonUtil.zerofill(숫자,길이) 을 사용하면 된다.
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

		//마지막에 구분자가 들어가면 장애를 발생할 수 있으므로 제거한다.
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

		//마지막에 구분자가 들어가면 장애를 발생할 수 있으므로 제거한다.
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

		//마지막에 구분자가 들어가면 장애를 발생할 수 있으므로 제거한다.
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
	 * 	폰헤더에서 min 추출후 정상적인 폰번호로 변경..
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
				str = str.replace('　',' ').trim();
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
	 * begin 과 end 의 날짜 차이를 구하는 법
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
	 * 입력된 년월의 마지막 일수를 구하는 공식
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
	 * 입력된 년월의 마지막 일수를 구하는 공식
	 * @param yyyyMM
	 * @return
	 */
	public static int getLastDayOfMonth(String yyyyMM){
		return getLastDayOfMonth(CommonUtil.parseInt(yyyyMM.substring(0,4)),CommonUtil.parseInt(yyyyMM.substring(4,6))-1);

	}

	/**
	 * 입력된 날짜간의 차이나는 일수 계산
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
