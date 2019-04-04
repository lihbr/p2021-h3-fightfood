package com.example.fightfood.product

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import android.widget.ImageView
import com.example.fightfood.App
import com.example.fightfood.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso

class ProductItem(var product: MutableMap<String, Any?>): AbstractItem<ProductItem,
        ProductItem.ProductViewHolder>() {
    override fun getType(): Int {
        return R.id.product_img
    }
    override fun getViewHolder(view: View?): ProductViewHolder {
        return ProductViewHolder(view)
    }
    override fun getLayoutRes(): Int {
        return R.layout.row_product
    }

    class ProductViewHolder(itemView: View?) : FastAdapter.ViewHolder<ProductItem>(itemView) {
        private var productImg: ImageView?
        private var rankEnergy: ImageView?
        private var rankQuality: ImageView?
        private var rankNatural: ImageView?

        init {
            productImg = itemView?.findViewById(R.id.product_img)
            rankEnergy = itemView?.findViewById(R.id.rank_energy)
            rankQuality = itemView?.findViewById(R.id.rank_quality)
            rankNatural = itemView?.findViewById(R.id.rank_natural)
        }
        override fun unbindView(item: ProductItem?) {
            productImg?.setImageResource(android.R.color.transparent)
            rankEnergy?.setImageResource(android.R.color.transparent)
            rankQuality?.setImageResource(android.R.color.transparent)
            rankNatural?.setImageResource(android.R.color.transparent)
        }
        override fun bindView(item: ProductItem?, payloads: MutableList<Any>?) {
            val product = item?.product
            if(product is MutableMap<*, *>) {
                val rank = mapOf(
                    "low" to ContextCompat.getColor(App.sharedInstance.applicationContext, R.color.rank_low),
                    "medium" to ContextCompat.getColor(App.sharedInstance.applicationContext, R.color.rank_medium),
                    "high" to ContextCompat.getColor(App.sharedInstance.applicationContext, R.color.rank_high),
                    "unknown" to ContextCompat.getColor(App.sharedInstance.applicationContext, R.color.rank_unknown)
                )

                // Set product image
                if (productImg != null && product["img"] != null && product["img"] != "") {
                    Picasso.get()
                        .load(product["img"] as String)
                        .into(productImg)
                }

                // Set energy rank
                if (product["energy"] != null && product["energy"] != "") {
                    rankEnergy!!.setColorFilter(rank[product["energy"]] as Int, PorterDuff.Mode.SRC_IN)
                } else {
                    rankEnergy!!.setColorFilter(rank["unknown"] as Int, PorterDuff.Mode.SRC_IN)
                }

                // Set quality rank
                if (product["quality"] != null && product["quality"] != "") {
                    rankQuality!!.setColorFilter(rank[product["quality"]] as Int, PorterDuff.Mode.SRC_IN)
                } else {
                    rankQuality!!.setColorFilter(rank["unknown"] as Int, PorterDuff.Mode.SRC_IN)
                }

                // Set natural rank
                if (product["natural"] != null && product["natural"] != "") {
                    rankNatural!!.setColorFilter(rank[product["natural"]] as Int, PorterDuff.Mode.SRC_IN)
                } else {
                    rankNatural!!.setColorFilter(rank["unknown"] as Int, PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }
}