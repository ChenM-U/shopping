package com.jt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {
	
	@RequestMapping("/getMsg")
	public String testTomcats(){
		return "这是8093服务器";
	}
	
}
