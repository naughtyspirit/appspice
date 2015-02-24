package it.appspice.sampleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import it.appspice.android.AppSpice;

class RateDialog {
    int appRun;
    String showLocation;
}

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/6/15.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSpice.init(this, SampleConstants.APP_SPICE_APP_ID);
//        Map<String, Object> data = new HashMap<>();
//        data.with("hi", "hello");
//
//        AppSpice.track(new Event("Sample", "AppStart", data));

        AppSpice.requestVariable("rateDialog", RateDialog.class);

        Button btn = (Button) findViewById(R.id.start_second_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });

//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                getSupportFragmentManager().get
//            }
//        });


//        Color newsletter_title_color = AppSpice.getColor("newsletter_title_color");

//        Field[] declaredFields = R.id.class.getDeclaredFields();

//        for (Field field : declaredFields) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                Log.d("Static field", field.getName());
//            }
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppSpice.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppSpice.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppSpice.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppSpice.onPause(this);
    }

    @Subscribe
    public void onRateDialogReceived(RateDialog rateDialog) {
        Log.d("AppSpice", rateDialog.showLocation);
    }

}
