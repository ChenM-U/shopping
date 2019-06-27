package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	//导入dubbo的用户接口
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;
	//	@Autowired
	//	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	@RequestMapping("/{modelName}")
	public String index(@PathVariable String modelName) {
		return modelName;
	}

	//使用Dubbo进行业务调用
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	//实现用户的登录
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		try {
			String token=userService.findUserByUP(user);
			//判断数据是否正确
			//如果不为空，将数据保存到cookie中
			//cookie中的key是固定值 JT_COOKIE
			if(!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setDomain("jt.com");
				cookie.setMaxAge(7*24*60*60);
				cookie.setPath("/");//cookie的权限
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}


	/*
	 * 实现用户登出操作
	 * 1.删除redis
	 * 2.删除cookie
	 */
	@RequestMapping("/logout")
	public String  Logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if(cookies.length!=0) {
			String token = null;
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token=cookie.getValue();
					break;
				}
			}
			//判断token是否有值，删除redis.cookie
			if(!StringUtils.isEmpty(token)) {
				jedisCluster.del(token);
				Cookie cookie = new Cookie("JT_TICKET", "");
				cookie.setMaxAge(0);//删除cookie
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				response.addCookie(cookie);
			}
		}
		//重定向到首页
		return "redirect:/";
	}
}
