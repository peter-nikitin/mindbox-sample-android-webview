package com.akkalomator.mindboxjssdktest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.webkit.CookieManager
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

        Mindbox.subscribeDeviceUuid {
            manager.setCookie(URL, "mindboxDeviceUUID=$it")
        }
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.loadUrl(URL)
        binding.viewCookies.setOnClickListener {
            Log.d(TAG, "cookies ::: ${manager.getCookie(URL)} task2")

            Mindbox.subscribeDeviceUuid {
                Log.d(TAG, "mindboxDeviceUUID=$it")
            }
        }

        binding.setCookies.setOnClickListener {
            val manager = CookieManager.getInstance()
            Mindbox.subscribeDeviceUuid { uuid ->
                val cookieString = manager.getCookie(URL)
                manager.removeAllCookies { }
                cookieString
                    .split(";")
                    .map(String::trim)
                    .forEach { cookie ->
                        val cookie = cookie.takeIf { !it.startsWith("mindboxDeviceUUID") }
                            ?: "mindboxDeviceUUID=$uuid"
                        manager.setCookie(URL, cookie)
                    }
            }
        }
    }
}