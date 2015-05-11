package com.xs.stocksettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class StockSettings extends miui.preference.PreferenceActivity{

    private static final String XS = SystemProperties.get("ro.product.mod_device");
    private EditTextPreference mDensity;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(miui.R.style.Theme_Light_Settings);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.stocksettings);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mDensity = (EditTextPreference) findPreference("density_key");

        mDensity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String NewDensity = (String) newValue;
                int i = Integer.parseInt(NewDensity);
                if (i < 300 || i > 600) {
                    Toast.makeText(getBaseContext(),R.string.density_error,Toast.LENGTH_LONG).show();
                    return false;
                }
                DialogReboot();
                RootCmd.RunRootCmd("setprop persist.xsdensity " + NewDensity + "");
                return true;
            }
        });

        //启动验证
        if (!this.XS.equals("bacon_xs")) {
            getPreferenceScreen().removePreference(findPreference("cm_settings"));
            getPreferenceScreen().removePreference(findPreference("about"));
            getPreferenceScreen().removePreference(findPreference("camera"));
            getPreferenceScreen().removePreference(findPreference("density_key"));
        }
    }

    public void DialogReboot() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_ok)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RootCmd.RunRootCmd("busybox killall system_server");
                    }
                })
                .setNeutralButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),R.string.dialog_reboot,Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
