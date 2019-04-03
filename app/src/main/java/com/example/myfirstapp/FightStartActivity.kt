package com.example.myfirstapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FightStartActivity : AppCompatActivity() {

    private var scanned: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_start)

        scanned = intent.getStringArrayExtra("SCANNED")
    }
}
