## jms-examples
============

## INTRO
This code demonstrates using a divert to divert a JMS message from a TOPIC to a QUEUE. It is based on Jboss quickstarts and docs.

### Pre-requisites:
1. Application Users must exist on JBoss (matching the code)
2. Jboss must be running before executing this code
3. Queue and Topic must exist

```xml
                <jms-destinations>
                    <jms-queue name="wfsInternalEventQueue">
                        <entry name="queue/wfsInternalEventQueue"/>
                    </jms-queue>
                    <jms-queue name="testQueue">
                        <entry name="queue/testQueue"/>
                        <entry name="java:jboss/exported/jms/queue/testQueue"/>
                    </jms-queue>
                    <jms-topic name="testTopic">
                        <entry name="topic/testTopic"/>
                        <entry name="java:jboss/exported/jms/topic/testTopic"/>
                    </jms-topic>
                </jms-destinations>
```

4. Create divert by running:

```
[standalone@localhost:9999 /] /subsystem=messaging/hornetq-server=default/divert=my-divert:add(divert-address=jms.topic.topic1,forwarding-address=jms.queue.queue1,exclusive=true)
```

5. Execute code by running :
```
mvn clean compile exec:java
```


Divert will look like:
```xml
 <diverts>
    <divert name="my-divert">
    <address>jms.topic.testTopic</address>
    <forwarding-address>jms.queue.testQueue</forwarding-address>
    <exclusive>true</exclusive>
    </divert>
   </diverts>
```