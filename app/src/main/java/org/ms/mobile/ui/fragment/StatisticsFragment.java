package org.ms.mobile.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.ms.mobile.R;
import org.ms.mobile.connection.ConnectionConstants;
import org.ms.mobile.connection.OnResult;
import org.ms.mobile.connection.WebServiceTask;
import org.ms.mobile.conversion.Parser;
import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.model.MeasurementDTO;
import org.ms.mobile.model.MeasurementTypeDTO;
import org.ms.mobile.ui.util.SwipeContainerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Hubert on 2017-04-03.
 */

public class StatisticsFragment extends Fragment {

    private static final String EMPTY_DEVICE_SELECTION = "Wybierz urządzenie";
    private static final String EMPTY_MEASUREMENT_TYPE_SELECTION = "Wybierz typ pomiaru";
    private static final int Y_SCALE_OFFSET = 5;

    private LineChart measurementChart;
    private SwipeRefreshLayout swipeContainer;
    private Description chartDescription;
    private Spinner deviceSpinner;
    private Spinner measurementTypeSpinner;

    private MeasurementTypeDTO selectedMeasurementTypeDTO;
    private Map<String, DeviceDTO> deviceDTOByName;
    private Map<String, MeasurementTypeDTO> measurementTypeDTOByName;
    private final String[] times = new String[100];
    private final String[] dates = new String[100];
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private float lowestMeasurementValue = Float.MAX_VALUE;
    private float highestMeasurementValue = 0.0f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistics_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchDevices();
        initViews();
    }

    private void initViews() {
        if (getView() == null)
            return;
        initToolbar();
        initChartDescription();
        initMeasurementChart();
        initXAxis();
        initSwipeContainer();
    }

    private void initToolbar() {
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("STATYSTYKI");
    }

    private void initChartDescription() {
        chartDescription = new Description();
        chartDescription.setText("");
        chartDescription.setTextSize(12);
        chartDescription.setTextColor(Color.WHITE);
    }

    private void initMeasurementChart() {
        measurementChart = (LineChart) getActivity().findViewById(R.id.chart);
        measurementChart.setNoDataTextColor(Color.WHITE);
        measurementChart.setNoDataText("Brak danych do wyświetlenia.");
        measurementChart.setDescription(chartDescription);
        measurementChart.getLegend().setEnabled(false);
        measurementChart.getAxisLeft().setTextColor(Color.WHITE);
        measurementChart.getAxisRight().setTextColor(Color.WHITE);
        measurementChart.setOnChartValueSelectedListener(new CustomValueSelectedListener());
    }

    private void initXAxis() {
        XAxis xAxis = measurementChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setLabelCount(15);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setValueFormatter(new CustomXAxisValueFormatter());
    }

    private void initSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDevices();
            }
        });
        swipeContainer.setColorSchemeColors(SwipeContainerUtil.getColors());
    }

    private void fetchDevices() {
        new WebServiceTask(ConnectionConstants.DEVICE_GET_FOR_PREVIEW, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                initDevicesSpinner(result);
            }
        }).execute();
    }

    private void initDevicesSpinner(String result) {
        if (getView() == null)
            return;
        swipeContainer.setRefreshing(false);
        List<DeviceDTO> deviceDTOs = new Parser().getDeviceDTOs(result);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getDeviceAdapterSource(deviceDTOs));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deviceSpinner = (Spinner) getActivity().findViewById(R.id.deviceSpinner);
        deviceSpinner.setAdapter(adapter);
        deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                String deviceName = (String) adapterView.getItemAtPosition(position);
                if (EMPTY_DEVICE_SELECTION.equals(deviceName)) {
                    clearMeasurementTypeSpinner();
                    clearMeasurementChart();
                    return;
                }
                DeviceDTO deviceDTO = deviceDTOByName.get(deviceName);
                initMeasurementTypeSpinner(deviceDTO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @NonNull
    private List<String> getDeviceAdapterSource(List<DeviceDTO> deviceDTOs) {
        List<String> deviceAdapterSource = new ArrayList<>();
        deviceAdapterSource.add(EMPTY_DEVICE_SELECTION);
        deviceDTOByName = new HashMap<>();
        for (DeviceDTO deviceDTO : deviceDTOs) {
            deviceAdapterSource.add(deviceDTO.getName());
            deviceDTOByName.put(deviceDTO.getName(), deviceDTO);
        }
        return deviceAdapterSource;
    }

    private void initMeasurementTypeSpinner(DeviceDTO deviceDTO) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getMeasurementAdapterSource(deviceDTO));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        measurementTypeSpinner = (Spinner) getActivity().findViewById(R.id.measurementTypeSpinner);
        measurementTypeSpinner.setAdapter(adapter);
        measurementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                String measurementTypeName = (String) adapterView.getItemAtPosition(position);
                clearMeasurementChart();
                if (EMPTY_MEASUREMENT_TYPE_SELECTION.equals(measurementTypeName))
                    return;
                selectedMeasurementTypeDTO = measurementTypeDTOByName.get(measurementTypeName);
                fetchMeasurements();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void clearMeasurementChart() {
        measurementChart.getAxisLeft().removeAllLimitLines();
        measurementChart.getAxisRight().removeAllLimitLines();
        measurementChart.clear();
        measurementChart.invalidate();
        lowestMeasurementValue = Float.MAX_VALUE;
        highestMeasurementValue = 0.0f;
    }

    private void clearMeasurementTypeSpinner() {
        if (measurementTypeSpinner != null)
            measurementTypeSpinner.setAdapter(null);
    }

    @NonNull
    private List<String> getMeasurementAdapterSource(DeviceDTO deviceDTO) {
        final List<String> measurementTypeAdapterSource = new ArrayList<>();
        measurementTypeAdapterSource.add(EMPTY_MEASUREMENT_TYPE_SELECTION);
        measurementTypeDTOByName = new HashMap<>();
        for (MeasurementTypeDTO measurementTypeDTO : deviceDTO.getMeasurementTypeDTOs()) {
            measurementTypeAdapterSource.add(measurementTypeDTO.getName());
            measurementTypeDTOByName.put(measurementTypeDTO.getName(), measurementTypeDTO);
        }
        return measurementTypeAdapterSource;
    }

    private void fetchMeasurements() {
        new WebServiceTask(ConnectionConstants.MEASUREMENT_GET_ALL, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                refreshChart(result);
            }
        }).showProgressDialog().execute(new Parser().toJSON(selectedMeasurementTypeDTO));
    }

    private void refreshChart(String result) {
        if (getView() == null)
            return;
        List<MeasurementDTO> measurementDTOs = new Parser().getMeasurementDTOs(result);
        if (measurementDTOs.size() == 0)
            return;

        List<Entry> entries = getEntries(measurementDTOs);
        LineDataSet lineDataSet = new LineDataSet(entries, selectedMeasurementTypeDTO.getUnit());
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(2);
        lineDataSet.setHighLightColor(Color.WHITE);

        setYTopScale();
        setYBottomScale();

        LineData lineData = new LineData(lineDataSet);
        lineData.notifyDataChanged();

        measurementChart.setData(lineData);
        measurementChart.notifyDataSetChanged();
        measurementChart.invalidate();
    }

    private void setYTopScale() {
        if (selectedMeasurementTypeDTO.getTopCriticalBound() != null) {
            float topCriticalBound = (float) (double) selectedMeasurementTypeDTO.getTopCriticalBound();
            measurementChart.getAxisLeft().addLimitLine(getLimitLine(topCriticalBound));
            measurementChart.getAxisLeft().setAxisMaximum(topCriticalBound + Y_SCALE_OFFSET);
            measurementChart.getAxisRight().setAxisMaximum(topCriticalBound + Y_SCALE_OFFSET);
        } else {
            measurementChart.getAxisLeft().setAxisMaximum(highestMeasurementValue + Y_SCALE_OFFSET);
            measurementChart.getAxisRight().setAxisMaximum(highestMeasurementValue + Y_SCALE_OFFSET);
        }
    }

    private void setYBottomScale() {
        if (selectedMeasurementTypeDTO.getBottomCriticalBound() != null) {
            float bottomCriticalBound = (float) (double) selectedMeasurementTypeDTO.getBottomCriticalBound();
            measurementChart.getAxisLeft().addLimitLine(getLimitLine(bottomCriticalBound));
            measurementChart.getAxisLeft().setAxisMinimum(bottomCriticalBound - Y_SCALE_OFFSET);
            measurementChart.getAxisRight().setAxisMinimum(bottomCriticalBound - Y_SCALE_OFFSET);
        } else {
            measurementChart.getAxisLeft().setAxisMinimum(lowestMeasurementValue - Y_SCALE_OFFSET);
            measurementChart.getAxisRight().setAxisMinimum(lowestMeasurementValue - Y_SCALE_OFFSET);
        }
    }

    @NonNull
    private LimitLine getLimitLine(float topCriticalBound) {
        LimitLine ll = new LimitLine(topCriticalBound);
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1);
        return ll;
    }

    @NonNull
    private List<Entry> getEntries(List<MeasurementDTO> measurementDTOs) {
        List<Entry> entries = new ArrayList<>();
        int iterationLimit = measurementDTOs.size() - 1;
        for (int i = iterationLimit; i >= 0; i--) {
            MeasurementDTO measurementDTO = measurementDTOs.get(iterationLimit - i);
            float value = (float) (double) measurementDTO.getValue();
            times[i] = timeFormat.format(measurementDTO.getDate());
            dates[i] = dateFormat.format(measurementDTO.getDate());
            entries.add(new Entry(i, value));
            highestMeasurementValue = value > highestMeasurementValue ? value : highestMeasurementValue;
            lowestMeasurementValue = value < lowestMeasurementValue ? value : lowestMeasurementValue;
        }
        Collections.sort(entries, new EntryXComparator());
        return entries;
    }

    private class CustomValueSelectedListener implements OnChartValueSelectedListener {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            String descriptionText = new StringBuilder()
                    .append("Czas: ")
                    .append(dates[(int) e.getX()])
                    .append(", pomiar: ")
                    .append(e.getY()).append(" ")
                    .append(selectedMeasurementTypeDTO.getUnit()).toString();
            chartDescription.setText(descriptionText);
            measurementChart.setDescription(chartDescription);
        }

        @Override
        public void onNothingSelected() {
            chartDescription.setText("");
            measurementChart.setDescription(chartDescription);
        }
    }

    private class CustomXAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return times[(int) value];
        }
    }
}