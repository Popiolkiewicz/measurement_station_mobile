package org.ms.mobile.ui.fragment;

import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ms.mobile.R;
import org.ms.mobile.connection.ConnectionConstants;
import org.ms.mobile.connection.OnResult;
import org.ms.mobile.connection.WebServiceTask;
import org.ms.mobile.conversion.Parser;
import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.model.MeasurementTypeDTO;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 19:02.
 */

public class AddMeasurementTypeFragment extends Fragment {

    private DeviceDTO deviceDTO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.deviceDTO = (DeviceDTO) getArguments().getSerializable("deviceDTO");
        return inflater.inflate(R.layout.add_measurement_type_fragment, container, false);
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
        toolbarTitle.setText(R.string.add_mesurement_type_toolbar_title);
        final EditText nameField = (EditText) getView().findViewById(R.id.measurementTypeNameField);
        final EditText codeField = (EditText) getView().findViewById(R.id.measurementTypeCodeField);
        final EditText unitField = (EditText) getView().findViewById(R.id.measurementTypeUnitField);
        final EditText bottomCBField = (EditText) getView().findViewById(R.id.measurementBottomCriticalBoundField);
        final EditText topCBField = (EditText) getView().findViewById(R.id.measurementTopCriticalBoundField);
        final Button addButton = (Button) getView().findViewById(R.id.addMeasurementTypeButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(nameField.getText().toString(),
                        codeField.getText().toString(),
                        unitField.getText().toString(),
                        bottomCBField.getText().toString(),
                        topCBField.getText().toString());
            }
        });
    }

    private void add(String name, String code, String unit, String bottomCB, String topCB) {
        if (name.isEmpty() || code.isEmpty() || unit.isEmpty()) {
            Toast.makeText(getContext(), R.string.lack_of_input_data_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Double bottomCBDouble = null;
        Double topCBDouble = null;
        try {
            if (!bottomCB.isEmpty())
                bottomCBDouble = Double.parseDouble(bottomCB);
            if (!topCB.isEmpty())
                topCBDouble = Double.parseDouble(topCB);
        } catch (ParseException ex) {
            Toast.makeText(getContext(), R.string.invalid_bound_values_input, Toast.LENGTH_SHORT).show();
            return;
        }

        MeasurementTypeDTO dto = new MeasurementTypeDTO();
        dto.setName(name);
        dto.setCode(code);
        dto.setUnit(unit);
        dto.setBottomCriticalBound(bottomCBDouble);
        dto.setTopCriticalBound(topCBDouble);
        dto.setDeviceDTO(deviceDTO);
        new WebServiceTask(ConnectionConstants.MEASUREMENT_TYPE_ADD, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                addSuccess(result);
            }
        }).showProgressDialog().execute(new Parser().toJSON(dto));
    }

    private void addSuccess(String result) {
        System.out.println(result);
        Toast.makeText(getContext(), getString(R.string.add_measurement_type_success), Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }
}
