package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

//编辑工具类，实现对象与json转换
public class ObjectMapperUtil {
	
	private static final ObjectMapper mapper=new ObjectMapper();
	
	public static String toJSON(Object target) {
		String json=null;
		try {
			json= mapper.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查的异常转化为运行时异常
			throw new RuntimeException();
		}
		return json;
	}
	public static <T> T toObject(String json,Class<T> targetClass){
		T target=null;
		try {
			target = mapper.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查的异常转化为运行时异常
			throw new RuntimeException();
		}
		return target;
	}

}
