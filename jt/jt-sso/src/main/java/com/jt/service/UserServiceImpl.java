package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/*
	 * true表示用户已经存在，fabse表示用户可以使用
	 * 1.param用户参数
	 * 2.type 1.username 2.phone 3.email
	 * 
	 * 将type转换为具体字段
	 * 
	 */
	
	@Override
	public boolean checkUser(String param, Integer type) {
		String colum = type==1?"username":(type==2?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(colum, param);
		Integer count = userMapper.selectCount(queryWrapper);
		return count==0?false:true;
	}
	
	@Transactional
	@Override
	public void saveUser(User user) {
		user.setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());
		userMapper.insert(user);
		//int i=1/0;
	}



	
	
	
}
