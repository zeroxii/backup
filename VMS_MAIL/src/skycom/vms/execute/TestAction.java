package skycom.vms.execute;
import org.apache.log4j.Logger;

import skycom.vms.common.GetProperties;
import skycom.vms.common.LoggerHelper;

public class TestAction {
	//private static final Logger LOGGER = LoggerFactory.getLogger(TestAction.class);
	private static final Logger logger = LoggerHelper.getLogger(); 	
	public TestAction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String value = GetProperties.getInstance().getMessageReturnCode("test");
		logger.info("dddd");
		System.out.println("value : "+value);
		//LOGGER.info("log test");

	}

}
