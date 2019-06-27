package com.jt;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import com.jt.pojo.User;

import redis.clients.jedis.Jedis;

public class testRedis {
	
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		jedis.set("1902","达内");
		jedis.expire("1902", 30);
		System.out.println(jedis.get("1902"));
	}
	
	//分布式锁
	@Test
	public void testTimeOut() throws Exception {
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		jedis.setex("aa", 2, "aa");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		//key不存在时候，才有用
		Long result = jedis.setnx("aa", "bb");
		System.out.println("获取输出数据"+result+":"+jedis.get("aa"));
	}
	
	//实现对象转换Json
	@Test
	public void ObjectToJson() throws Exception {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(998L).setItemDesc("测试");
		ObjectMapper mapper=new ObjectMapper();
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		ItemDesc desc2 = mapper.readValue(json, ItemDesc.class);
		System.out.println("测试对象："+desc2);
	}
	
	//实现List集合与Json转化
	@Test
	public void listToJson() throws Exception {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(998L).setItemDesc("测试");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(998L).setItemDesc("测试");
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		ObjectMapper mapper=new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		System.out.println("转化为json:"+json);
		Jedis jedis = new Jedis("192.168.190.23", 6379);
		jedis.set("itemDescList", json);
		//获取数据
		String string = jedis.get("itemDescList");
		List<ItemDesc> Value = mapper.readValue(string, list.getClass());
		System.out.println(Value);
	} 
	/*
	 *	利用redis保存业务数据
	 *	数据库数据：对象 Object
	 * 	
	 */
	
	@Test
	public void userToJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId(23);
		user.setName("达内");
		user.setAge(1);
		user.setSex("中");
		String json= mapper.writeValueAsString(user);
		System.out.println(json);
	}
	
	@Test
	public void jsonToUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId(23);
		user.setName("达内");
		user.setAge(1);
		user.setSex("中");
		String json= mapper.writeValueAsString(user);
		User user1 = mapper.readValue(json, User.class);
		System.out.println(user1);
	}
}
