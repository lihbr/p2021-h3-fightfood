package com.example.fightfood

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView

import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

import java.util.Arrays

class ScanActivity: Activity() {
    private var barcodeView: DecoratedBarcodeView? = null
    private var scanned: Array<String> = arrayOf("", "")

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || scanned.contains(result.text)) {
                // Prevent duplicate scans
                return
            }

            updateLayout(result.text, result.getBitmap())
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        barcodeView = findViewById(R.id.barcode_scanner)
        val formats = Arrays.asList(BarcodeFormat.EAN_13)
        barcodeView!!.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView!!.initializeFromIntent(intent)
        barcodeView!!.setStatusText("")
        barcodeView!!.decodeContinuous(callback)
    }

    override fun onResume() {
        super.onResume()

        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView!!.pause()
    }

    fun pause(view: View) {
        barcodeView!!.pause()
    }

    fun resume(view: View) {
        barcodeView!!.resume()
    }

    fun triggerScan(view: View) {
        barcodeView!!.decodeSingle(callback)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    companion object {
        private val TAG = ScanActivity::class.java.simpleName
    }

    /**
     * Update layout
     */
    private fun updateLayout(text: String = "", img: Bitmap? = null) {
        if ((scanned.size) > 0) {
            var filled: Int = 0
            var found: Boolean = false

            for (i:Int in 0 until scanned.size) {
                if (scanned[i] === "" && text !== "") {
                    if (!found) {
                        scanned[i] = text

                        if (img != null) {
                            val id: Int? = resources.getIdentifier("history_".plus((i + 1).toString()).plus("_image"), "id", packageName)
                            if (id != null) {
                                val historyImg = findViewById<ImageView>(id)
                                historyImg.setImageBitmap(img)
                                fade("history_".plus((i + 1).toString()).plus("_close"), 0f, 1f)
                            }
                        }

                        found = true
                        filled++
                    }
                } else if (scanned[i] !== "") {
                    filled++
                }
            }

            // Adapt text
            val instruction = findViewById<TextView>(R.id.instruction)
            val history = findViewById<TextView>(R.id.history)
            barcodeView!!.resume()
            fade("scan_done", 1f, 0f)
            if (filled == 0) {
                instruction.text = getString(R.string.scan_instruction_first)
                history.text = getString(R.string.scan_history_empty)
            } else {
                if (filled == 1) {
                    instruction.text = getString(R.string.scan_instruction_second)
                } else {
                    instruction.text = getString(R.string.scan_instruction_empty)
                    fade("scan_done", 0f, 1f)
                    barcodeView!!.pause()
                }
                history.text = getString(R.string.scan_history)
            }
        }
    }

    /**
     * Close an history image
     */
    fun closeHistory (view: View) {
        val historyNum: Int = Integer.parseInt(view.tag.toString())

        if (scanned[historyNum - 1] !== "") {
            fade("history_".plus(historyNum.toString()).plus("_close"), 1f, 0f)
            scanned[historyNum - 1] = ""

            val id: Int? = resources.getIdentifier("history_".plus(historyNum.toString()).plus("_image"), "id", packageName)
            if (id != null) {
                val historyImg = findViewById<ImageView>(id)
                historyImg.setImageResource(android.R.color.transparent)
            }

            updateLayout()
        }
    }

    /**
     * Fade in or out an element
     */
    private fun fade(id: String? = null, start: Float = 0f, end: Float = 1f, duration: Long = 500) {
        if (id != null) {
            val theID: Int? = resources.getIdentifier(id, "id", packageName)
            if (theID != null) {
                val element = findViewById<View>(theID)
                val objectAnimator = ObjectAnimator.ofFloat(element, "alpha", start, end)

                if (element.alpha == start) {
                    objectAnimator.interpolator = AccelerateInterpolator(1.5f)
                    objectAnimator.duration = duration

                    objectAnimator.start()
                }
            }
        }
    }

    /**
     * Launch fight if possible
     */
    fun launchFight(view: View) {
        if ((scanned.size) > 0) {
            for (i:Int in 0 until scanned.size) {
                if (scanned[i] === "") {
                    return
                }
            }

            val fightStartIntent = Intent(this, FightActivity::class.java)

            fightStartIntent.putExtra("SCANNED", scanned)

            startActivity(fightStartIntent)
        }
        return
    }
}