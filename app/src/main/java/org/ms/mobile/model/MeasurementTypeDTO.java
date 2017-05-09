package org.ms.mobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 23:00.
 */

public class MeasurementTypeDTO extends AbstractDTO {

    private String code;
    private String name;
    private String unit;
    private Double topCriticalBound;
    private Double bottomCriticalBound;

    private DeviceDTO deviceDTO;
    private MeasurementDTO measurementDTO;
    private List<MeasurementDTO> measurementDTOs = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getTopCriticalBound() {
        return topCriticalBound;
    }

    public void setTopCriticalBound(Double topCriticalBound) {
        this.topCriticalBound = topCriticalBound;
    }

    public Double getBottomCriticalBound() {
        return bottomCriticalBound;
    }

    public void setBottomCriticalBound(Double bottomCriticalBound) {
        this.bottomCriticalBound = bottomCriticalBound;
    }

    public DeviceDTO getDeviceDTO() {
        return deviceDTO;
    }

    public void setDeviceDTO(DeviceDTO deviceDTO) {
        this.deviceDTO = deviceDTO;
    }

    public MeasurementDTO getMeasurementDTO() {
        return measurementDTO;
    }

    public void setMeasurementDTO(MeasurementDTO measurementDTO) {
        this.measurementDTO = measurementDTO;
    }

    public List<MeasurementDTO> getMeasurementDTOs() {
        return measurementDTOs;
    }

    public void setMeasurementDTOs(List<MeasurementDTO> measurementDTOs) {
        this.measurementDTOs = measurementDTOs;
    }

}
