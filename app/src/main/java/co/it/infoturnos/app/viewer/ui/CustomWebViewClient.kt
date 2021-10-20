package co.it.infoturnos.app.viewer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import co.it.infoturnos.app.viewer.utilities.Utilities

class CustomWebViewClient : WebViewClient() {

    var cxt: Context? = null

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = request?.url
        Log.d("EPSON-AEV", "*******************")
        Log.d("EPSON-AEV", "uri ${request?.url}")
        if (uri.toString().startsWith(Utilities.printActionFlag)) {

            // Call intent broadcast service for print ticket
            val intentService = dataToIntent(uri)
            intentService.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            intentService.action = "co.it.infoturnos.app"
            intentService.component =
                ComponentName("com.epson.mpos_print_2", "com.epson.mpos_print_2.PrintService")
            cxt!!.sendBroadcast(intentService)
        } else {
            view?.loadUrl(request?.url.toString())
        }
        return true
    }

    private fun dataToIntent(uri: Uri?): Intent {
        Log.d("EPSON-AEV", "URI $uri")
        val intent = Intent()
        if (uri != null) {
            // Check scheme & host
            val scheme = uri.scheme
            val host = uri.host

            Log.d("EPSON-AEV", "scheme $scheme")
            Log.d("EPSON-AEV", "host $host")

            if (scheme != null && scheme == "epsonprint" && host != null && host == "mpos_print_2.epson.com") {
                // Check params
                val params: List<String> = uri.pathSegments

                Log.d("EPSON-AEV", "params.size ${params.size}")

                if (params.size == 2) {

                    val action = params[0]
                    val type = params[1]

                    if (action == "print") {
                        // Process data and action type
                        val data = uri.getQueryParameter("d")

                        if (data != null && data.isNotEmpty()) {
                            intent.putExtra(
                                "com.epson.mpos_print_2.extra_print_activity_data",
                                data
                            )
                            intent.putExtra(
                                "com.epson.mpos_print_2.extra_print_activity_type",
                                type
                            )
                            Log.d("EPSON-AEV", "data $data")
                            Log.d("EPSON-AEV", "type $type")
                        } else {
                            Log.e("EPSON-AEV", "No data found")
                        }
                    } else {
                        Log.e("EPSON-AEV", "no action print found")
                    }
                } else {
                    Log.e("EPSON-AEV", "No params found")
                }
            } else {
                Log.e("EPSON-AEV", "No found scheme or host")
            }
        }
        return intent
    }
}