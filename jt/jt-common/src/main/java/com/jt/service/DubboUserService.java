package com.jt.service;

import com.jt.pojo.User;

public interface DubboUserService {
	//Dubbo接口实现用户新增
	void saveUser(User user);

	String findUserByUP(User user);

}
