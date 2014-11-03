package com.zc.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 读写锁的特点有两个：
 * 1. 当读锁和写锁上锁时，不允许有写者锁上锁
 * 2. 当写锁上锁时，不允许有读者锁和写者锁上锁
 * 也就是说写锁是排他锁，同时只能有一个写锁
 * 读锁是共享锁，可以有多个读锁存在
 * **/

public class SimpleReadWriteLock implements ReadWriteLock{

	private final java.util.concurrent.locks.Lock lock;
	
	private final java.util.concurrent.locks.Condition existReadCondition;
	
	private final java.util.concurrent.locks.Condition existWriteCondition;
	
	private final Lock readLock;
	
	private final Lock writeLock;
	
	private volatile boolean write;
	
	private volatile int readCount;
	
	public SimpleReadWriteLock(){
		lock = new ReentrantLock();
		readLock = new ReadLock();
		writeLock = new WriteLock();
		existReadCondition = lock.newCondition();
		existWriteCondition = lock.newCondition();
		write = false;
		readCount = 0;
	}
	
	private class ReadLock implements Lock{

		@Override
		public void lock() {
			lock.lock();
			try{
				while(write){
					try {
						existWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("Interrupted");
					}
				}
				readCount ++;
			}finally{
				lock.unlock();
			}
		}

		@Override
		public void unlock() {
			lock.lock();
			try{
				readCount --;
				if(readCount == 0){
					existReadCondition.signalAll();
				}
			}finally{
				lock.unlock();
			}
		}
		
	}
	
	private class WriteLock implements Lock{

		@Override
		public void lock() {
			lock.lock();
			try{
				while(readCount > 0){
					try {
						existReadCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("Interrupted");
					}
				}
				while(write){
					try {
						existWriteCondition.await();
					} catch (InterruptedException e) {
						throw new RuntimeException("Interrupted");
					}
				}
				write = true;
			}finally{
				lock.unlock();
			}
		}

		@Override
		public void unlock() {
			lock.lock();
			try{
				write = false;
				existWriteCondition.signalAll();
			}finally{
				lock.unlock();
			}
		}
		
	}
	
	@Override
	public Lock readLock() {
		return readLock;
	}

	@Override
	public Lock writeLock() {
		return writeLock;
	}

}
