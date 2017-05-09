package org.ms.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.ms.mobile.R;
import org.ms.mobile.connection.ConnectionConstants;
import org.ms.mobile.connection.OnResult;
import org.ms.mobile.connection.WebServiceTask;
import org.ms.mobile.conversion.Parser;
import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.ui.adapter.PreviewListAdapter;
import org.ms.mobile.ui.util.SwipeContainerUtil;

import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-03 21:47.
 */

public class PreviewListFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        fetchData();
    }

    private void initViews() {
        if (getView() == null)
            return;
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("PODGLĄD");
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
        new WebServiceTask(ConnectionConstants.DEVICE_GET_FOR_PREVIEW, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                dataFetched(result);
            }
        }).execute();
    }

    private void dataFetched(String result) {
        if (getView() == null)
            return;
        swipeContainer.setRefreshing(false);
        System.out.println(result);
        List<DeviceDTO> deviceDTOs = new Parser().getDeviceDTOs(result);
        PreviewListAdapter previewListAdapter = new PreviewListAdapter(getContext(), deviceDTOs);
        ExpandableListView listView = (ExpandableListView) getView().findViewById(R.id.preview_list);
        listView.setAdapter(previewListAdapter);
        for (int i = 0; i < previewListAdapter.getGroupCount(); i++)
            listView.expandGroup(i);
    }
}
