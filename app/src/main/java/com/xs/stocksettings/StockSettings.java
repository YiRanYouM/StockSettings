package com.xs.stocksettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import android.preference.EditTextPreference;

import miui.preference.PreferenceActivity;

public class StockSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String XS = SystemProperties.get("ro.product.mod_device");
    private static final String SOUND = "sound_patch_key";
    private static final String SYSTEMUI = "systemui_style_key";
    private static final String STORAGE = "storage_key";

    private Preference mSound;
    private ListPreference mSystemUI;
    private ListPreference mStorage;
    private EditTextPreference mDensity;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(miui.R.style.Theme_Light_Settings);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.stocksettings);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mSound = (Preference) findPreference(SOUND);
        mDensity = (EditTextPreference) findPreference("density_key");
        mSystemUI = (ListPreference) findPreference(SYSTEMUI);
        mStorage = (ListPreference) findPreference(STORAGE);
        mSystemUI.setOnPreferenceChangeListener(this);
        mStorage.setOnPreferenceChangeListener(this);
        setListPreferenceSummary(mSystemUI);
        setListPreferenceSummary(mStorage);

        mDensity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String NewDensity = (String) newValue;
                if (NewDensity.equals("")) {
                    Toast.makeText(getBaseContext(), R.string.density_error, Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    int i = Integer.parseInt(NewDensity);
                    if (i < 280 || i > 320) {
                        Toast.makeText(getBaseContext(), R.string.density_error, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                DialogReboot();
                RootCmd.RunRootCmd("setprop persist.xsdensity " + NewDensity + "");
                return true;
            }
        });

        if (!this.XS.equals("8297_xs")) {
            getPreferenceScreen().removeAll();
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen,
                                         Preference preference) {

        if (preference == mSound) {
            RootCmd.RunRootCmd("mount -o remount,rw /data");
            RootCmd.RunRootCmd("cp -r /system/stocksettings/Audio_ver1_Vol_custom /data/nvram/APCFG/APRDCL/Audio_ver1_Vol_custom");
            Toast.makeText(this, R.string.sound_patch_toast, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void setListPreferenceSummary(ListPreference mListPreference) {
        if (mListPreference == mSystemUI) {
            if (0 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.systemui_default);
            } else {
                mListPreference.setSummary(R.string.systemui_diy);
            }
        }
        if (mListPreference == mStorage) {
            if (0 == Integer.parseInt(mListPreference.getValue())) {
                mListPreference.setSummary(R.string.storage_internal);
            } else {
                mListPreference.setSummary(R.string.storage_external);
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mSystemUI == preference) {
            String prefsValueSystemUI = (String) newValue;
            mSystemUI.setValue(prefsValueSystemUI);
            int mode = Integer.parseInt(prefsValueSystemUI);
            switch (mode) {
                case 0:
                    preference.setSummary(R.string.systemui_default);
                    RootCmd.RunRootCmd("mount -o remount,rw /data");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/systemui/default/statusbar_clock.maml /data/data/com.android.systemui/files/statusbar_clock.maml");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/systemui/default/statusbar_music.maml /data/data/com.android.systemui/files/statusbar_music.maml");
                    Toast.makeText(this, R.string.replace_toast, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    preference.setSummary(R.string.systemui_diy);
                    RootCmd.RunRootCmd("mount -o remount,rw /data");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/systemui/diy/statusbar_clock.maml /data/data/com.android.systemui/files/statusbar_clock.maml");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/systemui/diy/statusbar_music.maml /data/data/com.android.systemui/files/statusbar_music.maml");
                    Toast.makeText(this, R.string.replace_toast, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
        if (mStorage == preference) {
            String prefsValueStorage = (String) newValue;
            mStorage.setValue(prefsValueStorage);
            int mode = Integer.parseInt(prefsValueStorage);
            switch (mode) {
                case 0:
                    preference.setSummary(R.string.storage_internal);
                    RootCmd.RunRootCmd("mount -o remount,rw /system");
                    RootCmd.RunRootCmd("rm -rf /system/bin/sdcard");
                    RootCmd.RunRootCmd("rm -rf /system/bin/vold");
                    RootCmd.RunRootCmd("rm -rf /system/etc/vold.fstab");
                    RootCmd.RunRootCmd("rm -rf /system/etc/vold.fstab.nand");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/internal/sdcard /system/bin/sdcard");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/internal/vold /system/bin/vold");
                    RootCmd.RunRootCmd("chmod 0755 /system/bin/sdcard");
                    RootCmd.RunRootCmd("chmod 0755 /system/bin/vold");
                    RootCmd.RunRootCmd("dd if=/system/stocksettings/internal/boot.img of=/dev/bootimg");
                    Toast.makeText(this, R.string.replace_toast, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    preference.setSummary(R.string.storage_external);
                    RootCmd.RunRootCmd("mount -o remount,rw /system");
                    RootCmd.RunRootCmd("rm -rf /system/bin/sdcard");
                    RootCmd.RunRootCmd("rm -rf /system/bin/vold");
                    RootCmd.RunRootCmd("rm -rf /system/etc/vold.fstab");
                    RootCmd.RunRootCmd("rm -rf /system/etc/vold.fstab.nand");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/external/sdcard /system/bin/sdcard");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/external/vold /system/bin/vold");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/external/vold.fstab /system/etc/vold.fstab");
                    RootCmd.RunRootCmd("cp -r /system/stocksettings/external/vold.fstab.nand /system/etc/vold.fstab.nand");
                    RootCmd.RunRootCmd("chmod 0755 /system/bin/sdcard");
                    RootCmd.RunRootCmd("chmod 0755 /system/bin/vold");
                    RootCmd.RunRootCmd("chmod 0755 /system/etc/vold.fstab");
                    RootCmd.RunRootCmd("chmod 0755 /system/etc/vold.fstab.nand");
                    RootCmd.RunRootCmd("dd if=/system/stocksettings/external/boot.img of=/dev/bootimg");
                    Toast.makeText(this, R.string.replace_toast, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), R.string.dialog_reboot, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
