package com.zc.lock;

import java.util.concurrent.TimeUnit;

public interface TryLock extends Lock{
	//TimeUnit只支持毫秒
	public boolean trylock(long time, TimeUnit unit) throws InterruptedException;
}
