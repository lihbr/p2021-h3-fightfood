package com.example.fightfood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FightActivity : AppCompatActivity() {

    private var scanned: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        scanned = intent.getStringArrayExtra("SCANNED")
    }
}
