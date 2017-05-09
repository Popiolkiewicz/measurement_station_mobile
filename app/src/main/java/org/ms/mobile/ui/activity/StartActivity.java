package org.ms.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.ms.mobile.R;
import org.ms.mobile.ui.fragment.RegisterLoginFragment;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-12 19:35.
 */

public class StartActivity extends AppCompatActivity {


    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        LinearLayout powerOff = (LinearLayout) findViewById(R.id.powerOffLayout);
        powerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, new RegisterLoginFragment())
                .commit();
    }
}
