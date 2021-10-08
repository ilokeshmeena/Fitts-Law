package com.lmmarketings.fittslaw.touch

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.lmmarketings.fittslaw.MainActivity
import com.lmmarketings.fittslaw.R
import com.lmmarketings.fittslaw.ResultActivity
import com.lmmarketings.fittslaw.utils.*
import kotlinx.android.synthetic.main.activity_drag.*
import kotlinx.android.synthetic.main.activity_touch.*
import android.view.ViewGroup.MarginLayoutParams
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

class TouchActivity : AppCompatActivity() {
    var clickCurrent = 0
    private var lastClickedCordinateX = 0
    private var lastClickedCordinateY = 0
    var widthForMeasurement: Int = 0
    var cordinatesHasMap = HashMap<Int, Array<Int>>()
    var touchX = 0.0
    var touchY = 0.0
    var missesNo = 0
    var Dx = 0.0
    var Ae = 0.0
    var meanAe = 0.0
    var meanDx = 0.0
    var meanTime = 0.0
    var arrayOfAe = ArrayList<Double>()
    var arrayOfDx = ArrayList<Double>()
    var distA = 0.0
    var distB = 0.0
    var distC = 0.0
    var startTime = 0.toLong()
    var endTime = 0.toLong()
    var arrayOfIds: Array<TextView>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val displaySize1 = Point()
        windowManager.defaultDisplay.getSize(displaySize1)
        displayWidth=displaySize1.x
        val amplitudeConst = intent.getDoubleExtra(keyAmplitude, 0.0)
        Log.d("amplitudeConst", amplitudeConst.toString())
        val widthConst = intent.getDoubleExtra(keyWidth, 0.0)
        Log.d("WidthConst", widthConst.toString())
        widthForMeasurement = convertCmToPixel(widthConst)
        arrayOfIds = arrayOf(
            tocuh_circle_1,
            tocuh_circle_2,
            tocuh_circle_3,
            tocuh_circle_4,
            tocuh_circle_5,
            tocuh_circle_6,
            tocuh_circle_7,
            tocuh_circle_8,
            tocuh_circle_9,
            tocuh_circle_10,
            tocuh_circle_11,
            tocuh_circle_12,
            tocuh_circle_13
        )
        for (i in 0..12) {
            arrayOfIds!![i].layoutParams.apply {
                height = widthForMeasurement
                width = widthForMeasurement
            }
        }
        val marginsCircle9 = tocuh_circle_9.layoutParams as MarginLayoutParams
        val marginToGive = (displayWidth - marginsCircle9.marginStart - widthForMeasurement) / 2
        for (i in 0..12) {
            var paramMargin = arrayOfIds!![i].layoutParams as MarginLayoutParams
            paramMargin.marginStart = paramMargin.marginStart + marginToGive
            arrayOfIds!![i].apply {
                setOnClickListener(clickListener)
                setOnTouchListener(touchListener)
            }
            getCordinatesOfAll(arrayOfIds!![i], i)
        }

        btn_touch.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    val touchListener = View.OnTouchListener { v, event ->
        if (event!!.actionMasked == MotionEvent.ACTION_DOWN) {
            lastClickedCordinateX = event.getRawX().toInt()
            lastClickedCordinateY = event.getRawY().toInt()
        }
        false
    }

