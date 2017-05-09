package org.ms.mobile.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ms.mobile.R;
import org.ms.mobile.sp.SPUtil;

/**
 * Created by Hubert on 2017-04-03.
 */
public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtil.loggedIn(getApplicationContext()))
            fakeBootDelay();
        else
            runActivity(StartActivity.class);
    }

    private void fakeBootDelay() {
        //Fake boot delay
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                runActivity(MainActivity.class);
            }
        }.execute();
    }

    private <T extends Activity> void runActivity(Class<T> activityClass) {
        Intent intent = new Intent(BootActivity.this, activityClass);
        startActivity(intent);
        finish();
    }
}