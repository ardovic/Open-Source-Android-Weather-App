package com.ardovic.weatherappprototype.network;

import android.os.Handler;
import android.os.HandlerThread;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by user on 01.04.2018.
 */
public class FetchThreadDataTest {
    FetchThreadData<String> fetchThreadData;
    public FetchThreadDataTest() {
    }

    @Before
    public void setUp() throws Exception {
        fetchThreadData = new FetchThreadData<>(new Handler());
        fetchThreadData.start();
        fetchThreadData.getLooper();
    }

    @Test
    public void queueResponce() throws Exception {
        fetchThreadData.queueResponce("f", "a");
        fetchThreadData.queueResponce("c", "a");
        fetchThreadData.queueResponce("a", "a");
        fetchThreadData.queueResponce("d", "a");
    }

}