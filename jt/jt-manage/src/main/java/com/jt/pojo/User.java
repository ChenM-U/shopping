package com.jt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
//当程序转换对象时，如果遇到未知属性，则忽略
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	
	
}
