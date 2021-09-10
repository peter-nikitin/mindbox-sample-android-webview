package com.akkalomator.mindboxjssdktest

import android.app.Application
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import cloud.mindbox.mobile_sdk.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val domain = "api.mindbox.ru"
        val endpoint = "mpush-test-Android"
        if (domain.isNotBlank() && endpoint.isNotBlank()) {
            val configs = MindboxConfiguration.Builder(this, domain, endpoint)
                .build()

            Mindbox.setLogLevel(Level.VERBOSE)
            Mindbox.init(this, configs)
        }
    }
}