package org.ms.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.ms.mobile.R;
import org.ms.mobile.connection.ConnectionConstants;
import org.ms.mobile.connection.OnResult;
import org.ms.mobile.connection.WebServiceTask;
import org.ms.mobile.conversion.Parser;
import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.ui.adapter.DeviceListAdapter;
import org.ms.mobile.ui.adapter.OnClick;
import org.ms.mobile.ui.util.SwipeContainerUtil;

import java.util.List;

/**
 * Created by Hubert on 2017-04-03.
 */

public class DeviceListFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
        initViews();
    }

    private void initViews() {
        if (getView() == null)
            return;
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("URZĄDZENIA");
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFragment(new AddDeviceFragment());
            }
        });
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        swipeContainer.setColorSchemeColors(SwipeContainerUtil.getColors());
    }

    private void fetchData() {
        new WebServiceTask(ConnectionConstants.DEVICE_GET_FOR_USER, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                dataFetched(result);
            }
        }).execute();
    }

    private void dataFetched(String result) {
        if(getView() == null)
            return;
        swipeContainer.setRefreshing(false);
        List<DeviceDTO> deviceDTOs = new Parser().getDeviceDTOs(result);
        System.out.println(deviceDTOs);

        ListView listView = (ListView) getView().findViewById(R.id.device_list);
        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(getContext(), deviceDTOs, new OnClick() {
            @Override
            public void execute(Object transferObject) {
                DeviceDTO chosenDTO = (DeviceDTO) transferObject;
                MeasurementTypeListFragment fragment = new MeasurementTypeListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("chosenDTO", chosenDTO);
                fragment.setArguments(bundle);
                runFragment(fragment);
            }
        });
        listView.setAdapter(deviceListAdapter);
    }

    private void runFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("deviceListFragment")
                .commit();
    }

}
