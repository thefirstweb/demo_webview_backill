package com.stiemaji.demo

import android.app.Application
import com.sitemaji.core.SitemajiCore
import com.sitemaji.core.SitemajiCoreStatusListener

/**
 * Created by showsky on 2021/11/5
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SitemajiCore.Builder(this, Config.PACKAGE)
            .withDevelopEnable(true)
            .withLogEnabled(true)
            .withCoreStatusListener(object : SitemajiCoreStatusListener {
                override fun onSuccess() {
                }

                override fun onFail(p0: Int, p1: String?) {
                }
            })
            .build()
    }
}
