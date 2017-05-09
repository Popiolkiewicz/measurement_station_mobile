package org.ms.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.ms.mobile.R;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-03 21:48.
 */
public class MainFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        if (getView() == null)
            return;
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.app_name_toolbar);
        initButton((Button) getActivity().findViewById(R.id.button1), 1);
        initButton((Button) getActivity().findViewById(R.id.button2), 2);
        initButton((Button) getActivity().findViewById(R.id.button3), 3);
        initButton((Button) getActivity().findViewById(R.id.button4), 4);
    }

    private void initButton(Button button, final int fragmentOrdinal) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(fragmentOrdinal);
            }
        });
    }

    private void switchFragment(int fragmentOrdinal) {
        Fragment fragment = null;
        switch (fragmentOrdinal) {
            case 1:
                fragment = new PreviewListFragment();
                break;
            case 2:
                fragment = new DeviceListFragment();
                break;
            case 3:
                fragment = new StatisticsFragment();
                break;
            case 4:
                fragment = new SettingsFragment();
                break;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("mainFragment")
                .commit();
    }
}
