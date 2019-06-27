package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jt.enu.KEY_ENUM;

@Retention(RetentionPolicy.RUNTIME) //程序运行时有效
@Target(ElementType.METHOD)	//注解的作用范围
public @interface Cache_find {
	String key()		default "";						//接收用户key
	KEY_ENUM keyType() 	default KEY_ENUM.EMPTY;			//用户key类型
	int seconds()		default 0;					    //数据保存时间秒 默认永不过期
}
