package com.ardovic.weatherappprototype.network;

import com.ardovic.weatherappprototype.model.Weather;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 01.04.2018.
 *
 * @deprecated - используется retrofit
 */

public class WeatherDataPool extends ThreadPoolExecutor implements FetchThreadData.CallableTasker<Weather> {

    private boolean mShutdowned;

    public WeatherDataPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void shutdown() {
        mShutdowned = true;
        super.shutdown();
    }

    @Override
    public synchronized Future<Weather> setOtherTask(Callable<Weather> callable) {
        Future<Weather> future = null;
        if (callable != null && !mShutdowned) {
            future = submit(callable);
        }
        return future;
    }
}
