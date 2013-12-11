package br.ufpe.cin.dsoa.adapter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.dsoa.adapter.serializer.JsonSerializer;
import br.ufpe.cin.dsoa.api.event.Event;
import br.ufpe.cin.dsoa.api.event.EventAdapter;
import br.ufpe.cin.dsoa.api.event.EventConsumer;
import br.ufpe.cin.dsoa.api.event.EventType;
import br.ufpe.cin.dsoa.api.event.EventTypeCatalog;
import br.ufpe.cin.dsoa.api.event.Subscription;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class AMPQAdapter implements EventAdapter {

	private ExecutorService executorService;
	private EventTypeCatalog eventTypeCatalog;
	private final String serviceId;

	public AMPQAdapter() {
		executorService = Executors.newCachedThreadPool();
		serviceId = "AMPQAdapter";
	}

	@Override
	public String getId() {
		return serviceId;
	}

	public void exportEvent(Event event, Map<String, Object> configuration) {

		final String queueName = event.getEventType().getName();

		try {
			Connection connection = createConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);

			String jsonEvent = JsonSerializer.getInstance().getJson(event);
			byte[] msg = jsonEvent.getBytes();

			channel.basicPublish("", queueName, null, msg);
			channel.close();
			connection.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void importEvent(EventConsumer consumer, Subscription subscription) {
		final String queueName = subscription.getEventType().getName();

		try {
			Connection connection = createConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);

			final QueueingConsumer queueConsumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, "", true, false, null, queueConsumer);
			executorService.execute(new Worker(queueConsumer, consumer));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Connection createConnection() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("admin");
		factory.setPassword("admin");

		return factory.newConnection();
	}

	class Worker implements Runnable {

		private QueueingConsumer queueConsumer;
		private EventConsumer consumer;

		public Worker(QueueingConsumer queueConsumer, EventConsumer consumer) {
			super();
			this.queueConsumer = queueConsumer;
			this.consumer = consumer;
		}

		@Override
		public void run() {

			while (true) {
				String message = receive();
				Map<String, Object> eventMap = JsonSerializer.getInstance().getEventMap(message);
				String eventTypeName = (String) eventMap.get("type");

				EventType eventType = eventTypeCatalog.get(eventTypeName);
				Event dsoaEvent = eventType.convertToEvent(eventMap);
				consumer.handleEvent(dsoaEvent);
			}
		}

		private String receive() {
			String message = "";

			try {
				QueueingConsumer.Delivery delivery = queueConsumer.nextDelivery();
				message = new String(delivery.getBody());
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
			} catch (ConsumerCancelledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return message;
		}
	}

}
