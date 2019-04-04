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
import android.widget.Toast

import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

import java.util.Arrays
import android.content.Context
import java.io.ByteArrayOutputStream


class ScanActivity: Activity() {
    private var barcodeView: DecoratedBarcodeView? = null
    private var scanned: Array<String> = arrayOf("", "")
    private var scannedImg: Array<Bitmap?> = arrayOf(null, null)

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || scanned.contains(result.text)) {
                // Prevent duplicate scans
                Toast.makeText(applicationContext, R.string.scan_already_scanned, Toast.LENGTH_SHORT).show()
                return
            }

            updateLayout(result.text, result.getBitmap())
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        init()
    }

    private fun init() {
        barcodeView = findViewById(R.id.barcode_scanner)
        val formats = Arrays.asList(BarcodeFormat.EAN_13)
        barcodeView!!.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView!!.initializeFromIntent(intent)
        barcodeView!!.setStatusText("")
        barcodeView!!.decodeContinuous(callback)
    }

    override fun onResume() {
        super.onResume()

        for (i:Int in 0 until scanned.size) {
            scanned[i] = ""
            scannedImg[i] = null
        }
        updateLayout()

        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView!!.pause()
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
    private fun updateLayout(text: String? = null, img: Bitmap? = null) {
        if (scanned.isNotEmpty()) {
            var filled = 0
            var found = false

            for (i:Int in 0 until scanned.size) {
                // Update arrays
                if (scanned[i] == "" && text != null) {
                    if (!found) {
                        scanned[i] = text
                        scannedImg[i] = img
                        found = true
                    }
                }

                // Count existing
                if (scanned[i] != "") {
                    filled++
                }

                // Update img
                val id: Int? = resources.getIdentifier("history_${i + 1}_image", "id", packageName)
                if (id != null) {
                    val historyImg = findViewById<ImageView>(id)
                    if (scannedImg[i] != null) {
                        historyImg.setImageBitmap(scannedImg[i])
                        fade("history_${i + 1}_close", 0f, 1f)
                    } else {
                        historyImg.setImageResource(android.R.color.transparent)
                        fade("history_${i + 1}_close", 1f, 0f)
                    }
                }
            }

            println("no")
            println(filled.toString())
            println("no")

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
        val historyNum: Int = Integer.parseInt(view.tag.toString()) - 1

        if (scanned[historyNum] !== null) {
            scanned[historyNum] = ""
            scannedImg[historyNum] = null
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
        if (scanned.isNotEmpty()) {
            for (i:Int in 0 until scanned.size) {
                if (scanned[i] == "") {
                    return
                }
            }

            val fightIntent = Intent(this, FightActivity::class.java)
            fightIntent.putExtra("SCANNED", scanned)
            saveScannedImg()
            startActivity(fightIntent)
        }
    }

    /**
     * Save scanned images
     */
    private fun saveScannedImg() {
        for (i:Int in 0 until scannedImg.size) {
            if (scannedImg[i] != null) {
                var fileName: String? = "scannedImg_${i + 1}"

                try {
                    val bytes = ByteArrayOutputStream()
                    scannedImg[i]!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val fo = openFileOutput(fileName, Context.MODE_PRIVATE)
                    fo.write(bytes.toByteArray())
                    // remember close file output
                    fo.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    fileName = null
                }
            }
        }
    }
}