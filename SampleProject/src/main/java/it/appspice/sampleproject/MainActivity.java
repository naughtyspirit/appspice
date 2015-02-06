package it.appspice.sampleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.appspice.android.AppSpice;
import it.appspice.android.api.models.VariableProperties;
import it.appspice.android.listeners.OnVariablePropertiesListener;
import it.appspice.android.listeners.UserTrackingListener;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/6/15.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSpice.setUserTrackingPreferenceListener(new UserTrackingListener() {
            @Override
            public void onTrackingEnabled() {

            }

            @Override
            public void onTrackingDisabled() {

            }
        });
        AppSpice.init(this);
//        Map<String, Object> data = new HashMap<>();
//        data.put("hi", "hello");
//
//        AppSpice.track(new Event("Sample", "AppStart", data));

        AppSpice.getVariableProperties("newsTextView", new OnVariablePropertiesListener() {
            @Override
            public void onPropertiesReady(VariableProperties variableProperties) {

            }
        });

        Button btn = (Button) findViewById(R.id.start_second_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });



//        Color newsletter_title_color = AppSpice.getColor("newsletter_title_color");

//        Field[] declaredFields = R.id.class.getDeclaredFields();

//        for (Field field : declaredFields) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                Log.d("Static field", field.getName());
//            }
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
