package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	/*
	 * 业务说明
	 * 校验用户是否存在
	 * http://sso.jt.com/user/check/{param}/{type}
	 * 返回值：SysResult
	 * 由于跨域请求，所以返回值必须特殊处理 callback（json）
	 */
	@RequestMapping("/user/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback) {
		JSONPObject object=null;
		try {
			boolean flag=userService.checkUser(param,type);
			object=new JSONPObject(callback, SysResult.ok(flag));
		} catch (Exception e) {
			e.printStackTrace();
			new JSONPObject(callback, SysResult.fail());
		}
		return object;
	}	
	
	//利用跨域实现用户信息回显
	@RequestMapping("/user/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
		//回传的数据需要经过200判断SysResult
		return new JSONPObject(callback, SysResult.fail());
		}else {
			return new JSONPObject(callback, SysResult.ok(userJSON));
		}
	}
	
	/*@RequestMapping("/user/register")
	public SysResult saveUser(String userJSON) {
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}*/
}
