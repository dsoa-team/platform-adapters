package br.ufpe.cin.dsoa.adapter.mongodb;

import java.net.UnknownHostException;
import java.util.Map;

import br.ufpe.cin.dsoa.api.event.Event;
import br.ufpe.cin.dsoa.api.event.EventAdapter;
import br.ufpe.cin.dsoa.api.event.EventConsumer;
import br.ufpe.cin.dsoa.api.event.Subscription;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongodbAdapter implements EventAdapter{
	
	private final String serviceId;
	
	public MongodbAdapter() {
		serviceId = "MongoAdapter";
	}

	public void exportEvent(Event event, Map<String, Object> configuration) {
		
		String eventType = event.getEventType().getName();
		String source = (String) event.getMetadataProperty("source").getValue();
		
		Mongo conn = null;
		try {
			conn = new Mongo("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		DB db = conn.getDB("dsoa");
		DBCollection coll = db.getCollection(eventType.toLowerCase());
		
		DBObject attrs = new BasicDBObject();
		attrs.put(eventType.toLowerCase(), source.toLowerCase());
		
		
		coll.insert(attrs);
		
		DBCursor cursor = coll.find();
		System.out.println(cursor.next().toString());
		System.out.println("Number of site documents: " + coll.count());
		
	}

	public String getId() {
		return serviceId;
	}

	public void importEvent(EventConsumer arg0, Subscription arg1) {
		
	}

}
