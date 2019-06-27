package com.jt;

import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class testRedis2 {
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		jedis.hset("user1", "id", "200");
		jedis.hset("user1", "name", "tomcat服务器");
		String hget = jedis.hget("user1", "name");
		System.out.println(hget);
		Map<String, String> hgetAll = jedis.hgetAll("user1");
		System.out.println(hgetAll);
	}
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		jedis.lpush("list", "1","2","3","4","5");
		String rpop = jedis.rpop("list");
		System.out.println(rpop);
	}
	
	
	//redis事务控制
	@Test
	public void testTx(){
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		Transaction transaction = jedis.multi();//开启事务
		try {
			transaction.set("ww","www");
			transaction.set("dd",null);
			transaction.exec();//提交事务
		} catch (Exception e) {
			//e.printStackTrace();
			transaction.discard();//事务回滚
		}
	} 
	
}
