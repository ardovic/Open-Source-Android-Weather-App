package com.ardovic.weatherappprototype;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class DataReaderService extends IntentService {


    public DataReaderService() {
        super("data_reader_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
