package com.xs.stocksettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;

public class GestureActivity extends PreferenceActivity {
	
	private static final String DOUBLE_TAP_TO_WAKE = "double_tap_to_wake";
	private static final String CAMERA_GESTURE = "camera_gesture";
	private static final String MUSIC_GESTURE = "music_gesture";
	
	private CheckBoxPreference mDoubleTapToWake;
	private CheckBoxPreference mCameraGesture;
	private CheckBoxPreference mMusicGesture;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_gesture);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		mDoubleTapToWake = (CheckBoxPreference) findPreference(DOUBLE_TAP_TO_WAKE);
		mCameraGesture = (CheckBoxPreference) findPreference(CAMERA_GESTURE);
		mMusicGesture = (CheckBoxPreference) findPreference(MUSIC_GESTURE);
	}
	
	public boolean onPreferenceTreeClick(PreferenceScreen preferencescreen,Preference preference){
		if (preference == mDoubleTapToWake){
			if (mDoubleTapToWake.isChecked()){
				RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/double_tap_enable");
			}
			else {
				RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/double_tap_enable");
			}
		}
		if (preference == mCameraGesture){
			if (mCameraGesture.isChecked()){
				RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/camera_enable");
			}
			else {
				RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/camera_enable");
			}
		}
		if (preference == mMusicGesture){
			if (mMusicGesture.isChecked()){
				RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/music_enable");
			}
			else {
				RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/music_enable");
			}
		}
		return false;
	}
	
	public static void RestoreDoubleTapToWake (Context paramContext){
		if (PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("double_tap_to_wake",true)){
			RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/double_tap_enable");
		}
		else if (!PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("double_tap_to_wake",true)){
			RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/double_tap_enable");
		}
	}
	
	public static void RestoreCameraGesture (Context paramContext){
		if (PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("camera_gesture",true)){
			RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/camera_enable");
		}
		else if (!PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("camera_gesture",true)){
			RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/camera_enable");
		}
	}
	public static void RestoreMusicGesture (Context paramContext){
		if (PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("music_gesture",true)){
			RootCmd.RunRootCmd("echo '1' > /proc/touchpanel/music_enable");
		}
		else if (!PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("music_gesture",true)){
			RootCmd.RunRootCmd("echo '0' > /proc/touchpanel/music_enable");
		}
	}
}