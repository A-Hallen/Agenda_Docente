package com.hallen.agendadocente

import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import java.lang.Exception
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.hallen.agendadocente.common.FloatingWindowCFG


class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var textView:TextView
    private lateinit var etBase:EditText
    private lateinit var cbInvertir:CheckBox
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        textView = findViewById(R.id.text_view)
        etBase   = findViewById(R.id.edit_text_base)
        cbInvertir=findViewById(R.id.cb_invertir)

        editText.addTextChangedListener { respuesta() }


        if (isMyServiceRunning()) {
            // onDestroy() method in FloatingWindowGFG
            // class will be called here
            stopService(Intent(this@MainActivity, FloatingWindowCFG::class.java))
        }
    }


    private fun isMyServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {

            if (FloatingWindowCFG::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
    private fun requestOverlayDisplayPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Screen Overlay Permission Needed")
        builder.setMessage("Enable 'Display over other apps' from System Settings.")
        builder.setPositiveButton(
            "Open Settings"
        ) { _, _ -> // The app will redirect to the 'Display over other apps' in Settings.
            // This is an Implicit Intent. This is needed when any Action is needed
            // to perform, here it is
            // redirecting to an other app(Settings).
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, RESULT_OK)
        }
        dialog = builder.create()
        dialog.show()
    }
    private fun checkOverlayDisplayPermission(): Boolean {
        // Android Version is lesser than Marshmallow or
        // the API is lesser than 23
        // doesn't need 'Display over other apps' permission enabling.
        return Settings.canDrawOverlays(this)
        // If 'Display over other apps' is not enabled
        // it will return false or else true

    }
    fun onClick(view: View){
        val focused = etBase.isFocused
        when(view.id){
            R.id.btn_7->{
                if (focused) etBase.text.append('7') else editText.text.append('7')
            }R.id.btn_8->{
            if (focused) etBase.text.append('8') else editText.text.append('8')
        }R.id.btn_9->{
            if (focused) etBase.text.append('9') else editText.text.append('9')
        }R.id.btn_4->{
            if (focused) etBase.text.append('4') else editText.text.append('4')
        }R.id.btn_5->{
            if (focused) etBase.text.append('5') else editText.text.append('5')
        }R.id.btn_6->{
            if (focused) etBase.text.append('6') else editText.text.append('6')
        }R.id.btn_1->{
            if (focused) etBase.text.append('1') else editText.text.append('1')
        }R.id.btn_2->{
            if (focused) etBase.text.append('2') else editText.text.append('2')
        }R.id.btn_3->{
            if (focused) etBase.text.append('3') else editText.text.append('3')
        }R.id.btn_0->{
            if (focused) etBase.text.append('0') else editText.text.append('0')
        }R.id.btn_point->{
            if (focused) etBase.text.append('.') else editText.text.append('.')
        }R.id.delete->{
            if (focused){ etBase.setText(""); textView.text = ""
            } else { editText.setText(""); textView.text = "" }
        }
            R.id._r->{
                respuesta()
            }R.id.btn_minimize->{
            if (checkOverlayDisplayPermission()) {
                // FloatingWindowGFG service is started
                startService(Intent(this@MainActivity, FloatingWindowCFG::class.java))
                // The MainActivity closes here
                finish()
            } else {
                // If permission is not given,
                // it shows the AlertDialog box and
                // redirects to the Settings
                requestOverlayDisplayPermission()
            }
        }
        }
    }

    private fun respuesta() {
        try {
            val base:Float = etBase.text.toString().toFloat()
            val entry:Float = editText.text.toString().toFloat()
            val res:Float
            if(base != 0F && entry != 0F){
                res = if (cbInvertir.isChecked){
                    entry / 100F * base
                } else {
                    entry / base * 100F
                }
                textView.text = res.toString()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}

