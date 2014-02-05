package skycom.pbx.db;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DBCPServlet extends HttpServlet {
   
    public void init(ServletConfig config) throws ServletException {
        String registerPool = "";
        String jdbcDriver = ""; 
        String jdbcURL = "";
        String user = "";
        String password = ""; 
        int maxActive = 0;
        int maxIdle = 0;
        
        registerPool = config.getInitParameter("registerPool");
        jdbcDriver = config.getInitParameter("jdbcDriver"); 
        jdbcURL = config.getInitParameter("jdbcURL");
        user = config.getInitParameter("user");
        password = config.getInitParameter("password");
        maxActive = Integer.parseInt(config.getInitParameter("maxActive"));
        maxIdle = Integer.parseInt(config.getInitParameter("maxIdle"));
        
        try {
            Class.forName(jdbcDriver);
            
            // Connection Pool
            GenericObjectPool connectionPool = new GenericObjectPool(null);
            connectionPool.setMaxActive(maxActive); 
            connectionPool.setMaxIdle(maxIdle); 
            connectionPool.setTestOnBorrow(false);
            connectionPool.setTestOnReturn(false);
            connectionPool.setTestWhileIdle(true);
            connectionPool.setNumTestsPerEvictionRun(1);
            connectionPool.setTimeBetweenEvictionRunsMillis(100000);
   
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                jdbcURL,
                user, 
                password);
            
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                connectionFactory,
                connectionPool,
                null, // statement pool
                "select * from t_web_user", 
                false, 
                true);
            
            PoolingDriver driver = new PoolingDriver();

            driver.registerPool(registerPool, connectionPool);
            
            System.out.println("---------------------------------------");
            System.out.println("dbcp Connection Pool loading..");
            System.out.println("---------------------------------------");
            
        } catch(Exception ex) {
            //throw new ServletException(ex);
            ex.printStackTrace();
        }

    }
}
