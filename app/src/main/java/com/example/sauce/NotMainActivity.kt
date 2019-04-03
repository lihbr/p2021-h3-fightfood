package com.example.sauce

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NotMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_main)
        showRandomNumber()
    }

    companion object {
        const val TOTAL_COUNT = "total_count"
    }

    fun showRandomNumber() {
        // Get the count from the intent extras
        val count = intent.getIntExtra(TOTAL_COUNT, 0)

        // Generate the random number
        val randomNumb = (0..count).random()

        // Display the random number.
        findViewById<TextView>(R.id.textView3).text = Integer.toString(randomNumb)
    }
}
