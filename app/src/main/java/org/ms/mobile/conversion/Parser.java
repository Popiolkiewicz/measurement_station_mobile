package org.ms.mobile.conversion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.ms.mobile.model.DeviceDTO;
import org.ms.mobile.model.MeasurementDTO;
import org.ms.mobile.model.MeasurementTypeDTO;
import org.ms.mobile.model.UserDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hubert on 2017-04-03.
 */
public class Parser {

    private Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .create();
    }

    public String toJSON(Object userRequestDTO) {
        return gson.toJson(userRequestDTO);
    }

    public UserDTO getLoginRequestDTO(String json) {
        try {
            return gson.fromJson(json, new TypeToken<UserDTO>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new UserDTO();
        }
    }

    public List<DeviceDTO> getDeviceDTOs(String json) {
        try {
            return gson.fromJson(json, new TypeToken<ArrayList<DeviceDTO>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<MeasurementTypeDTO> getMeasurementTypeDTOs(String json) {
        try {
            return gson.fromJson(json, new TypeToken<ArrayList<MeasurementTypeDTO>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<MeasurementDTO> getMeasurementDTOs(String json) {
        try {
            return gson.fromJson(json, new TypeToken<ArrayList<MeasurementDTO>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
