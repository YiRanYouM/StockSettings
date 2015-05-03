package com.xs.stocksettings;

import android.os.Bundle;
import android.os.SystemProperties;

public class StockSettings extends miui.preference.PreferenceActivity {

    private static final String XS = SystemProperties.get("ro.product.mod_device");

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(miui.R.style.Theme_Light_Settings);
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
