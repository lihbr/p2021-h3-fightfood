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
import android.widget.ImageView
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toastMe(view: View) {
        val daSingleton = Volley.newRequestQueue(this);
        val url = "https://world.openfoodfacts.org/api/v0/product/3350030201890.json"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                val foodPic1 = findViewById<ImageView>(R.id.food1)
                val foodPic2 = findViewById<ImageView>(R.id.food2)

                var strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val code = jsonObj.getString("code")
                val label = jsonObj.getJSONObject("product").getString("labels")
                val productPicture = jsonObj.getJSONObject("product").getString("image_small_url")

                Picasso.get()
                    .load(productPicture)
                    .into(foodPic1)
                Picasso.get()
                    .load(productPicture)
                    .into(foodPic2)


                findViewById<TextView>(R.id.textView2).text = "%s".format(code.toString())

            },
            Response.ErrorListener { error ->
                findViewById<TextView>(R.id.textView2).text = "Shiet"
                // TODO: Handle error
            }
        )

        daSingleton.add(jsonObjectRequest)

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
