package com.zc.lock;

public class CAS {
	private int value;
	
	public synchronized int get(){
		return value;
	}
	
	public synchronized int compareAndSwap(int expected, int real){
		int oldValue = value;
		if(value == expected){
			value = real;
		}
		return oldValue;
	}
	
	public synchronized boolean compareAndSet(int expected, int real){
		return (expected == compareAndSwap(expected, real));
	}
}
