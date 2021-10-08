package com.lmmarketings.fittslaw.drag

import android.app.ProgressDialog
import android.content.ClipData
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lmmarketings.fittslaw.*
import com.lmmarketings.fittslaw.utils.*
import kotlinx.android.synthetic.main.activity_drag.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

class DragActivity : AppCompatActivity() {
    private var start = 0
    private var end = 1
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
    var arrayOfTime = ArrayList<Long>()
    var distA = 0.0
    var distB = 0.0
    var distC = 0.0
    var startTime = 0.toLong()
    var endTime = 0.toLong()

    var arrayOfIds: Array<TextView>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val displaySize1 = Point()
        windowManager.defaultDisplay.getSize(displaySize1)
        displayWidth=displaySize1.x
        arrayOfDx.add(0.0)
        val amplitudeConst = intent.getDoubleExtra(keyAmplitude, 0.0)
        Log.d("amplitudeConst", amplitudeConst.toString())
        val widthConst = intent.getDoubleExtra(keyWidth, 0.0)
        Log.d("WidthConst", widthConst.toString())
        widthForMeasurement = convertCmToPixel(widthConst)
        arrayOfIds = arrayOf(
            drag_circle_1,
            drag_circle_2,
            drag_circle_3,
            drag_circle_4,
            drag_circle_5,
            drag_circle_6,
            drag_circle_7,
            drag_circle_8,
            drag_circle_9,
            drag_circle_10,
            drag_circle_11,
            drag_circle_12,
            drag_circle_13
        )
        for (i in 0..12) {
            arrayOfIds!![i].layoutParams.apply {
                height = widthForMeasurement
                width = widthForMeasurement
            }
        }
        val marginsCircle9 = drag_circle_9.layoutParams as MarginLayoutParams
        val marginToGive = (displayWidth - marginsCircle9.marginStart - widthForMeasurement) / 2
        for (i in 0..12) {
            var paramMargin=arrayOfIds!![i].layoutParams as MarginLayoutParams
            paramMargin.marginStart=paramMargin.marginStart+marginToGive
            arrayOfIds!![i].setOnLongClickListener(onLongClickListener)
            getCordinatesOfAll(arrayOfIds!![i], i)
        }

        setDragListner()

