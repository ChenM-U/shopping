package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;
	
	@RequestMapping("/show")
	public String findCartList(Model model) {
		//User user = (User) request.getAttribute("JT_USER");
		//Long userId = user.getId();
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList =cartService.findCartListByUser(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			Long userId = UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	@RequestMapping("/delete/{itemId}")
	public String deleCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.deleCart(cart);
			//重定向到购物车列表
			return "redirect:/cart/show.html";
	}
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//重定向到购物车列表
		return "redirect:/cart/show.html";
	}

}
