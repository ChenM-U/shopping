package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartListByUser(Long userId);

	void updateCartNum(Cart cart);

	void deleCart(Cart cart);

	void insertCart(Cart cart);
}
