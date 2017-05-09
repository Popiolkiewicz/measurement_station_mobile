package org.ms.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.ms.mobile.R;
import org.ms.mobile.sp.SPUtil;
import org.ms.mobile.ui.fragment.MainFragment;

/**
 * Created by Hubert on 2017-04-03.
 */
public class MainActivity extends AppCompatActivity {

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
                powerOffClickEvent();
            }
        });
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, new MainFragment())
                .commit();
    }

    private void powerOffClickEvent() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogLayout)
                .create();
        dialogLayout.findViewById(R.id.yes_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SPUtil.clear(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogLayout.findViewById(R.id.no_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
