package org.ms.mobile;

import android.app.Application;
import android.content.Context;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-12 23:47.
 */

public class MSM extends Application {

    private static MSM msm;

    public static Context getAppContext() {
        return msm.getApplicationContext();
    }

    public void onCreate() {
        super.onCreate();
        msm = this;
    }
}
