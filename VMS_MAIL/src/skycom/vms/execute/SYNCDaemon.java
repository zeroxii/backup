package skycom.vms.execute;

public class SYNCDaemon {

	public SYNCDaemon() {
		// TODO Auto-generated constructor stub
	}
	public void execute(){
		System.out.println("WEATHER DAEMON SYNC...EXECUTE!");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		skycom.vms.execute.SYNCDaemon daemon = new skycom.vms.execute.SYNCDaemon(); 
		System.out.println("WEATHER DAEMON SYNC...START!");
		daemon.execute();

	}

}
