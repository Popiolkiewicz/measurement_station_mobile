package org.ms.mobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ms.mobile.R;
import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.model.MeasurementDTO;
import org.ms.mobile.model.MeasurementTypeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-14 00:44.
 */

public class PreviewListAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private List<DeviceDTO> dataSource = new ArrayList<>();

    public PreviewListAdapter(Context context, List<DeviceDTO> deviceDTOs) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataSource = deviceDTOs;
    }

    @Override
    public int getGroupCount() {
        return dataSource.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<MeasurementTypeDTO> children = dataSource.get(groupPosition).getMeasurementTypeDTOs();
        if (children == null)
            return 0;
        return children.size();
    }

    @Override
    public DeviceDTO getGroup(int groupPosition) {
        return dataSource.get(groupPosition);
    }

    @Override
    public MeasurementTypeDTO getChild(int groupPosition, int childPosition) {
        DeviceDTO newsCategoryDTO = dataSource.get(groupPosition);
        List<MeasurementTypeDTO> children = newsCategoryDTO.getMeasurementTypeDTOs();
        return children != null ? children.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        final DeviceDTO deviceDTO = getGroup(groupPosition);
        view = inflater.inflate(R.layout.preview_list_item_parent, viewGroup, false);
        ImageView okIV = (ImageView) view.findViewById(R.id.okStateIcon);
        ImageView warningIV = (ImageView) view.findViewById(R.id.warningStateIcon);
        if (deviceDTO.getWarning()) {
            okIV.setVisibility(View.INVISIBLE);
            warningIV.setVisibility(View.VISIBLE);
        }
        TextView deviceNameTV = (TextView) view.findViewById(R.id.deviceNameText);
        deviceNameTV.setText(deviceDTO.getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        MeasurementTypeDTO mtDTO = getChild(i, i1);
        MeasurementDTO mDTO = mtDTO.getMeasurementDTO();

        view = inflater.inflate(R.layout.preview_list_item_child, viewGroup, false);
        ImageView okIV = (ImageView) view.findViewById(R.id.okStateIcon);
        ImageView warningIV = (ImageView) view.findViewById(R.id.warningStateIcon);
        TextView typeNameTV = (TextView) view.findViewById(R.id.measurementTypeNameText);
        TextView typeBoundTV = (TextView) view.findViewById(R.id.measurementTypeBoundsText);
        TextView valueTV = (TextView) view.findViewById(R.id.measurementValue);

        if (mDTO == null || mDTO.getWarning()) {
            okIV.setVisibility(View.INVISIBLE);
            warningIV.setVisibility(View.VISIBLE);
        }
        typeNameTV.setText(mtDTO.getName());
        typeBoundTV.setText(("Zakres normy: " + mtDTO.getBottomCriticalBound() + "-" + mtDTO.getTopCriticalBound())
                .replace("null", "brak")
                .replace("null-null", "brak"));
        valueTV.setText(mDTO != null ? mDTO.getValue() + " " + mtDTO.getUnit() : "-");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
