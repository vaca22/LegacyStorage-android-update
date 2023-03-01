package com.vaca.legacystorage_android_update

import android.app.Application

class MainApplication:Application() {
    companion object{
        lateinit var myApplication:MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        PathUtil.initVar(this)
        myApplication=this
    }
}