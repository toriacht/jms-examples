package com.brian.jms.queue;

import com.brian.jms.listeners.QueueListener;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ebrigun
 * Date: 24/04/14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class JMSQueue {

    private static final Logger logger = Logger.getLogger(JMSQueue.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/testQueue";
    private static final String DEFAULT_TOPIC = "jms/topic/testTopic";

    private static final String DEFAULT_MESSAGE_COUNT = "1";
    //    private static final String DEFAULT_USERNAME = "quickstartUser";
//    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String DEFAULT_USERNAME = "jmsUser2";
    private static final String DEFAULT_PASSWORD = "Passw0rd!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";
    ConnectionFactory connectionFactory;



    public JMSQueue(){

        try {
            setUpQueue();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



    private  void  setUpQueue() throws Exception {


        // Set up the context for the JNDI lookup
        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
        env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
        Context context = new InitialContext(env);

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage message = null;



        try {
            logger.info("Initialising JMS Queue..........................................................................");
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            logger.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
            logger.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            logger.info("Attempting to acquire destination \"" + destinationString + "\"");
            destination = (Destination) context.lookup(destinationString);
            logger.info("Found destination \"" + destinationString + "\" in JNDI");

//            // Create the JMS connection, session, producer, and consumer
//            logger.info(" ################################### #################################################### ");
            logger.info("Creating Connection with username: " + System.getProperty("password", DEFAULT_PASSWORD));
            connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
            logger.info("Creating Queue Session");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            consumer = session.createConsumer(destination);

            logger.info("Creating up queue message listener");
            consumer.setMessageListener(new QueueListener());
            connection.start();
            logger.info("JMS Queue Initialised...........................................................................");

            int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));
            String content = System.getProperty("message.content", DEFAULT_MESSAGE);

//            logger.info("Sending " + count + " messages with content: " + content);
//
//            // Send the specified number of messages
//            for (int i = 0; i < count; i++) {
//                message = session.createTextMessage(content);
//                producer.send(message);
//            }

            // Then receive the same number of messages that were sent
//            for (int i = 0; i < count; i++) {
//                message = (TextMessage) consumer.receive(5000);
//                logger.info("Received message with content " + message.getText());
//            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        } finally {
            if (context != null) {
//                context.close();
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
//                connection.close();
            }
        }
    }

}
