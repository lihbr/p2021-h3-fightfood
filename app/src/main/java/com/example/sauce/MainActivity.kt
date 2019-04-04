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


    fun getProduct(view: View) {
        val daSingleton = Volley.newRequestQueue(this);
        // Put a product code here (from scan) then just concatenate it with the url below
        //val productCode = 0
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
                val productCalScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("energy_value")
                val productQualScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("nutrition-score-fr")
                val productNatScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("nova-group")

                Picasso.get()
                    .load(productPicture)
                    .into(foodPic1)

                // Save the different values in some Textview (they are displayed in premium version, and set to a size of 0sp for the free version)
                findViewById<TextView>(R.id.firstCalVal).text = "%s".format(productCalScore.toString())
                findViewById<TextView>(R.id.firstQualVal).text = "%s".format(productQualScore.toString())
                findViewById<TextView>(R.id.firstNatVal).text = "%s".format(productNatScore.toString())

                // Get the different values for comparison
                var calValue = findViewById<TextView>(R.id.firstCalVal).text
                var qualValue = findViewById<TextView>(R.id.firstQualVal).text
                var natValue = findViewById<TextView>(R.id.firstNatVal).text

                var calValueComp = findViewById<TextView>(R.id.firstCalVal2).text
                var qualValueComp = findViewById<TextView>(R.id.firstQualVal2).text
                var natValueComp = findViewById<TextView>(R.id.firstNatVal2).text

                // Change text to string, because I don't know, this language is bullshit and it won't let me do toInt without these lines
                calValue = calValue.toString()
                qualValue = qualValue.toString()
                natValue = natValue.toString()
                calValueComp = calValueComp.toString()
                qualValueComp = qualValueComp.toString()
                natValueComp = natValueComp.toString()

                // Kcal Values Comparison
                // Each time the first collum is for the upper icons
                // 1/2 = Orange 12/22 = Red 13/23 = Green
                if(calValue != "TextView"){
                    if(calValue.toInt() > calValueComp.toInt()){
                        findViewById<ImageView>(R.id.calIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon12).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.calIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.calIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon23).visibility = View.VISIBLE
                    }else if(calValue.toInt() == calValueComp.toInt()){
                        findViewById<ImageView>(R.id.calIcon1).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.calIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.calIcon2).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.calIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon23).visibility = View.INVISIBLE
                    }else{
                        findViewById<ImageView>(R.id.calIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon13).visibility = View.VISIBLE

                        findViewById<ImageView>(R.id.calIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.calIcon22).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.calIcon23).visibility = View.INVISIBLE
                    }
                }

                // Health Values Comparison
                // Each time the first collum is for the upper icons
                // 1/2 = Orange 12/22 = Red 13/23 = Green
                if(qualValue != "TextView"){
                    if(qualValue.toInt() > qualValueComp.toInt()){
                        findViewById<ImageView>(R.id.qualIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon13).visibility = View.VISIBLE

                        findViewById<ImageView>(R.id.qualIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon22).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.qualIcon23).visibility = View.INVISIBLE
                    }else if(qualValue.toInt() == qualValueComp.toInt()){
                        findViewById<ImageView>(R.id.qualIcon1).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.qualIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.qualIcon2).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.qualIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon23).visibility = View.INVISIBLE
                    }else{
                        findViewById<ImageView>(R.id.qualIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon12).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.qualIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.qualIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.qualIcon23).visibility = View.VISIBLE
                    }
                }

                // Natural Impact Values Comparison
                // Each time the first collum is for the upper icons
                // 1/2 = Orange 12/22 = Red 13/23 = Green
                if(natValue != "TextView"){
                    if(natValue.toInt() > natValueComp.toInt()){
                        findViewById<ImageView>(R.id.natIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon13).visibility = View.VISIBLE

                        findViewById<ImageView>(R.id.natIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon22).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.natIcon23).visibility = View.INVISIBLE
                    }else if(natValue.toInt() == natValueComp.toInt()){
                        findViewById<ImageView>(R.id.natIcon1).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.natIcon12).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.natIcon2).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.natIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon23).visibility = View.INVISIBLE
                    }else {
                        findViewById<ImageView>(R.id.natIcon1).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon12).visibility = View.VISIBLE
                        findViewById<ImageView>(R.id.natIcon13).visibility = View.INVISIBLE

                        findViewById<ImageView>(R.id.natIcon2).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon22).visibility = View.INVISIBLE
                        findViewById<ImageView>(R.id.natIcon23).visibility = View.VISIBLE
                    }
                }

                // Now you have cancer, enjoy !

            },
            Response.ErrorListener { error ->
                findViewById<TextView>(R.id.textView2).text = "Shiet"
                // TODO: Handle error
            }
        )

        daSingleton.add(jsonObjectRequest)
    }

    fun getProduct2(view: View) {
        val daSingleton = Volley.newRequestQueue(this);
        // Put a product code here (from scan) then just concatenate it with the url below
        //val productCode = 0
        val url = "https://world.openfoodfacts.org/api/v0/product/3350030182793.json"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                val foodPic1 = findViewById<ImageView>(R.id.food1)
                val foodPic2 = findViewById<ImageView>(R.id.food2)

                var strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val code = jsonObj.getString("code")
                val label = jsonObj.getJSONObject("product").getString("labels")
                val productPicture = jsonObj.getJSONObject("product").getString("image_small_url")
                val productCalScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("energy_value")
                val productQualScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("nutrition-score-fr")
                val productNatScore = jsonObj.getJSONObject("product").getJSONObject("nutriments").getString("nova-group")

                Picasso.get()
                    .load(productPicture)
                    .into(foodPic2)

                findViewById<TextView>(R.id.firstCalVal2).text = "%s".format(productCalScore.toString())
                findViewById<TextView>(R.id.firstQualVal2).text = "%s".format(productQualScore.toString())
                findViewById<TextView>(R.id.firstNatVal2).text = "%s".format(productNatScore.toString())


                var calValue = findViewById<TextView>(R.id.firstCalVal2).text
                var qualValue = findViewById<TextView>(R.id.firstCalVal2).text
                var natValue = findViewById<TextView>(R.id.firstCalVal2).text

                var calValueComp = findViewById<TextView>(R.id.firstCalVal).text
                var qualValueComp = findViewById<TextView>(R.id.firstCalVal).text
                var natValueComp = findViewById<TextView>(R.id.firstCalVal).text

            },
            Response.ErrorListener { error ->
                findViewById<TextView>(R.id.textView2).text = "Shiet"
                // TODO: Handle error
            }
        )

        daSingleton.add(jsonObjectRequest)
    }
}