    private val clickListener = View.OnClickListener {
//        Log.d(
//            "cordinates", "\nstartX=${cordinatesHasMap[clickCurrent]!![0]}\t" +
//                    "startY=${cordinatesHasMap[clickCurrent]!![1]}\t" +
//                    "endX=${cordinatesHasMap[clickCurrent + 1]!![0]}\t" +
//                    "endY=${cordinatesHasMap[clickCurrent + 1]!![1]}\t" +
//                    "dropx=${lastClickedCordinateX.toString()}\t" +
//                    "dropY=${lastClickedCordinateY.toString()}"
//        )
        if (it.id == arrayOfIds!![clickCurrent].id) {
            arrayOfIds!![clickCurrent].visibility = View.INVISIBLE
            when {
                clickCurrent == 0 -> {
                    startTime = System.currentTimeMillis()
                    arrayOfIds!![clickCurrent + 1].apply {
                        text = "Click"
                        setBackgroundResource(R.drawable.start_bg)
                    }

                }
                clickCurrent <= 12 -> {
                    calculateDistances()
                    Dx = ((distC.pow(2)) - (distB.pow(2)) - (distA.pow(2))) / (2.0 * distA)
                    arrayOfDx.add(Dx)
                    Ae = distA + arrayOfDx[arrayOfDx.size - 1]
                    arrayOfAe.add(Ae)
                    if (clickCurrent < 12) {
                        arrayOfIds!![clickCurrent + 1].apply {
                            text = "Click"
                            setBackgroundResource(R.drawable.start_bg)
                        }
                    } else {
                        endTime = System.currentTimeMillis()
                        val progressDialog = ProgressDialog(this)
                        progressDialog.setTitle("Fitts' Law")
                        progressDialog.setMessage("Loading....")
                        progressDialog.setCancelable(false)
                        progressDialog.show()

                        var tempAe = 0.0
                        for (temp in arrayOfAe) {
                            tempAe += temp
                        }
                        meanAe = tempAe / 12
                        arrayOfDx.removeAt(0)
                        meanDx = 4.133 * calculateSD(arrayOfDx)
                        val ID = log2((meanAe * 2) / meanDx)
                        meanTime = ((endTime - startTime).toDouble() / (12 * 1000))
                        var throughPut = ID / meanTime
                        effectiveAmplitude = meanAe
                        effectiveWidth = meanDx
                        idMain = ID
                        meanTimeMain = meanTime
                        noOfMissesMain = missesNo
                        throughPutMain = throughPut
                        val intent = Intent(this, ResultActivity::class.java)
                        progressDialog.hide()
                        startActivity(intent)

                    }
                }
            }
            clickCurrent += 1
        } else {
            missesNo += 1
        }

    }

    private fun convertCmToPixel(centimeters: Double): Int {
        Log.d("dpi", resources.displayMetrics.densityDpi.toString())
        return ((centimeters * resources.displayMetrics.densityDpi) / 2.54).toInt()
    }

    private fun getCordinatesOfAll(v: View, position: Int) {
        val location = IntArray(2)
//        v.getLocationOnScreen(location)
//        val tempArray= arrayOf(location[0]+(widthForMeasurement/2),location[1]+(widthForMeasurement/2))
        val margins = v.layoutParams as MarginLayoutParams
        location[0] = margins.marginStart
        location[1] = margins.topMargin
        val tempArray = arrayOf(
            location[0] + (widthForMeasurement / 2),
            location[1] + (widthForMeasurement / 2)
        )
        Log.d("X-margin", "${position + 1} : " + tempArray[0].toString())
        Log.d("Y-margin+", "${position + 1} : " + tempArray[1].toString())
        cordinatesHasMap[position] = tempArray
    }

    private fun calculateDistances() {
        distA = sqrt(
            (cordinatesHasMap[clickCurrent - 1]!![0].toDouble()
                    - cordinatesHasMap[clickCurrent]!![0].toDouble()).pow(2.0)
                    + (cordinatesHasMap[clickCurrent - 1]!![1].toDouble()
                    - cordinatesHasMap[clickCurrent]!![1].toDouble()).pow(2.0)
        )

        distB = sqrt(
            (touchX
                    - cordinatesHasMap[clickCurrent]!![0].toDouble()).pow(2.0)
                    + (touchY - cordinatesHasMap[clickCurrent]!![1]
                .toDouble()).pow(2.0)
        )

        distC = sqrt(
            (cordinatesHasMap[clickCurrent - 1]!![0].toDouble()
                    - touchX).pow(2.0)
                    + (cordinatesHasMap[clickCurrent - 1]!![1].toDouble()
                    - touchY).pow(2.0)
        )
    }

    private fun calculateSD(numArray: ArrayList<Double>): Double {
        var sum = 0.0
        var standardDeviation = 0.0

        for (num in numArray) {
            sum += num
        }

        val mean = sum / 12

        for (num in numArray) {
            standardDeviation += (num - mean).pow(2.0)
        }

        return sqrt(standardDeviation / 12)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
    @Override
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
}