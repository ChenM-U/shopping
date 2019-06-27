package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.dubbo.config.spring.util.ObjectUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;


@Component	//拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4的版本中，要求必须重写三个方法，不管是否有效
	 * 在spring5的版本中，在接口中添加default属性，则可以省略
	 */

	/**
	 * 	返回值结果：
	 * true：拦截放行
	 * false：请求拦截，重定向到登录页面
	 * 业务逻辑：
	 * 1.获取cookie数据
	 * 2.从cookie中获取token（TICKET）
	 * 3.判断redis缓存服务器中是否有数据
	 * 4.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token=null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token=cookie.getValue();
				break;
			}
		}
		//判断token是否有效
		if(!StringUtils.isEmpty(token)) {
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//将user对象保存到request域中
				//request.setAttribute("JT_USER",user);
				//用户每次操作都把数据储存到session，切记用完销毁
				//request.getSession().setAttribute("JT_USER",user);
				UserThreadLocal.set(user);
				return true;
			}
		}
		//重定向到用户的登录页面
		response.sendRedirect("/user/login.html");
		return false;//表示拦截
	}
	
	//销毁session
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//request.getSession().removeAttribute("JT_USER");
		UserThreadLocal.remove();
	}
}
