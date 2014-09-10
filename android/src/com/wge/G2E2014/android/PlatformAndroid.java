package com.wge.G2E2014.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.wge.G2E2014.Helpers.Platform;

public class PlatformAndroid extends AndroidApplication implements Platform {
    private Activity activity;

    public PlatformAndroid(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void SetOrientation(String string) {
        if(string == "portrait")
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if(string == "landscape")
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public String GetOrientation() {
        if(activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            return "portrait";
        else if(activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            return "landscape";
        else
            return "";
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
