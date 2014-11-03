package com.zc.lock;

public class BlockingArray<T> {
	private final T[] array;
	
	private int head;
	
	private int tail;
	
	private int count;
	
	public BlockingArray(int size){
		array = (T[])new Object[size];
	} 
	
	public synchronized void put(T item) throws InterruptedException {
		while(isFull()){
			wait();
		}
		
		array[tail] = item;
		if(++ tail == array.length){
			tail = 0;
		}
		count ++;
		System.out.println("Add item: " + item);
		// 通知条件队列有元素进入
		notifyAll();
	}
	
	public synchronized T take() throws InterruptedException {
		while(isEmpty()){
			wait();
		}
		
		T item = array[head];
		if(++ head == array.length){
			head = 0;
		}
		count --;
		System.out.println("Take item: " + item);
		// 通知条件队列有元素出去
		notifyAll();
		return item;
	}
	
	public synchronized boolean isFull(){
		return count == array.length;
	}
	
	public synchronized boolean isEmpty(){
		return count == 0;
	}
}
