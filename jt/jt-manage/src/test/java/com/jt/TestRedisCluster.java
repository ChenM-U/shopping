 package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestRedisCluster {
	@Test
	public void TestCluster() {
		Set<HostAndPort> sets = new HashSet<>();
		sets.add(new HostAndPort("192.168.190.23",7000));
		sets.add(new HostAndPort("192.168.190.23",7001));
		sets.add(new HostAndPort("192.168.190.23",7002));
		sets.add(new HostAndPort("192.168.190.23",7003));
		sets.add(new HostAndPort("192.168.190.23",7004));
		sets.add(new HostAndPort("192.168.190.23",7005));
		JedisCluster cluster = new JedisCluster(sets);
		cluster.set("1902", "集群搭建完成");
		System.out.println("获取集群数据:"+cluster.get("1902"));
		
	}	
	
}
