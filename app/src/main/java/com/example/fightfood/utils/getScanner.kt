package com.example.fightfood.utils

import android.app.Activity
import android.widget.Toast
import com.example.fightfood.R
import com.example.fightfood.ScanActivity
import com.google.zxing.integration.android.IntentIntegrator

fun getScanner(context: Activity, code: Int = 200) {
    val scanner = IntentIntegrator(context)
    scanner.setCaptureActivity(ScanActivity::class.java)
    scanner.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
    scanner.setBeepEnabled(false)
    scanner.setOrientationLocked(false)
    scanner.setPrompt("")
    scanner.initiateScan()

    if (code != 200) {
        if (code == 404) {
            Toast.makeText(context, R.string.scan_not_found, Toast.LENGTH_SHORT).show()
        } else if (code == 500) {
            Toast.makeText(context, R.string.fight_error, Toast.LENGTH_SHORT).show()
        }
    }
}