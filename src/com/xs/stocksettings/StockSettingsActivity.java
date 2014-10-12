package com.xs.stocksettings;

import android.os.Bundle;
import android.preference.*;
import android.provider.Settings;
import android.widget.Toast;

import android.os.SystemProperties;

public class StockSettingsActivity extends PreferenceActivity {

	private static final String ABOUT = "about";
	private static final String REBOOT = "advanced_reboot";
	private static final String PROXIMITY = "proximity_on_wake";

	private static final String XS = SystemProperties
			.get("ro.build.display.id");
	private static final String XS1 = SystemProperties.get("ro.weibo.com");
	private static final String XS2 = SystemProperties
			.get("ro.product.mod_device");

	private Preference mAbout;
	private CheckBoxPreference mReboot;
	private CheckBoxPreference mProximity;

	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		addPreferencesFromResource(R.xml.activity_stocksettings);

		mAbout = (Preference) findPreference(ABOUT);
		mReboot = (CheckBoxPreference) findPreference(REBOOT);
		mProximity = (CheckBoxPreference) findPreference(PROXIMITY);
	}

	public void onStart() {
		super.onStart();
		if (!this.XS.equals("11-20140805-SNAPSHOT-M9-bacon-XS")
				|| !this.XS1.equals("weibo.com/acexs")
				|| !this.XS2.equals("bacon_xs")) {
			Toast.makeText(this, "您的ROM似乎不是正版ROM，请下载XS的正版作品！多谢支持！顺便鄙视盗版狗！",
					20000).show();
			finish();
		}
	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen,
			Preference preference) {

		if (preference == mAbout) {
			Toast.makeText(this, R.string.tap_about, 2000).show();
		}

		if (preference == mReboot) {
			if (mReboot.isChecked()) {
				Settings.Secure.putInt(getContentResolver(), REBOOT, 1);
			} else {
				Settings.Secure.putInt(getContentResolver(), REBOOT, 0);
			}
		}

		if (preference == mProximity) {
			if (mProximity.isChecked()) {
				Settings.System.putInt(getContentResolver(), PROXIMITY, 1);
			} else {
				Settings.System.putInt(getContentResolver(), PROXIMITY, 0);
			}
		}
		return false;
	}

}
