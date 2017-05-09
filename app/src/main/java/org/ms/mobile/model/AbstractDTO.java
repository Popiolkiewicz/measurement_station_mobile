package org.ms.mobile.model;

import java.io.Serializable;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-13 23:15.
 */
abstract class AbstractDTO implements Serializable {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
