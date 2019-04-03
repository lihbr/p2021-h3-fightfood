package com.example.sauce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_not_main.*
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toastMe(view: View) {
        val reqsdqsd = Volley.newRequestQueue(this);
        val url = "https://world.openfoodfacts.org/api/v0/product/3350030201890.json"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                var strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val code = jsonObj.getString("code")
                val label = jsonObj.getJSONObject("product").getString("labels")

                findViewById<TextView>(R.id.textView2).text = "Response: %s".format(code.toString())
                // Log.d("TAG", "La Sauce")
            },
            Response.ErrorListener { error ->
                findViewById<TextView>(R.id.textView2).text = "Shiet"
                // Log.d("TAG", "SnanS")
                // TODO: Handle error
            }
        )

        reqsdqsd.add(jsonObjectRequest)

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }

    /*fun countMe (view: View) {
        // Get the text view
        val showCountTextView = findViewById<TextView>(R.id.textView2)

        // Get the value of the text view.
        val countString = showCountTextView.text.toString()

        // Convert value to a number and increment it
        var count: Int = Integer.parseInt(countString)
        count += (-10..10).random()

        // Display the new value in the text view.
        showCountTextView.text = count.toString();
    }

    fun randomSooS (view: View){
        // Create an Intent to start the second activity
        val randomIntent = Intent(this, NotMainActivity::class.java)

        // Get the text view
        val showCountTextView = findViewById<TextView>(R.id.textView2)

        // Get the value of the text view.
        val countString = showCountTextView.text.toString()

        // Convert value to a number and increment it
        var count: Int = Integer.parseInt(countString)
        count += 100

        // Display the new value in the text view.
        showCountTextView.text = count.toString();

        // Add the count to the extras for the Intent.
        randomIntent.putExtra(NotMainActivity.TOTAL_COUNT, count)

        // Start the new activity.
        startActivity(randomIntent)
    }*/
}
