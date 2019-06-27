package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class testSentinel {
	@Test
	public void testsentinel() {
		//masterName 代表主机的变量名称
		//sentinels set<String> IP:端口
		Set<String> sentinels=new HashSet<>();
		sentinels.add("192.168.190.23:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("mygod", "端午过后，没有假了！");
		System.out.println(jedis.get("mygod"));
		jedis.close();
	}
	
}