        btn_drag.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }


    val dragListener = View.OnDragListener { v, event ->
        val view = event!!.localState as View
        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {

            }
            DragEvent.ACTION_DRAG_EXITED -> {
            }
            DragEvent.ACTION_DROP -> {
                if (view.id == arrayOfIds!![start].id) {
                    val touchPosition = getTouchPositionFromDragEvent(v!!, event)
                    endTime = System.currentTimeMillis() - startTime
//                    Log.d(
//                        "cordinates", "\nstartX=${cordinatesHasMap.get(start)!![0]}\t" +
//                                "startY=${cordinatesHasMap.get(start)!![1]}\t" +
//                                "endX=${cordinatesHasMap.get(end)!![0]}\t" +
//                                "endY=${cordinatesHasMap.get(end)!![1]}\t" +
//                                "dropx=${touchPosition.x.toString()}\t" +
//                                "dropY=${touchPosition.y.toString()}"
//                    )
                    arrayOfTime.add(endTime)
                    touchX = touchPosition.x.toDouble()
                    touchY = touchPosition.y.toDouble()
                    arrayOfIds!![start].visibility = View.INVISIBLE

                    calculateDistances()
                    Dx = ((distC.pow(2)) - (distB.pow(2)) - (distA.pow(2))) / (2.0 * distA)
                    arrayOfDx.add(Dx)
                    Ae = distA + arrayOfDx[arrayOfDx.size - 1]
                    arrayOfAe.add(Ae)

                    if (end < 12) {
                        start = end
                        end = start + 1
                        arrayOfIds!![start].text = getString(R.string.drag_text)
                        arrayOfIds!![start].setBackgroundResource(R.drawable.start_bg)
                        arrayOfIds!![end].text = getString(R.string.drop_text)
                        arrayOfIds!![end].setBackgroundResource(R.drawable.end_bg)
                        setDragListner()
                    } else {
//                        arrayOfIds!![end].text = getString(R.string.end_text)
                        arrayOfIds!![end].apply {
                            text = getString(R.string.end_text)
                            setBackgroundResource(R.drawable.end_bg)
                            val progressDialog = ProgressDialog(this@DragActivity)
                            progressDialog.setTitle("Fitts' Law")
                            progressDialog.setMessage("Loading....")
                            progressDialog.setCancelable(false)
                            progressDialog.show()
                            Log.d("size", "${arrayOfDx.size} and ${arrayOfAe.size}")
                            var tempAe = 0.0
                            for (temp in arrayOfAe) {
                                tempAe += temp
                            }
                            meanAe = tempAe / 12
                            arrayOfDx.removeAt(0)
                            meanDx = 4.133 * calculateSD(arrayOfDx)
                            val ID = log2((meanAe * 2) / meanDx)
                            var tempTime = 0.toLong()
                            for (temp in arrayOfTime) {
                                tempTime += temp
                            }
                            meanTime = ((tempTime.toDouble()) / (1000 * 12))
                            var throughPut = ID / meanTime
                            Log.d(
                                "Calculations",
                                "Average Ae" + "${tempAe / 12}\t" + "Average width" + "$meanDx"
                            )

                            effectiveAmplitude = meanAe
                            effectiveWidth = meanDx
                            idMain = ID
                            meanTimeMain = meanTime
                            noOfMissesMain = missesNo
                            throughPutMain = throughPut
                            val intent = Intent(this@DragActivity, ResultActivity::class.java)
                            progressDialog.hide()
                            startActivity(intent)
                        }

                    }

                } else {
                    missesNo += 1
                }
            }
        }
        true
    }


    private val onLongClickListener = View.OnLongClickListener { v ->
        startTime = System.currentTimeMillis()
        if (v!!.id != arrayOfIds!![start].id) {
            missesNo += 1
        }
        val clipData = ClipData.newPlainText("", "")
        val shadow = View.DragShadowBuilder(v)
        v.startDrag(clipData, shadow, v, 0)
        true
    }

    private fun getTouchPositionFromDragEvent(item: View, event: DragEvent): Point {
        val rItem = Rect()
        item.getGlobalVisibleRect(rItem)
        return Point(
            rItem.left + (event.x / 2).toInt(),
            rItem.top + (event.y / 2).toInt()
        )
    }

    private fun calculateDistances() {
        distA = sqrt(
            (cordinatesHasMap[start]!![0].toDouble()
                    - cordinatesHasMap[end]!![0].toDouble()).pow(2.0)
                    + (cordinatesHasMap[start]!![1].toDouble()
                    - cordinatesHasMap[end]!![1].toDouble()).pow(2.0)
        )

        distB = sqrt(
            (touchX
                    - cordinatesHasMap[end]!![0].toDouble()).pow(2.0)
                    + (touchY - cordinatesHasMap[end]!![1]
                .toDouble()).pow(2.0)
        )

        distC = sqrt(
            (cordinatesHasMap[start]!![0].toDouble()
                    - touchX).pow(2.0)
                    + (cordinatesHasMap[start]!![1].toDouble()
                    - touchY).pow(2.0)
        )
    }

    private fun getCordinatesOfAll(v: View, position: Int) {
        val location = IntArray(2)
//        v.getLocationOnScreen(location)
//        val tempArray= arrayOf(location[0]+(widthForMeasurement/2),location[1]+(widthForMeasurement/2))
        val margins = v.layoutParams as MarginLayoutParams
        location[0] = margins.marginStart
        location[1] = margins.topMargin
        Log.d("X-margin", "$position" + location[0].toString())
        Log.d("Y-margin", "$position" + location[1].toString())
        val tempArray = arrayOf(
            location[0] + (widthForMeasurement / 2),
            location[1] + (widthForMeasurement / 2)
        )
        cordinatesHasMap[position] = tempArray
    }

    private fun convertCmToPixel(centimeters: Double): Int {
        Log.d("dpi", resources.displayMetrics.densityDpi.toString())
        return ((centimeters * resources.displayMetrics.densityDpi) / 2.54).toInt()
    }

    private fun setDragListner() {
        arrayOfIds!![end].setOnDragListener(dragListener)
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