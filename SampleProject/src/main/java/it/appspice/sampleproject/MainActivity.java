package it.appspice.sampleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import it.appspice.android.AppSpice;
import it.appspice.android.api.models.Event;

/**
 * Author: Atanas Dimitrov <seishin90@yandex.ru>
 * Created on: 19/Aug/2014
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSpice.init(this);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("hi", "hello");

        AppSpice.track(new Event("Sample", "AppStart", data));

        Button btn = (Button) findViewById(R.id.start_second_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
