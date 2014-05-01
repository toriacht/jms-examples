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

import com.brian.jms.queue.JMSQueue;
import com.brian.jms.topic.JmsTopic;
import org.apache.log4j.Logger;


public class JMSDivertDemo {
    private static final Logger log = Logger.getLogger(JMSDivertDemo.class.getName());


    public static void main(String[] args) throws Exception {

        setUpJMSQueue();
        setUpJmsTopic();

    }

    /**
     *
     */
    private static void setUpJmsTopic() {
        int noOfMessages = 5;
        log.info("Instantiating topic...............");
        JmsTopic topic = new JmsTopic();
        try {
            topic.sendMessage("Skiddidly Dee", noOfMessages);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     *
     * @throws Exception
     */
    private static void setUpJMSQueue() throws Exception {

        log.info("Instantiating queue...............");
        JMSQueue queue = new JMSQueue();

    }

}

