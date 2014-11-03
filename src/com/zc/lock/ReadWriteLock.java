package com.zc.lock;


public interface ReadWriteLock {
	
	public Lock readLock();
	
	public Lock writeLock();
	
}
