package com.elzhao.library;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Method;

public class Reflect {

    private static final String TAG = "Reflect";

    public static Application getCurApplication() {
        Application application = null;
        Class cls;
        try {
            cls = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = cls.getDeclaredMethod("currentApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
            Log.d(TAG, "ActivityThread.currentApplication:" + application);
        } catch (Exception e) {
            Log.d(TAG, "e:" + e.toString());
        }
        if (application != null) {
            return application;
        }
        try {
            cls = Class.forName("android.app.AppGlobals");
            Method currentApplicationMethod = cls.getDeclaredMethod("getInitialApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
            Log.d(TAG, "AppGlobals.getInitialApplication:" + application);
        } catch (Exception e) {
            Log.d(TAG, "e:" + e.toString());
        }
        return application;
    }
}

