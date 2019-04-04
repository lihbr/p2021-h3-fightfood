package com.example.fightfood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fightfood.product.ProductItem
import com.example.fightfood.utils.getScanner
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {

    private var offResponses: ArrayList<String> = arrayListOf()
    private var products: MutableList<MutableMap<String, Any?>> = mutableListOf()
    private lateinit var productsItemAdapter: FastItemAdapter<ProductItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        offResponses = intent.getStringArrayListExtra("OFF_RESPONSES")

        parseAndRankResponse()

        initRecycler()
    }

    /**
     * Parse response to rank
     */
    private fun parseAndRankResponse() {
        var moy = 0f

        for (product in offResponses) {
            val p = JSONObject(product)
            var img = p.getJSONObject("product").getString("image_small_url")
            if (img == null || img == "") {
                img = p.getJSONObject("product").getString("image_url")
            }

            var energy = p.getJSONObject("product").getJSONObject("nutriments").getString("energy_100g")
            if (energy == null || energy == "") {
                energy = p.getJSONObject("product").getJSONObject("nutriments").getString("energy")
            }
            moy += energy.toFloat()

            var quality = p.getJSONObject("product").getString("nutrition_grades")
            if (quality == null || quality == "") {
                quality = p.getJSONObject("product").getString("nutrition_grade_fr")
            }
            quality = quality.toString()
            if (quality == "a" || quality == "b") {
                quality = "high"
            } else if (quality == "c") {
                quality = "medium"
            } else if (quality == "d" || quality == "e") {
                quality = "low"
            }

            var natural = p.getJSONObject("product").getString("nova_group")
            if (natural == null || natural == "") {
                natural = p.getJSONObject("product").getJSONObject("nutriments").getString("nova-group")
            }
            natural = natural.toString()
            if (natural == "1") {
                natural = "high"
            } else if (natural == "2") {
                natural = "medium"
            } else if (natural == "3" || natural == "4") {
                natural = "low"
            }

            products.add(mutableMapOf(
                "img" to img,
                "energy" to energy.toFloat(),
                "quality" to quality,
                "natural" to natural
            ))
        }

        moy /= offResponses.size
        val stdError = moy / 5

        println(moy)
        println(stdError)

        for (product in products) {
            println(product["energy"])
            if (product["energy"] as Float - (moy + stdError) > 0) {
                product.put("energy", "high")
            } else if (product["energy"] as Float - (moy - stdError) > 0) {
                product.put("energy", "low")
            } else if (product["energy"] != null) {
                product.put("energy", "medium")
            }
        }
    }

    /**
     * Init recycler view
     */
    private fun initRecycler() {
        val productsRecyclerView = this.findViewById<RecyclerView>(R.id.result_list)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        productsRecyclerView.setLayoutManager(linearLayoutManager)

        productsItemAdapter = FastItemAdapter()

        for (product in products) {
            productsItemAdapter.add(ProductItem(product))
        }

        productsRecyclerView.setAdapter(productsItemAdapter)
    }

    /**
     * Launch scan activity again
     */
    fun scanAgain(view: View) {
        getScanner(this)
    }
}
