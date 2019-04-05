package com.example.fightfood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.fightfood.product.ProductItem
import com.example.fightfood.utils.getScanner
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_result.*
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
            val p = JSONObject(product).getJSONObject("product")

            var img = ""
            var energy = "-1"
            var quality = ""
            var natural = ""

            // Find img
            if (p.has("image_small_url")) {
                img = p.getString("image_small_url")
            }

            // Find energy
            if (p.has("nutriments") && p.getJSONObject("nutriments").has("energy_100g")) {
                energy = p.getJSONObject("nutriments").getString("energy_100g")
                moy += energy.toFloat()
            }

            // Find quality
            if (p.has("nutrition_grades")) {
                quality = p.getString("nutrition_grades")
            }

            if (quality == "a" || quality == "b") {
                quality = "high"
            } else if (quality == "c") {
                quality = "medium"
            } else if (quality == "d" || quality == "e") {
                quality = "low"
            }

            // Find natural
            if (p.has("nova_group")) {
                natural = p.getString("nova_group")
            }

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

        // Determine energy status accordingly to other scanned products
        moy /= offResponses.size
        val stdError = moy / 5

        for (product in products) {
            println(product["energy"])
            if (product["energy"] as Float - (moy + stdError) > 0) {
                product.put("energy", "high")
            } else if (product["energy"] as Float - (moy - stdError) > 0) {
                product.put("energy", "low")
            } else if (product["energy"] as Float >= 0) {
                product.put("energy", "medium")
            } else {
                product.put("energy", "")
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
