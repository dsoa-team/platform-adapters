package br.ufpe.cin.dsoa.adapter;

import java.util.Map;

import br.ufpe.cin.dsoa.adapter.redis.RedisClient;
import br.ufpe.cin.dsoa.adapter.serializer.JsonSerializer;
import br.ufpe.cin.dsoa.api.event.Event;
import br.ufpe.cin.dsoa.api.event.EventAdapter;
import br.ufpe.cin.dsoa.api.event.EventConsumer;
import br.ufpe.cin.dsoa.api.event.Subscription;

public class RedisAdapter implements EventAdapter {

	private final String serviceId;

	public RedisAdapter() {
		serviceId = "RedisAdapter";
	}

	@Override
	public String getId() {
		return serviceId;
	}

	public void exportEvent(Event event, Map<String, Object> configuration) {

		String jsonEvent = JsonSerializer.getInstance().getJson(event);

		String name = event.getEventType().getName();
		String source = (String) event.getMetadataProperty("soruce").getValue();
		String key = String.format("%s.%s", name, source);
		
		RedisClient.getInstance().add(key, jsonEvent);

	}

	public void importEvent(EventConsumer consumer, Subscription subscription) {
	}
}
