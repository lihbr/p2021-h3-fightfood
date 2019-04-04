package com.example.fightfood

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
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
     * (cheap) animation for the fight
     */
    private fun animateFight() {
        val primary: View? = findViewById(R.id.fight_1)
        val secondary: View? = findViewById(R.id.fight_2)
        val thunder: View? = findViewById(R.id.fight_thunder)
        if (primary != null && secondary != null && thunder != null) {
            val anims = mutableListOf<ObjectAnimator>()
            anims.add(ObjectAnimator.ofFloat(primary, "translationY", -2500f, 0f))
            anims.add(ObjectAnimator.ofFloat(primary, "translationX", -600f, 0f))
            anims.add(ObjectAnimator.ofFloat(primary, "rotation", -90f, 0f))
            anims.add(ObjectAnimator.ofFloat(secondary, "translationY", 2500f, 0f))
            anims.add(ObjectAnimator.ofFloat(secondary, "translationX", 600f, 0f))
            anims.add(ObjectAnimator.ofFloat(secondary, "rotation", 90f, 0f))
            anims.add(ObjectAnimator.ofFloat(thunder, "rotation", 0f, 180f))
            anims.add(ObjectAnimator.ofFloat(primary, "translationY", 0f, -800f))
            anims.add(ObjectAnimator.ofFloat(primary, "translationX", 0f, 800f))
            anims.add(ObjectAnimator.ofFloat(secondary, "translationY", 0f, 800f))
            anims.add(ObjectAnimator.ofFloat(secondary, "translationX", 0f, -800f))

            for (anim in anims) {
                anim.interpolator = AccelerateInterpolator(1.25f)
                anim.duration = 2000
            }

            val animatorSet = AnimatorSet()
            animatorSet.play(anims[0])
                .with(anims[1]).with(anims[2])
                .with(anims[3]).with(anims[4])
                .with(anims[5]).before(anims[6])
                .before(anims[7]).before(anims[8])
                .before(anims[9]).before(anims[10])

            animatorSet.addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animationDone = true
                    tryResult()
                }

                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animationDone = true
                    tryResult()
                }

            })

            animatorSet.start()
        } else {
            animationDone = true
            tryResult()
        }
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

    // Quickfix
    override fun onResume() {
        super.onResume()
        if (animationDone && offResponses.size == scanned.size) {
            getScanner(this)
        }
    }
}
