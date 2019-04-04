package com.example.fightfood

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
    }

    /**
     * Check if camera permission are granted or ask for it
     */
    private fun checkPermission() {
        if (checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), 101
            )
        }
    }

    /**
     * Launch scan activity
     */
    fun startScan(view: View) {
        if (checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            checkPermission()
        } else {
            val scanner = IntentIntegrator(this)
            scanner.setCaptureActivity(ScanActivity::class.java)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
            scanner.setBeepEnabled(false)
            scanner.setOrientationLocked(false)
            scanner.setPrompt("")
            scanner.initiateScan()
        }
    }
}
