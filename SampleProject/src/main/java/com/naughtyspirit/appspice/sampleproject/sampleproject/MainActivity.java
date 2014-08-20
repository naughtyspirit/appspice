package com.naughtyspirit.appspice.sampleproject.sampleproject;

import android.app.Activity;
import android.os.Bundle;

import com.naughtyspirit.appspice.client.Appspice;

/**
 * Author: Atanas Dimitrov <seishin90@yandex.ru>
 * Created on: 19/Aug/2014
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appspice appspice = Appspice.getInstance(this);
        appspice.init("", "");
    }
}
