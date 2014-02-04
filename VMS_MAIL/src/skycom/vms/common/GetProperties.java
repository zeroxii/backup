package skycom.vms.common;


import java.util.Properties;
import org.apache.log4j.Logger;



public class GetProperties {
	//private static final Logger LOGGER = LoggerFactory.getLogger(GetProperties.class);
	private static final Logger logger = LoggerHelper.getLogger(); 	
	static GetProperties instance = new GetProperties();
	
	public static GetProperties getInstance(){
		return instance;
	}
	public String getMessageReturnCode(String key){
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("MessageResources.properties"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String value = prop.getProperty(key);
		
		return value;
	}
	public String getDBReturnCode(String key){
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String value = prop.getProperty(key);
		
		return value;
	}
}
