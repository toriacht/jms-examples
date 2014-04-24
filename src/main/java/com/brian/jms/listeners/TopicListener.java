package com.brian.jms.listeners;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ebrigun
 * Date: 24/04/14
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class TopicListener implements MessageListener
{
    private static final Logger logger = Logger.getLogger(TopicListener.class.getName());
    AtomicInteger counter = new AtomicInteger();

    public void onMessage(Message msg)
    {
        logger.info("Topic Listener activated ");
        // done.release();
        TextMessage tm = (TextMessage) msg;
        counter.incrementAndGet();
        try {

            logger.info("\n\n~~~~~~~~~~~~~~ MESSAGE RECEIVED ON TOPIC  ~~~~~~~~~~~~~~~~~~~~~" + tm.getText()+"~~~~~~~~ message no: "+counter);


        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}