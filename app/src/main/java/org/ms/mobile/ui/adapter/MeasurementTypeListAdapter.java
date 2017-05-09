package org.ms.mobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ms.mobile.R;
import org.ms.mobile.model.MeasurementTypeDTO;

import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 19:37.
 */

public class MeasurementTypeListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<MeasurementTypeDTO> measurementTypeDTOs;

    public MeasurementTypeListAdapter(Context context, List<MeasurementTypeDTO> measurementTypeDTOs) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.measurementTypeDTOs = measurementTypeDTOs;
    }

    @Override
    public int getCount() {
        return measurementTypeDTOs.size();
    }

    @Override
    public MeasurementTypeDTO getItem(int position) {
        return measurementTypeDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final MeasurementTypeDTO dto = getItem(position);
        view = inflater.inflate(R.layout.measurement_type_list_item, viewGroup, false);
        TextView nameTV = (TextView) view.findViewById(R.id.measurementTypeNameText);
        TextView codeTV = (TextView) view.findViewById(R.id.measurementTypeCodeText);
        TextView unitTV = (TextView) view.findViewById(R.id.measurementTypeUnitText);
        TextView boundsTV = (TextView) view.findViewById(R.id.measurementTypeBoundsText);
        nameTV.setText("Nazwa: " + dto.getName());
        codeTV.setText("Kod: " + dto.getCode());
        unitTV.setText("Jednostka: " + dto.getUnit());
        boundsTV.setText(("Zakres normy: " + dto.getBottomCriticalBound()
                + "-" + dto.getTopCriticalBound())
                .replace("null", "brak")
                .replace("null-null", "brak"));
        return view;
    }
}
