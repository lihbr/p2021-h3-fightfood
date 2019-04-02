package com.example.myfirstapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toastMe(view: View) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
    }

    fun countMe(view: View) {
        // Get the text view
        val textView = findViewById<TextView>(R.id.textView)

        // Get the value of the text view.
        val countString = textView.text.toString()

        // Convert value to a number and increment it
        var count: Int = Integer.parseInt(countString)
        count++

        // Display the new value in the text view.
        textView.text = count.toString()
    }

    fun randomMe(view: View) {
        // Get the text view
        val textView = findViewById<TextView>(R.id.textView)

        // Convert value to a number and increment it
        var count: Int = floor((Math.random() * 100)).toInt()

        // Display the new value in the text view.
        textView.text = count.toString()
    }

    fun scanMe(view: View) {
        val scanner = IntentIntegrator(this)
        scanner.setCaptureActivity(FixedCaptureActivity::class.java)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
        scanner.setBeepEnabled(false)
        scanner.setOrientationLocked(false)
        scanner.initiateScan()

        /*
        // Create an Intent to start the second activity
        val scanIntent = Intent(this, SecondActivity::class.java)

        // Get the text view
        val textView = findViewById<TextView>(R.id.textView)

        // Get the current value of the text view.
        val countString = textView.text.toString()

        // Convert the count to an int
        val count = Integer.parseInt(countString)

        // Add the count to the extras for the Intent.
        scanIntent.putExtra("TOTAL_COUNT", count)

        // Start the new activity.
        startActivity(scanIntent)
        */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
