package com.jt;

public interface test {
	static boolean f(char c) {
		System.out.println(c);
		return true;
	}
	public static void main(String[] args) {
		f('A');
		System.out.println(123);
	}
}
