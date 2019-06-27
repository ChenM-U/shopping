package com.jt;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//redis分片操作
public class TestRedisShards {
	/*
	 * 	操作时，需要把一台redis当做一台
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo info1 = new JedisShardInfo("192.168.190.23", 6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.190.23", 6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.190.23", 6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		//操作分片的redis对象的工具类
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("1902", "1902最棒");
 		System.out.println(shardedJedis.get("1902"));
	}
	
}
