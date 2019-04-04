package com.example.fightfood

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fightfood.utils.getScanner
import org.json.JSONObject

class FightActivity : AppCompatActivity() {

    private var scanned: Array<String> = arrayOf("", "")
    private var offResponses: ArrayList<String> = arrayListOf()
    private var animationDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        scanned = intent.getStringArrayExtra("SCANNED")

        fetchAPI()
        displayImg()
        animateFight()
    }

    /**
     * Fetch OFF API
     */
    private fun fetchAPI() {
        val baseUrl = "https://world.openfoodfacts.org/api/v0/product/"

        for (i:Int in 0 until scanned.size) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "$baseUrl${scanned[i]}",
                null,
                Response.Listener { response ->
                    val result = response.toString()
                    val status = JSONObject(result).getInt("status")
                    if (status != 1) {
                        goBack(404)
                    } else {
                        offResponses.add(result)
                        tryResult()
                    }
                },
                Response.ErrorListener { error ->
                    println(error)
                    goBack(500)
                }
            )
            jsonObjectRequest.tag = "fight_fetch"
            App.sharedInstance.requestQueue.add(jsonObjectRequest)
        }
    }

    /**
     * Display scanned images
     */
    private fun displayImg() {
        for (i:Int in 0 until scanned.size) {
            // Update img
            val id: Int? =
                resources.getIdentifier("fight_${i + 1}_image", "id", packageName)
            if (id != null) {
                val fightImg = findViewById<ImageView>(id)
                var fileName: String? = "scannedImg_${i + 1}"
                val img: Bitmap? = BitmapFactory.decodeStream(this.openFileInput(fileName))
                if (img != null) {
                    fightImg.setImageBitmap(img)
                } else {
                    fightImg.setImageResource(android.R.color.transparent)
                }
            }
        }
    }

    /**
     * Animate the fight
     */
    private fun animateFight() {
        animationDone = true
        tryResult()
    }

    /**
     * Check if both animation and api fetches are done
     */
    private fun tryResult() {
        if (!animationDone || offResponses.size != scanned.size) {
            return
        }

        val resultIntent = Intent(this, ResultActivity::class.java)
        resultIntent.putStringArrayListExtra("OFF_RESPONSES", offResponses)
        startActivity(resultIntent)
    }

    private fun goBack(code: Int) {
        getScanner(this, code)
    }
}
