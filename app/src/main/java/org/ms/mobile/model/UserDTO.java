package org.ms.mobile.model;

import java.util.List;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-12 19:10.
 */

public class UserDTO extends AbstractDTO {

    private String login;
    private String password;

    private List<DeviceDTO> deviceDTOs;

    public UserDTO() {

    }

    public UserDTO(Long userId) {
        this.id = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<DeviceDTO> getDeviceDTOs() {
        return deviceDTOs;
    }

    public void setDeviceDTOs(List<DeviceDTO> deviceDTOs) {
        this.deviceDTOs = deviceDTOs;
    }

}
