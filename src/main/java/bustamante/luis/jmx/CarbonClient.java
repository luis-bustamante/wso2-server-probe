package bustamante.luis.jmx;

import org.wso2.carbon.server.admin.service.ServerAdminMBean;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;




/**
 * Created by luisbustamante on 24/04/2017.
 */
public class CarbonClient {

    static String JMX_SERVICE_URL = "service:jmx:rmi://localhost:11111/jndi/rmi://localhost:9999/jmxrmi";


    public static class ClientListener implements NotificationListener{

        public void handleNotification(Notification notification, Object handback) {
            System.out.println("Received notification:");
            System.out.println("ClassName: " + notification.getClass().getName());
            System.out.println("Source: " + notification.getSource());
            System.out.println("Type: " + notification.getType());
            System.out.println("Message: " + notification.getMessage());
            if(notification instanceof AttributeChangeNotification){
                AttributeChangeNotification acn = (AttributeChangeNotification) notification;
                System.out.println("AttributeName: " + acn.getAttributeName());
                System.out.println("AttributeType: " + acn.getAttributeType());
                System.out.println("NewValue: " + acn.getNewValue());
                System.out.println("OldValue: " + acn.getOldValue());
            }
        }
    }


    public static void main(String args[]){

        try {

            //  Provide credentials required by server for user authentication
            HashMap   environment = new HashMap();
            String[]  credentials = new String[] {"admin", "admin"};
            environment.put (JMXConnector.CREDENTIALS, credentials);

            //Create an RMI connection Client and connect to RMI Server
            System.out.println("Creating and connecting RMI");
            JMXServiceURL url = new JMXServiceURL(JMX_SERVICE_URL);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);

            //Create Listener
            ClientListener listener = new ClientListener();

            //Get an MBeanServerConnection
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

            //Get domains from MBeanServer
            System.out.println("Domains: ");
            String domains[] = mbsc.getDomains();
            Arrays.sort(domains);
            for(String domain: domains){
                System.out.println("Domain = " + domain);
            }
            System.out.println("MBeanServer default domain = " + mbsc.getDefaultDomain());
            System.out.println("MBean count = " + mbsc.getMBeanCount());

            System.out.println("Query WSO2 Carbon MBeans: ");
            ObjectName wso2CarbonAll = new ObjectName("org.wso2.carbon:*");
            Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(wso2CarbonAll,null));
            for(ObjectName name: names){
                System.out.println("Object name = " + name);
            }

            // ----------------------
            // Manage the Hello Carbon MBean
            // ----------------------

            System.out.println("Carbon MBean operations");

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
