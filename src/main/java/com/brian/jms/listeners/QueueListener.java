package com.brian.jms.listeners;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created with IntelliJ IDEA.
 * User: ebrigun
 * Date: 24/04/14
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class QueueListener implements MessageListener
{
//    private static final Logger logger = LoggerFactory.getLogger(QueueListener.class);
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(QueueListener.class.getName());
    AtomicInteger counter = new AtomicInteger();

    public void onMessage(Message msg)
    {
//        logger.info("Queue Listener activated ");
        // done.release();
        TextMessage tm = (TextMessage) msg;
        counter.incrementAndGet();
        try {

            logger.info("  ::::::: MESSAGE RECEIVED ON QUEUE ::::::: Message Content: " + tm.getText()+" :::::::::::   message no: "+counter);
//            System.out.println(" MESSAGE RECEIVED ON QUEUE LISTENER :::::::Message Content: " + tm.getText()+":::::::::::   message no: "+counter);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}