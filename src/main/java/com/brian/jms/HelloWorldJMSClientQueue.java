/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brian.jms;

import EDU.oswego.cs.dl.util.concurrent.CountDown;

import java.util.Calendar;
import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HelloWorldJMSClientQueue {
    private static final Logger log = Logger.getLogger(HelloWorldJMSClientQueue.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_TOPIC = "jms/topic/testTopic";

    private static final String DEFAULT_MESSAGE_COUNT = "1";
//    private static final String DEFAULT_USERNAME = "quickstartUser";
//    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String DEFAULT_USERNAME = "jmsUser2";
    private static final String DEFAULT_PASSWORD = "Passw0rd!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";

    static CountDown done = new CountDown(1);

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = null;
//        Connection connection = null;
//        Session session = null;
//        MessageProducer producer = null;
//        MessageConsumer consumer = null;
//        Destination destination = null;
//        TextMessage message = null;
        Context context = null;





            // Set up the context for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
            env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
            context = new InitialContext(env);

//            sendAndReceiveMessageOnQueue(connectionFactory, context);

            publishAndReciveMessageOnTopic(connectionFactory, context);


    }


    /**
     * Topic Demonstration
     * @param connectionFactory
     * @param context
     */
    private static void publishAndReciveMessageOnTopic(ConnectionFactory connectionFactory, Context context) {

        try {
            setupPubSub(connectionFactory, context);
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    public static void setupPubSub(ConnectionFactory connectionFactory, Context context)
            throws JMSException, NamingException
    {

        TopicConnection topicConn = null;

       // InitialContext iniCtx = new InitialContext();
       // log.info("init lookup...");
       // Object tmp = iniCtx.lookup("ConnectionFactory");
       // log.info("Topic Conn F");
        // Perform the JNDI lookups

        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
        connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
        log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

        TopicConnectionFactory tcf = (TopicConnectionFactory) connectionFactory;
        log.info("TopicConnectionFactory created: "+tcf);
        topicConn = tcf.createTopicConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
        log.info("Topic Connection created: "+topicConn );

        String topicString = System.getProperty("topic", DEFAULT_TOPIC);
        log.info("Attempting to acquire topic \"" + topicString + "\"");
        Topic topic= (Topic) context.lookup(topicString);
        log.info("Found topic \"" + topicString + "\" in JNDI");
        log.info("Attempting to create topic session ");
        TopicSession session = topicConn.createTopicSession(false,
                TopicSession.AUTO_ACKNOWLEDGE);
        log.info("Attempting to start topic connection ");
        topicConn.start();
        log.info("Success connection started...");
        log.info("Attempting to create subscriber...");
        TopicSubscriber recv = session.createSubscriber(topic);
        log.info("Success subscriber created..");
        log.info("Attempting to set message listener...");
        recv.setMessageListener(new ExListener());
        log.info("Success");

        sendMessage("Test topic message "+ Calendar.getInstance().getTime().toString(),session,topic);









    }

    private static void sendMessage(String text, TopicSession session, Topic topic) {

        // Send a text msg
        TopicPublisher send = null;
        try {
            send = session.createPublisher(topic);

            TextMessage tm = session.createTextMessage(text);
            log.info("Publishing message, sent text=" + tm.getText());
            send.publish(tm);
            log.info("Message Published" );
            send.close();
            log.info("Publisher closed");
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    /**
     *  Queue Demonstration
     * @param connectionFactory
     * @param context
     * @throws Exception
     */
    private static void sendAndReceiveMessageOnQueue(ConnectionFactory connectionFactory, Context context) throws Exception {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage message = null;

        try {
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
            log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            log.info("Attempting to acquire destination \"" + destinationString + "\"");
            destination = (Destination) context.lookup(destinationString);
            log.info("Found destination \"" + destinationString + "\" in JNDI");

//            // Create the JMS connection, session, producer, and consumer
//            log.info(" ################################### #################################################### ");
//            log.info("Creating Connection with username: "+System.getProperty("username", DEFAULT_USERNAME));
//            log.info("Creating Connection with username: "+System.getProperty("password", DEFAULT_PASSWORD));
            connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
//            log.info(" ################################### #################################################### ");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            consumer = session.createConsumer(destination);
            connection.start();

            int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));
            String content = System.getProperty("message.content", DEFAULT_MESSAGE);

            log.info("Sending " + count + " messages with content: " + content);

            // Send the specified number of messages
            for (int i = 0; i < count; i++) {
                message = session.createTextMessage(content);
                producer.send(message);
            }

            // Then receive the same number of messages that were sent
            for (int i = 0; i < count; i++) {
                message = (TextMessage) consumer.receive(5000);
                log.info("Received message with content " + message.getText());
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        } finally {
            if (context != null) {
                context.close();
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static class ExListener implements MessageListener
    {


        public void onMessage(Message msg)
        {
            log.info("Listener activated ");
            done.release();
            TextMessage tm = (TextMessage) msg;
            try {
                log.info("Message received on topic, topic message: " + tm.getText());
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }


}

