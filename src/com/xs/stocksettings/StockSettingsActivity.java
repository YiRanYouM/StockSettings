package com.xs.stocksettings;

import android.os.Bundle;
import android.preference.*;
import android.provider.Settings;
import android.widget.Toast;

import android.os.SystemProperties;

public class StockSettingsActivity extends PreferenceActivity {

	private static final String ABOUT = "about";
	private static final String CRT = "screen_animation_style";
	private static final String REBOOT = "advanced_reboot";

	private static final String XS = SystemProperties.get("ro.build.display.id");
	private static final String XS1 = SystemProperties.get("ro.weibo.com");

	private Preference mAbout;
	private CheckBoxPreference mCrt;
	private CheckBoxPreference mReboot;

	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		addPreferencesFromResource(R.xml.activity_stocksettings);

		mAbout = (Preference) findPreference(ABOUT);
		mCrt = (CheckBoxPreference) findPreference(CRT);
		mReboot = (CheckBoxPreference) findPreference(REBOOT);
	}

	public void onStart() {
		super.onStart();
		if (!this.XS.equals("11-20140805-SNAPSHOT-M9-bacon-XS") || !this.XS1.equals("weibo.com/acexs")) {
			Toast.makeText(this, "您的ROM似乎不是正版ROM，请下载XS的正版作品！多谢支持！顺便鄙视盗版狗！", 20000).show();
			finish();
		}
	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen,
			Preference preference) {

		if (preference == mAbout) {
			Toast.makeText(this, "附加设置由XS开发，仅限本人及小华使用，严禁他人盗窃！", 2000).show();
		}

		if (preference == mCrt) {
			if (mCrt.isChecked()) {
				Settings.System.putInt(getContentResolver(), CRT, 0);
			} else {
				Settings.System.putInt(getContentResolver(), CRT, 1);
			}
		}

		if (preference == mReboot) {
			if (mReboot.isChecked()) {
				Settings.Secure.putInt(getContentResolver(), REBOOT, 1);
			} else {
				Settings.Secure.putInt(getContentResolver(), REBOOT, 0);
			}
		}
		return false;
	}

}
