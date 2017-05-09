package org.ms.mobile.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ms.mobile.model.UserDTO;

/**
 * Created by Hubert Popiołkiewicz on 2017-04-10 21:58.
 */

public class SPUtil {

    public static boolean loggedIn(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(SPConstants.IS_LOGGED_IN, false);
    }

    public static void clear(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void applyLoginData(Context context, UserDTO userDTO) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(SPConstants.USER_ID, userDTO.getId()).apply();
        sp.edit().putString(SPConstants.USER_LOGIN, userDTO.getLogin()).apply();
        sp.edit().putString(SPConstants.USER_PASSWORD, userDTO.getPassword()).apply();
        sp.edit().putBoolean(SPConstants.IS_LOGGED_IN, true).apply();
    }
}
