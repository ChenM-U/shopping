package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
	
	/**
	 * 如何存取多个数据？
	 * 用map<k,v> 结构
	 */
	private static ThreadLocal<User> thread=new ThreadLocal<User>();
	
	public static void set(User user) {
		thread.set(user);
	}
	
	public static User get() {
		return thread.get();
	}
	
	//关闭线程，防止内存泄露
	public static void remove() {
		thread.remove();
	}
}
