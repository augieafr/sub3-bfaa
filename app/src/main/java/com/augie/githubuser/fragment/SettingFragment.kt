package com.augie.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.augie.githubuser.R
import com.augie.githubuser.receiver.AlarmReceiver

class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var alarmPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        ALARM = getString(R.string.key_alarm)
        alarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == ALARM) {
            if (alarmPreference.isChecked) {
                Toast.makeText(context, getString(R.string.alarm_on), Toast.LENGTH_SHORT).show()
                alarmReceiver.setAlarm(requireContext(), "4:20")
            } else {
                Toast.makeText(context, getString(R.string.alarm_off), Toast.LENGTH_SHORT).show()
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }
}