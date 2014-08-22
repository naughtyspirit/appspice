package com.naughtyspirit.appspice.sampleproject.sampleproject;

import android.app.Activity;
import android.os.Bundle;

import com.naughtyspirit.appspice.client.AppSpice;

/**
 * Created by NaughtySpirit
 * Created on 22/Aug/2014
 */
public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        AppSpice.showAd(this);
    }
}
