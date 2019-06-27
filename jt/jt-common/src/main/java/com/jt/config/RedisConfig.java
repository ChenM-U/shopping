package com.jt.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;

//Redis的配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String redisNodes;
//	@Value("${redis.nodes}")
//	private String redisNodes;
/*	@Value("${redis.sentinels}")
	private String JedisSentinelNodes;
	@Value("${redis.sentinel.masterN*/
	//private String masterName;
	
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		String[] split = redisNodes.split(",");
		for (String string : split) {
			String host = string.split(":")[0];
			int port = Integer.parseInt(string.split(":")[1]);
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
		
	}
	
	//哨兵机制实现redis
	/*@Bean
	public JedisSentinelPool jedisSentinelPool() {
		Set<String> sentinels = new HashSet<>();
		String[] nodes = JedisSentinelNodes.split(",");
		for (String sNode : nodes) {
			sentinels.add(sNode);
		}
		return new JedisSentinelPool(masterName,sentinels);
	}*/
	
//	@Bean
//	public ShardedJedis shardedJedis() {
//		ArrayList<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//		String[] nodes = redisNodes.split(",");
//		for (String node : nodes) {
//			String host = node.split(":")[0];
//			String port = node.split(":")[1];
//			JedisShardInfo info = new JedisShardInfo(host,port);
//			shards.add(info);
//		}
//		return new ShardedJedis(shards);
//	}
	/*
	 * 
	 * @Value("${jedis.host}") private String host;
	 * 
	 * @Value("${jedis.port}") private Integer port;
	 * 
	 * @Bean public Jedis jedis() { return new Jedis(host,port); }
	 * 
	 */}
