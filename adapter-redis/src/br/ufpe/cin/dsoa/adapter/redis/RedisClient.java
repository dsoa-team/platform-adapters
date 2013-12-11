package br.ufpe.cin.dsoa.adapter.redis;

import redis.clients.jedis.Jedis;

public class RedisClient {

	private static RedisClient instance;

	private Jedis redis;

	//private static final String PASSWORD = "password";
	private static final String HOST = "localhost";

	private RedisClient() {
		this.redis = new Jedis(HOST);
		//this.redis.auth(PASSWORD);
	}

	public static RedisClient getInstance() {
		if (null == instance) {
			instance = new RedisClient();
		}
		return instance;
	}

	public void add(String key, String value) {
		this.redis.rpush(key, value);
	}
}
