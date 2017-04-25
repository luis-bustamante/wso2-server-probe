package bustamante.luis.jmx;

import org.wso2.carbon.server.admin.service.ServerAdminMBean;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by luisbustamante on 24/04/2017.
 */
public class CarbonClient {

    static String JMX_SERVICE_URL = "service:jmx:rmi://localhost:11111/jndi/rmi://localhost:9999/jmxrmi";

    public static void main(String args[]){

        try {

            //  Provide credentials required by server for user authentication
            HashMap   environment = new HashMap();
            String[]  credentials = new String[] {"admin", "admin"};
            environment.put (JMXConnector.CREDENTIALS, credentials);

            //Create an RMI connection Client and connect to RMI Server
            System.out.println("Creating and connecting RMI...");
            JMXServiceURL url = new JMXServiceURL(JMX_SERVICE_URL);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);

            //Get an MBeanServerConnection
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

            // Construct the ObjectName for the Carbon MBean
            ObjectName mBeanName = new ObjectName("org.wso2.carbon:type=ServerAdmin");
            ServerAdminMBean mBeanProxy = JMX.newMBeanProxy(mbsc, mBeanName,ServerAdminMBean.class);

            System.out.println("WSO2 Server Status:");
            System.out.println("Version: " + mBeanProxy.getServerVersion());
            System.out.println("Status: " + mBeanProxy.getServerStatus());
            System.out.println("is alive: " + mBeanProxy.isAlive());

            jmxc.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
