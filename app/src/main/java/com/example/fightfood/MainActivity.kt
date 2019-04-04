package com.example.fightfood

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.View
import android.widget.Toast
import com.example.fightfood.utils.getScanner

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
     * Check if device is connected to interne
     */
    private fun checkInternet():Boolean {
        val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!= null && networkInfo.isConnected
    }

    /**
     * Launch scan activity
     */
    fun startScan(view: View) {
        if (checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            checkPermission()
        } else if (!checkInternet()) {
            Toast.makeText(applicationContext, R.string.not_connected, Toast.LENGTH_SHORT).show()
        } else {
            getScanner(this)
        }
    }

    fun comingSoon(view: View) {
        Toast.makeText(applicationContext, R.string.coming_soon, Toast.LENGTH_SHORT).show()
    }
}
