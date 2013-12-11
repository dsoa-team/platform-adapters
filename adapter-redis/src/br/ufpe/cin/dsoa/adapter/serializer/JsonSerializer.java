package br.ufpe.cin.dsoa.adapter.serializer;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.cin.dsoa.api.event.Event;

import com.google.gson.Gson;

public class JsonSerializer {

	private static JsonSerializer instance;

	private Gson gson;

	private JsonSerializer() {
		this.gson = new Gson();
	}

	public static JsonSerializer getInstance() {
		if (null == instance) {
			instance = new JsonSerializer();
		}
		return instance;
	}

	public String getJson(Event dsoaEvent) {
		String json = this.gson.toJson(dsoaEvent.toMap());

		return json;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getEventMap(String json) {
		Map<?, ?> dsoaEventMap = this.gson.fromJson(json, HashMap.class);

		return (Map<String, Object>) dsoaEventMap;
	}
}
