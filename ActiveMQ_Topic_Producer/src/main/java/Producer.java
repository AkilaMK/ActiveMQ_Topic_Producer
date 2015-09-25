import java.io.Serializable;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;

public class Producer {

	private static String topicName = "topic";
	private static String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	private static String url = "tcp://localhost:61616";

	public static void main(String[] args) throws JMSException {

		Properties properties = new Properties();

		properties.put("java.naming.factory.initial", initialContextFactory);
		properties.put("connectionfactory.TopicConnectionFactory", url);
		properties.put("topic." + topicName, topicName);
		
		try {
			
			InitialContext ctx = new InitialContext(properties);
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory");
			TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
			
			Session session = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Topic topic = session.createTopic("topic");

			MessageProducer producer = session.createProducer(topic);

			TextMessage message = session.createTextMessage();
			message.setText("HELLO JMS WORLD");
			
			ObjectMessage objMessage = session.createObjectMessage();
			objMessage.setObject((Serializable) new String("Seria"));
			
			for (int i = 0; i < 20; i++) {
				producer.send(message);

				System.out.println("Sent message : " + message.getText() + "'");
				
				Thread.sleep(2000);
			}
			
			topicConnection.close();
			
		} catch (Exception e) {

		}
	}
}