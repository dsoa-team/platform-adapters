package br.ufpe.cin.dsoa.adapter.mongodb;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

public class ClientExample {
	
	public static void main(String[] args) {
		
		Mongo conn = null;
		try {
			conn = new Mongo("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WriteConcern w = new WriteConcern(1, 2000);
		conn.setWriteConcern(w);
		
		DB db = conn.getDB("crawler");
		DBCollection coll = db.getCollection("sites");
		
		//DBObject doc = new BasicDBObject();
		//String[] tags = {"database", "open-source"};
		
		//doc.put("url", "org.mongodb");
		//doc.put("tags", tags);
		
		DBObject attrs = new BasicDBObject();
		attrs.put("lastAcess", new Date());
		attrs.put("pingtime", 20);
		
		//doc.put("attrs", attrs);
		
		coll.insert(attrs);
		
		System.out.println("Initial document:n");
		System.out.println(attrs.toString());	
		
		System.out.println("Updating pingtime... n");
		//coll.update(new BasicDBObject("_id", doc.get("_id")), 
			//	new BasicDBObject("$set", new BasicDBObject("pingtime", 30)));
		
		DBCursor cursor = coll.find();
		//System.out.println("After updaten");
		System.out.println(cursor.next().toString());
		
		//System.out.println("Number of site documents: " + coll.count());
		
		//System.out.println("Removing documents... n");
		coll.remove(new BasicDBObject());
		
	}

}
