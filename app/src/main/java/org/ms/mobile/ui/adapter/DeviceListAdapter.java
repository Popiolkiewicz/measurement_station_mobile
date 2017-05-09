package org.ms.mobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ms.mobile.R;
import org.ms.mobile.model.DeviceDTO;

import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 19:37.
 */

public class DeviceListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    private List<DeviceDTO> deviceDTOs;
    private OnClick onClick;

    public DeviceListAdapter(Context context, List<DeviceDTO> deviceDTOs, OnClick onClick) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.deviceDTOs = deviceDTOs;
        this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return deviceDTOs.size();
    }

    @Override
    public DeviceDTO getItem(int position) {
        return deviceDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final DeviceDTO dto = getItem(position);
        view = inflater.inflate(R.layout.device_list_item, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.execute(dto);
            }
        });
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(dto.getName());
        textView2.setText("(Numer seryjny: " + dto.getSerialNumber() + ")");
        return view;
    }
}
