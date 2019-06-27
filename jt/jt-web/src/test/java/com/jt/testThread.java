package com.jt;

import org.junit.Test;

public class testThread {
    
	static int x = 0, y = 0;
   
    public static void writer() {
        x = 1;
        y = 2;
    }
    //@Test
    public static void reader() {
    	//int x = 0, y = 0;
        int r1 = y;
        int r2 = x;
        System.out.println(r2);
        System.out.println(r1);
    }
    public static void main(String[] args) {
    	for(int i=1;;i++) {
    	reader();
    	}
	}
}
