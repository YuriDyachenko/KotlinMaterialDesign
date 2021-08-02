package dyachenko.kotlinmaterialdesign.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dyachenko.kotlinmaterialdesign.R
import dyachenko.kotlinmaterialdesign.model.SettingsData
import dyachenko.kotlinmaterialdesign.view.pod.PODFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readSettings()
        setCurrentTheme()

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commitNow()
        }
    }

    private fun readSettings() {
        SettingsData.CURRENT_THEME =
            getSharedPreferences(SettingsData.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getInt(SettingsData.CURRENT_THEME_PREF_NAME, SettingsData.THEME_PURPLE)
    }

    private fun setCurrentTheme() {
        if (SettingsData.CURRENT_THEME == SettingsData.THEME_TEAL) {
            setTheme(SettingsData.THEME_TEAL_ID)
        } else {
            setTheme(SettingsData.THEME_PURPLE_ID)
        }
    }

}