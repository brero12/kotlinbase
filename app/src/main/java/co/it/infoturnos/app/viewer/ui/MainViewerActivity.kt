package co.it.infoturnos.app.viewer.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.WebSettings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import co.it.infoturnos.app.viewer.R
import co.it.infoturnos.app.viewer.ui.settings.SettingsActivity
import co.it.infoturnos.app.viewer.utilities.Utilities
import kotlinx.android.synthetic.main.activity_main_viewer.*

class MainViewerActivity : AppCompatActivity() {

    private var urlAtr: String? = ""
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_viewer)

        pref = getSharedPreferences(Utilities.preferencesName, Context.MODE_PRIVATE)


        //val deviceName: String =
          //  Settings.System.getString(contentResolver, "device_name")

        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        val client = CustomWebViewClient()
        client.cxt = applicationContext
        webView.webViewClient = client

        goToPage(pref,"Test1")

        // To Settings Activity
        gesture_area.setOnLongClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        //val deviceName: String = Build.getSerial()

        val deviceName: String ="test"

        goToPage(pref,deviceName)
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        /*if (hasFocus) hideSystemUI()*/
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun goToPage(pref: SharedPreferences, deviceName: String) {
        urlAtr = pref.getString(Utilities.preferencesUrl,
            "https://3.135.18.80/turnos/atril"
        )
        webView.loadUrl(urlAtr)
    }
}
