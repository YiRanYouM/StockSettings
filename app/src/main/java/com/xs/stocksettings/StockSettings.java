package com.xs.stocksettings;

import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.PreferenceActivity;

public class StockSettings extends PreferenceActivity {

    private static final String XS = SystemProperties.get("ro.product.mod_device");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.stocksettings);

        //启动验证
        if (!this.XS.equals("bacon_xs")) {
            getPreferenceScreen().removePreference(findPreference("cm_settings"));
            getPreferenceScreen().removePreference(findPreference("about"));
            getPreferenceScreen().removePreference(findPreference("camera"));
        }
    }

}
