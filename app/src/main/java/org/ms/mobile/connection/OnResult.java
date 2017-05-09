package org.ms.mobile.connection;

import android.util.Log;

/**
 * Created by Hubert on 2017-04-12.
 */
public abstract class OnResult {

    public abstract void onSuccess(String result);

    public void onFailure(String result) {
        Log.e("ERROR", result);
    }

}
