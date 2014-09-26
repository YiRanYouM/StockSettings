package com.xs.stocksettings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class CameraSwitchActivity extends PreferenceActivity {
	
	private static final String MIUICAMERA = "miui_camera";
	private static final String CMCAMERA = "cm_camera";
	private static final String NUBIACAMERA = "nubia_camera";
	
	private Preference mMiuiCamera;
	private Preference mCmCamera;
	private Preference mNubiaCamera;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_camera_switch);
		
		mMiuiCamera = (Preference) findPreference(MIUICAMERA);
		mCmCamera = (Preference) findPreference(CMCAMERA);
		mNubiaCamera = (Preference) findPreference(NUBIACAMERA);
	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen,
			Preference preference) {
		if(preference == mCmCamera) {
			RootCmd.RunRootCmd("mount -o remount,rw /system");
			RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
			RootCmd.RunRootCmd("cp /system/app/CameraDir/cm_camera.apk /system/app/Camera.apk");
			Toast.makeText(this, "当前相机已经设置为CM相机", 2000).show();
		}
		if(preference == mMiuiCamera) {
			RootCmd.RunRootCmd("mount -o remount,rw /system");
			RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
			RootCmd.RunRootCmd("cp /system/app/CameraDir/miui_camera.apk /system/app/Camera.apk");
			Toast.makeText(this, "当前相机已经设置为MIUI相机", 2000).show();
		}
		if(preference == mNubiaCamera) {
			RootCmd.RunRootCmd("mount -o remount,rw /system");
			RootCmd.RunRootCmd("rm -rf /system/app/Camera.apk");
			RootCmd.RunRootCmd("cp /system/app/CameraDir/nubia_camera.apk /system/app/Camera.apk");
			Toast.makeText(this, "当前相机已经设置为Nubia相机", 2000).show();
		}
		return false;
	}
}