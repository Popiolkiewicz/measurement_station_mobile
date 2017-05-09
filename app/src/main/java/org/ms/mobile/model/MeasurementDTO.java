package org.ms.mobile.model;

import java.util.Date;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-14 00:52.
 */
public class MeasurementDTO {

    private Double value;
    private Date date;
    private Boolean warning;

    private MeasurementTypeDTO measurementTypeDTO;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public MeasurementTypeDTO getMeasurementTypeDTO() {
        return measurementTypeDTO;
    }

    public void setMeasurementTypeDTO(MeasurementTypeDTO measurementTypeDTO) {
        this.measurementTypeDTO = measurementTypeDTO;
    }
}
