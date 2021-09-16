package com.akkalomator.mindboxjssdktest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.webkit.CookieManager
import android.webkit.WebView
import cloud.mindbox.mobile_sdk.Mindbox
import com.akkalomator.mindboxjssdktest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"

        const val URL = "https://wpush-test.nikitin-petr.ru/"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val manager = CookieManager.getInstance()
        manager.setAcceptCookie(true);

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.loadUrl(URL)
        binding.webView.getSettings().domStorageEnabled = true


        binding.viewCookies.setOnClickListener {
            Log.d(TAG, "cookies ::: ${manager.getCookie(URL)} Cookie for $URL")

            Mindbox.subscribeDeviceUuid {
                Log.d(TAG, "mindboxDeviceUUID=$it")
            }

            binding
                .webView
                .evaluateJavascript("(function() {return window.localStorage.getItem('mindboxDeviceUUID')})()"
                ) { s ->
                    android.util.Log.d(
                        TAG,
                        (s)!!
                    ) // Prints the string 'null' NOT Java null
                };
        }
        fun syncMindboxDeviceUUID(webView: WebView){
            Mindbox.subscribeDeviceUuid { uuid ->
                webView
                    .evaluateJavascript("document.cookie = \"mindboxDeviceUUID=$uuid\";  window.localStorage.setItem('mindboxDeviceUUID', '$uuid')"
                    ) { };
                    }
            }
        binding.setCookies.setOnClickListener {
            syncMindboxDeviceUUID(binding.webView)
        }
    }
}