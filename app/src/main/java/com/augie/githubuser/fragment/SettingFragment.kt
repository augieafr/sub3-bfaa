package com.augie.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.augie.githubuser.R
import com.augie.githubuser.receiver.AlarmReceiver

class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var alarmKey: String
    private lateinit var alarmPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        alarmKey = getString(R.string.key_alarm)
        alarmPreference = findPreference<SwitchPreference>(alarmKey) as SwitchPreference
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
        if (key == alarmKey) {
            if (alarmPreference.isChecked) {
                Toast.makeText(context, getString(R.string.alarm_on), Toast.LENGTH_SHORT).show()
                alarmReceiver.setAlarm(requireContext(), "9:00")
            } else {
                Toast.makeText(context, getString(R.string.alarm_off), Toast.LENGTH_SHORT).show()
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }
}