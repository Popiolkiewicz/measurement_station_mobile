package org.ms.mobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 19:20.
 */

public class DeviceDTO extends AbstractDTO {

    private String name;
    private String serialNumber;
    private Boolean warning;

    private UserDTO userDTO;
    private List<MeasurementTypeDTO> measurementTypeDTOs = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<MeasurementTypeDTO> getMeasurementTypeDTOs() {
        return measurementTypeDTOs;
    }

    public void setMeasurementTypeDTOs(List<MeasurementTypeDTO> measurementTypeDTOs) {
        this.measurementTypeDTOs = measurementTypeDTOs;
    }

}
