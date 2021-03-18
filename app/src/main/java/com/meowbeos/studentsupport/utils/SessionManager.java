package com.meowbeos.studentsupport.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Project name: StudentSupport
 * Created by MeowBeos
 * Date: 12/1/2021
 */

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREF_NAME = "StudentSupport";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String STUDENT_CODE = "Student_Code";
    public static final String DISPLAY_NAME = "Display_name";
    public static final String ID = "Id";

    public SessionManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void createLoginSession(int id, String student_code, String display_name) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(ID, id);
        editor.putString(STUDENT_CODE, student_code);
        editor.putString(DISPLAY_NAME, display_name);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(ID, String.valueOf(pref.getInt(ID, 0)));
        user.put(STUDENT_CODE, pref.getString(STUDENT_CODE, null));
        user.put(DISPLAY_NAME, pref.getString(DISPLAY_NAME, null));

        return user;
    }

    public void logoutUser() {

        pref.edit().remove(IS_LOGIN).apply();
        pref.edit().remove(ID).apply();
        pref.edit().remove(STUDENT_CODE).apply();
        pref.edit().remove(DISPLAY_NAME).apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
