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
import org.ms.mobile.model.MeasurementTypeDTO;
import org.ms.mobile.ui.adapter.MeasurementTypeListAdapter;
import org.ms.mobile.ui.util.SwipeContainerUtil;

import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 21:59.
 */
public class MeasurementTypeListFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private DeviceDTO deviceDTO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.deviceDTO = (DeviceDTO) getArguments().getSerializable("chosenDTO");
        return inflater.inflate(R.layout.measurement_type_list_fragment, container, false);
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
        toolbarTitle.setText("TYPY POMIARÓW");
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeasurementTypeFragment fragment = new AddMeasurementTypeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("deviceDTO", deviceDTO);
                fragment.setArguments(bundle);
                runFragment(fragment);
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

    private void runFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("measurementTypeListFragment")
                .commit();
    }

    private void fetchData() {
        new WebServiceTask(ConnectionConstants.MEASUREMENT_TYPE_GET_FOR_DEVICE, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                dataFetched(result);
            }
        }).execute(new Parser().toJSON(deviceDTO));
    }

    private void dataFetched(String result) {
        if (getView() == null)
            return;
        swipeContainer.setRefreshing(false);
        List<MeasurementTypeDTO> measurementTypeDTOs = new Parser().getMeasurementTypeDTOs(result);
        ListView listView = (ListView) getView().findViewById(R.id.measurement_type_list);
        MeasurementTypeListAdapter deviceListAdapter = new MeasurementTypeListAdapter(getContext(), measurementTypeDTOs);
        listView.setAdapter(deviceListAdapter);
    }
}
