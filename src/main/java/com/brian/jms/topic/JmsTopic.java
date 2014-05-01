package com.brian.jms.topic;

import com.brian.jms.listeners.TopicListener;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ebrigun
 * Date: 24/04/14
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class JmsTopic {

    private static final Logger logger  = Logger.getLogger("JmsTopic");

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
    TopicSession session;
    Topic topic;
    TopicConnection topicConn;
    public JmsTopic(){

        try {
            intiialiseTopic();
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void intiialiseTopic() throws NamingException, JMSException {

        final Properties envTopic = new Properties();
        envTopic.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        envTopic.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        envTopic.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
        envTopic.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
        Context contextTopic = new InitialContext(envTopic);



        // InitialContext iniCtx = new InitialContext();
        // logger.info("init lookup...");
        // Object tmp = iniCtx.lookup("ConnectionFactory");
        // logger.info("Topic Conn F");
        // Perform the JNDI lookups

        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        logger.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
        connectionFactory = (ConnectionFactory) contextTopic.lookup(connectionFactoryString);
        logger.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

        TopicConnectionFactory tcf = (TopicConnectionFactory) connectionFactory;
        logger.info("TopicConnectionFactory created: "+tcf);
        topicConn = tcf.createTopicConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
        logger.info("Topic Connection created: "+topicConn );

        String topicString = System.getProperty("topic", DEFAULT_TOPIC);
        logger.info("Attempting to acquire topic \"" + topicString + "\"");
        topic= (Topic) contextTopic.lookup(topicString);
        logger.info("Found topic \"" + topicString + "\" in JNDI");
        logger.info("Attempting to create topic session ");
         session = topicConn.createTopicSession(false,
                TopicSession.AUTO_ACKNOWLEDGE);
        logger.info("Attempting to start topic connection ");
        topicConn.start();
        logger.info("Success connection started...");
        logger.info("Attempting to create subscriber...");
        TopicSubscriber recv = session.createSubscriber(topic);
        logger.info("Success subscriber created..");
        logger.info("Attempting to set message listener...");
        recv.setMessageListener(new TopicListener());
        logger.info("Success");
    }


    public void sendMessage(String text, int noOfMessages) throws InterruptedException {

        // Send a text msg
        TopicPublisher send = null;
        try {
            send = session.createPublisher(topic);

            TextMessage tm = session.createTextMessage(text);
            logger.info("Publishing "+noOfMessages+ " messages: "+ tm.getText());

            for(int i=0;i<noOfMessages;i++) {
                System.out.println("\n");
                logger.info("***********************************************************************");
                logger.info("Publishing message: "+ tm.getText());
                send.publish(tm);
                Thread.sleep(2000);
            }
            logger.info("Message Published" );
            send.close();
            logger.info("Publisher closed");
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
