package it.appspice.sampleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.appspice.android.AppSpice;

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
        AppSpice.showAd(this);

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
        AppSpice.onDestroy();
    }
}
