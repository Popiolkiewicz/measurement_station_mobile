package org.ms.mobile.ui.util;

import android.app.Activity;
import android.content.Context;

import org.ms.mobile.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-12 23:12.
 */

public class ProgressDialogUtil {

    private static SpotsDialog progressDialog;

    private ProgressDialogUtil() {

    }

    public static void show(final Context context) {
        if (context == null)
            return;
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                progressDialog = new SpotsDialog(context, R.style.CustomProgressDialog);
                progressDialog.setMessage(context.getResources().getString(R.string.wait));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    public static void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
