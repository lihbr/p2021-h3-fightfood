package com.example.fightfood

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class App: Application() {
    lateinit var requestQueue: RequestQueue

    companion object {
        lateinit var sharedInstance: App
    }

    override fun onCreate() {
        super.onCreate()
        App.sharedInstance = this
        requestQueue = Volley.newRequestQueue(getApplicationContext())
        requestQueue?.start()
    }
}