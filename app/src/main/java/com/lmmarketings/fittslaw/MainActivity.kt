package com.lmmarketings.fittslaw

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lmmarketings.fittslaw.drag.DragActivity
import com.lmmarketings.fittslaw.touch.TouchActivity
import com.lmmarketings.fittslaw.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alertDialog=AlertDialog.Builder(this)
            .setTitle("Incompatible system reuirements")
            .setMessage("Please run this app on display size equal to \n1080 Px and 1920Px")
            .setCancelable(false)
            .setPositiveButton("Close App", DialogInterface.OnClickListener { dialogInterface, i ->
                this.finishAffinity() })
        val displaySize1 = Point()
        windowManager.defaultDisplay.getSize(displaySize1)
        displayWidth=displaySize1.x
//        val h=displaySize1.y
//        log.d
//        if ((displayWidth != 1080 ) && (h!=1920)){
//            alertDialog.show()
//        }
        privacy_and_policies.setOnClickListener {
            startActivity(Intent(this,Privacy::class.java))
        }
        btn_drag_1.setOnClickListener {
            type="Drag"
            sequenceId = 1
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[0])
            intent.putExtra(keyWidth, arrayOfWidth[0])
            startActivity(intent)
        }
        btn_drag_2.setOnClickListener {
            type="Drag"
            sequenceId = 2
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[1])
            intent.putExtra(keyWidth, arrayOfWidth[1])
            startActivity(intent)
        }
        btn_drag_3.setOnClickListener {
            type="Drag"
            sequenceId = 3
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[2])
            intent.putExtra(keyWidth, arrayOfWidth[2])
            startActivity(intent)
        }
        btn_drag_4.setOnClickListener {
            type="Drag"
            sequenceId = 4
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[3])
            intent.putExtra(keyWidth, arrayOfWidth[3])
            startActivity(intent)
        }
        btn_drag_5.setOnClickListener {
            type="Drag"
            sequenceId = 5
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[4])
            intent.putExtra(keyWidth, arrayOfWidth[4])
            startActivity(intent)
        }
        btn_drag_6.setOnClickListener {
            type="Drag"
            sequenceId = 6
            val intent = Intent(this, DragActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[5])
            intent.putExtra(keyWidth, arrayOfWidth[5])
            startActivity(intent)
        }

        btn_touch_1.setOnClickListener {
            type="Touch"
            sequenceId = 1
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[0])
            intent.putExtra(keyWidth, arrayOfWidth[0])
            startActivity(intent)
        }
        btn_touch_2.setOnClickListener {
            type="Touch"
            sequenceId = 2
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[1])
            intent.putExtra(keyWidth, arrayOfWidth[1])
            startActivity(intent)
        }
        btn_touch_3.setOnClickListener {
            type="Touch"
            sequenceId = 3
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[2])
            intent.putExtra(keyWidth, arrayOfWidth[2])
            startActivity(intent)
        }
        btn_touch_4.setOnClickListener {
            type="Touch"
            sequenceId = 4
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[3])
            intent.putExtra(keyWidth, arrayOfWidth[3])
            startActivity(intent)
        }
        btn_touch_5.setOnClickListener {
            type="Touch"
            sequenceId = 5
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[4])
            intent.putExtra(keyWidth, arrayOfWidth[4])
            startActivity(intent)
        }
        btn_touch_6.setOnClickListener {
            type="Touch"
            sequenceId = 6
            val intent = Intent(this, TouchActivity::class.java)
            intent.putExtra(keyAmplitude, arrayOfAmplitudes[5])
            intent.putExtra(keyWidth, arrayOfWidth[5])
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}