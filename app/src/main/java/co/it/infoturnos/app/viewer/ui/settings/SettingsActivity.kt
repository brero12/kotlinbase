package co.it.infoturnos.app.viewer.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.it.infoturnos.app.viewer.R
import co.it.infoturnos.app.viewer.utilities.Utilities
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val pref = getSharedPreferences(Utilities.preferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()

        settings_url_atril.editText?.setText(pref.getString(Utilities.preferencesUrl, ""))

        save_settings.setOnClickListener {
            editor.putString(Utilities.preferencesUrl, settings_url_atril.editText?.text.toString())
            editor.apply()
            Toast.makeText(this, getString(R.string.settings_message_update), Toast.LENGTH_LONG).show()
        }
    }
}