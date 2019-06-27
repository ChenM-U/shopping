package com.jt.aop;

import java.util.HashSet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component	//交给spring容器管理
@Aspect		//标识切面类
public class RedisAspect {

	//哨兵机制，一般数量为1000时，效率最高

	//容器初始化时，不需要实例化该对象
	//只有用户使用时才初始化
	//一般工具类中添加该注解
	@Autowired(required=false)	//需要时注入,true的话,必须对jedis赋值
	private JedisCluster jedis;
	//private RedisService jedis;
	//private ShardedJedis jedis;
	//private Jedis jedis;


	@SuppressWarnings("unchecked")
	//使用该方法可以直接获取一个注解的对象
	@Around("@annotation(cache_find)")//要求拦截一个注解
	public Object around(ProceedingJoinPoint joinPoint,Cache_find cache_find) {
		//1.获取key
		String key = getKey(joinPoint,cache_find);
		//2.获取缓存数据
		String result = jedis.get(key);
		Object data = null;
		//3.判断结果是否有数据
		try {
			if(StringUtils.isEmpty(result)) {
				//表示缓存中没有数据,查询数据库
				data = joinPoint.proceed();
				//将数据转化为json串
				String json = ObjectMapperUtil.toJSON(data);
				if(cache_find.seconds() == 0)
					jedis.set(key,json);	//表示永不超时
				else
					jedis.setex(key,cache_find.seconds(), json);
				System.out.println("查询数据库");
			}else {
				Class targetClass = getTargetClass(joinPoint);
				data = ObjectMapperUtil.toObject(result, targetClass);
				System.out.println("AOP查询缓存!!!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}

	public String getKey(ProceedingJoinPoint joinPoint, Cache_find cache_find) {
		//获取key类型
		KEY_ENUM keyType = cache_find.keyType();
		//判断用户选择类型
		if(keyType.equals(KEY_ENUM.EMPTY)) {
			//如果用户选择empty则表示使用用户自己的key
			return cache_find.key();
		}else {
			//动态拼接key  ITEM_CAT_0
			String key = cache_find.key();
			//获取用户第一个参数
			String param = String.valueOf(joinPoint.getArgs()[0]);
			return key + "_" + param;
		}
	}
	//获取返回值类型
	public Class getTargetClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}
}
