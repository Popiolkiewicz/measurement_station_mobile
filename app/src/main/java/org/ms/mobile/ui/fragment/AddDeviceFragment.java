package org.ms.mobile.ui.fragment;

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

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 19:02.
 */

public class AddDeviceFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_device_fragment, container, false);
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
        toolbarTitle.setText("DODAJ URZĄDZENIE");
        final EditText deviceNameField = (EditText) getView().findViewById(R.id.deviceNameField);
        final EditText deviceSerialNumberField = (EditText) getView().findViewById(R.id.deviceSerialNumberField);
        final Button addDeviceButton = (Button) getView().findViewById(R.id.addDeviceButton);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(deviceNameField.getText().toString(),
                        deviceSerialNumberField.getText().toString());
            }
        });
    }

    private void add(String name, String serialNumber) {
        if (name.isEmpty() || serialNumber.isEmpty()) {
            Toast.makeText(getContext(), "Nie uzupełniono wszystkich pól!", Toast.LENGTH_SHORT).show();
            return;
        }
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setSerialNumber(serialNumber);
        deviceDTO.setName(name);
        new WebServiceTask(ConnectionConstants.DEVICE_ADD, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                addSuccess(result);
            }
        }).showProgressDialog().execute(new Parser().toJSON(deviceDTO));
    }

    private void addSuccess(String result) {
        System.out.println(result);
        Toast.makeText(getContext(), getString(R.string.add_device_success), Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }
}
