package com.prime.awitd.contactbook.setting;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by SantaClaus on 14/12/2016.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context p_context;

    // Shared Pref mode
    int PRIVATE_MODE = 0;

    // Shared Pref file name
    private static final String PREF_NAME = "prime-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this.p_context = context;
        pref = p_context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
