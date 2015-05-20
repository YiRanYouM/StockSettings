package com.xs.stocksettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import miui.preference.PreferenceActivity;

public class StockSettings extends miui.preference.PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String XS = SystemProperties.get("ro.product.mod_device");
    private static final String KEYHOMEDOUBLETAPACTION = "key_home_double_tap_action";
    private static final String CAMERASWITCH = "camera_switch_key";
    private EditTextPreference mDensity;
    private CheckBoxPreference mKeyHomeDoubleTapAction;
    private ListPreference mCameraSwitch;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(miui.R.style.Theme_Light_Settings);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.stocksettings);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mDensity = (EditTextPreference) findPreference("density_key");
        mCameraSwitch = (ListPreference) findPreference(CAMERASWITCH);
        mKeyHomeDoubleTapAction = (CheckBoxPreference) findPreference(KEYHOMEDOUBLETAPACTION);

        mCameraSwitch.setOnPreferenceChangeListener(this);
        setListPreferenceSummary(mCameraSwitch);

        mDensity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String NewDensity = (String) newValue;
                if (NewDensity.equals("")) {
                    Toast.makeText(getBaseContext(),R.string.density_error,Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    int i = Integer.parseInt(NewDensity);
                    if (i < 300 || i > 600) {
                        Toast.makeText(getBaseContext(), R.string.density_error, Toast.LENGTH_LONG).show();
                        return false;
                    }
                    DialogReboot();
                    RootCmd.RunRootCmd("setprop persist.xsdensity " + NewDensity + "");
                    return true;
                }
            }
        });

        //启动验证
        if (!this.XS.equals("bacon_xs")) {
            getPreferenceScreen().removeAll();
        }
    }

    public boolean onPreferenceTreeClick (PreferenceScreen preferencescreen, Preference preference) {
        if (preference == mKeyHomeDoubleTapAction) {
            if(mKeyHomeDoubleTapAction.isChecked()){
                Settings.System.putInt(getContentResolver(),KEYHOMEDOUBLETAPACTION,8);
            } else {
                Settings.System.putInt(getContentResolver(),KEYHOMEDOUBLETAPACTION,0);
            }
        }
        return false;
    }

    public void setListPreferenceSummary(ListPreference mListPreference) {
        if (mListPreference == mCameraSwitch) {
            if (0 == Integer.parseInt(mListPreference.getValue())){
                mListPreference.setSummary(R.string.camera_switch_color_summary);
            } else if (1 == Integer.parseInt(mListPreference.getValue())){
                mListPreference.setSummary(R.string.camera_switch_miui_summary);
            } else if (2 == Integer.parseInt(mListPreference.getValue())){
                mListPreference.setSummary(R.string.camera_switch_cm_summary);
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mCameraSwitch == preference) {
            String ValueCameraSwitch = (String) newValue;
            mCameraSwitch.setValue(ValueCameraSwitch);
            int mode = Integer.parseInt(ValueCameraSwitch);
            switch (mode) {
                case 0:
                    preference.setSummary(R.string.camera_switch_color_summary);
                    RootCmd.RunRootCmd("mount -o remount,rw /system");
                    RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
                    RootCmd.RunRootCmd("cp -f /system/stocksettings/ColorCameraMod.apk /system/app/Camera.apk");
                    RootCmd.RunRootCmd("chmod 0644 /system/app/Camera.apk");
                    break;
                case 1:
                    preference.setSummary(R.string.camera_switch_miui_summary);
                    RootCmd.RunRootCmd("mount -o remount,rw /system");
                    RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
                    RootCmd.RunRootCmd("cp -f /system/stocksettings/MiuiCamera.apk /system/app/Camera.apk");
                    RootCmd.RunRootCmd("chmod 0644 /system/app/Camera.apk");
                    break;
                case 2:
                    preference.setSummary(R.string.camera_switch_cm_summary);
                    RootCmd.RunRootCmd("mount -o remount,rw /system");
                    RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
                    RootCmd.RunRootCmd("cp -f /system/stocksettings/CyanogenModCamera.apk /system/app/Camera.apk");
                    RootCmd.RunRootCmd("chmod 0644 /system/app/Camera.apk");
                    break;
                default:
                    break;
            }
        }
        return true;
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
