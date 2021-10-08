package com.lmmarketings.fittslaw

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lmmarketings.fittslaw.utils.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.math.abs
import kotlin.math.log2

class ResultActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        result_sequence.text="Sequence : $type $sequenceId "
        result_ae_before.text="Amplitude : ${convertCmToPixel(arrayOfAmplitudes[sequenceId-1]).toInt()} px"
        result_we_before.text="Width : ${convertCmToPixel(arrayOfWidth[sequenceId-1]).toInt()} px"
        result_id_before.text="ID : ${log2(((arrayOfAmplitudes[sequenceId-1]*2)/ arrayOfWidth[sequenceId-1]))} bits"
        result_ae_after.text="Ae : ${abs(effectiveAmplitude)} px"
        result_we_after.text="We : ${abs(effectiveWidth)} px"
        result_id_after.text="IDe : ${abs(idMain)} bits"
        result_mean_time.text="MT : ${abs(meanTimeMain)} sec"
        result_no_of_misses.text="Misses : ${abs(noOfMissesMain)}"
        result_throughtput.text="TPe : ${abs(throughPutMain)} bps"

        btn_result.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun convertCmToPixel(centimeters: Double): Int {
        Log.d("dpi", resources.displayMetrics.densityDpi.toString())
        return ((centimeters * resources.displayMetrics.densityDpi) / 2.54).toInt()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
    }
}