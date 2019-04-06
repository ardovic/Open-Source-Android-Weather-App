package com.ardovic.weatherappprototype.network;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.Log;

import com.ardovic.weatherappprototype.model.Weather;

import java.lang.ref.WeakReference;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 01.04.2018.
 *
 * @deprecated - используется retrofit
 */

public class FetchThreadData<T> extends HandlerThread {

    private boolean mQuit;
    private HandlerRequest<T> mHandlerAnswer;
    private Handler mMainThreadCallback;
    private ConcurrentMap<T, String> mConcurrentMap;
    private static final int MESSAGE_DOWNLOAD = 314;
    private static final String TAG = "FetchThreadData";
    private WeatherFetchListener<T> mWeatherFetchListener;
    private CallableTasker<Weather> mTasker;
    private ArrayBlockingQueue<Runnable> mArrayBlockingQueue = new ArrayBlockingQueue<>(10, true);
    WeatherDataPool mDataPool;

    public void setWeatherFetchListener(WeatherFetchListener weatherFetchListener) {
        mWeatherFetchListener = weatherFetchListener;
    }

    public FetchThreadData(Handler handler) {
        super(TAG);
        int cores = Runtime.getRuntime().availableProcessors();
        mDataPool = new WeatherDataPool(cores, cores * 2 + 1,
                5, TimeUnit.SECONDS, mArrayBlockingQueue);
            mTasker = (CallableTasker) mDataPool;
            mMainThreadCallback = handler;

    }

    @Override
    protected void onLooperPrepared() {
        mConcurrentMap = new ConcurrentHashMap<>();
        mDataPool.prestartCoreThread();
        mHandlerAnswer = new HandlerRequest<>(this, Looper.myLooper());
    }

    @Override
    public boolean quit() {
        mQuit = true;
        mDataPool.shutdown();
        return super.quit();
    }

    public void clearQueue() {
        mHandlerAnswer.removeMessages(MESSAGE_DOWNLOAD);
        mArrayBlockingQueue.clear();
        mConcurrentMap.clear();
    }

    private static class HandlerRequest<Z> extends Handler {

        WeakReference<FetchThreadData> mThreadData;

        HandlerRequest(FetchThreadData data, Looper looper) {
            super(looper);
            mThreadData = new WeakReference<>(data);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_DOWNLOAD) {
                Z z = (Z) msg.obj;
                mThreadData.get().resultFromServer(z);
                Log.d(TAG, "handleMessage: " + z.toString());
            }
        }
    }

    public void queueResponce(T t, String s) {
        if (s != null && !mConcurrentMap.containsValue(s)) {
                mConcurrentMap.put(t, s);
                mHandlerAnswer.obtainMessage(MESSAGE_DOWNLOAD, t).sendToTarget();
        } else {
            mConcurrentMap.remove(t);
            Log.d(TAG, "queueResponce: request is in queue");
        }
    }
    private void resultFromServer(final T t) {
        final String loc = mConcurrentMap.get(t);
        Weather weather = null;
        Future<Weather> f = mTasker.setOtherTask(new WeatherTaskPool(loc));
        try {
            Log.d(TAG, "resultFromServer: " + f.get());
            weather = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final Weather finalWeather = weather;
        mMainThreadCallback.post(new Runnable() {
            @Override
            public void run() {
                if (mQuit || !mConcurrentMap.get(t).equals(loc)) {
                    return;
                }
                if (finalWeather != null) {
                    mConcurrentMap.remove(t);
                    mWeatherFetchListener.onDataFetched(t, finalWeather);
                } else {
                    mConcurrentMap.remove(t);
                    mWeatherFetchListener.onDataFetched(t, null);
                }
            }
        });
    }
    public interface WeatherFetchListener<T> {
        void onDataFetched(T t, @Nullable Weather s);
    }
    protected interface CallableTasker<V> {
        Future<V> setOtherTask(Callable<V> callable);
    }
}