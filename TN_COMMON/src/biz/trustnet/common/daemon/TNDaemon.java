/* 
 * Project Name : TN_COMMON
 * File Name	: TNDaemon.java
 * Date			: 2007. 08. 20
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} 임주섭
 * Comment      : biz.trustnet.common.daemon.Daemon 을 구동하기 윈한 추상클래스 
 *                Daemon 이용자는 아래의 클래스의 아래 3개 요청 메소드를 구현해야 한다.
 */

package biz.trustnet.common.daemon;


public abstract class TNDaemon {
	
	public abstract boolean request();
}
