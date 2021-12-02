package com.hallen.agendadocente.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Build
import android.graphics.PixelFormat
import android.view.*
import android.view.View.OnTouchListener
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.hallen.agendadocente.MainActivity
import com.hallen.agendadocente.R
import java.lang.Exception

class FloatingWindowCFG : Service() {
    // The reference variables for the
    // ViewGroup, WindowManager.LayoutParams,
    // WindowManager, Button, EditText classes are created
    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private lateinit var etBase: EditText
    private lateinit var cbInvertir: CheckBox
    private lateinit var sbTransparence:SeekBar
    private lateinit var llTransparence:LinearLayout
    private lateinit var llShadow:LinearLayout
    private var floatView: ViewGroup? = null
    private var floatWindowLayoutParam: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null

    // As FloatingWindowGFG inherits Service class,
    // it actually overrides the onBind method
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        // The screen height and width are calculated, cause
        // the height and width of the floating window is set depending on this
        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        // To obtain a WindowManager of a different Display,
        // we need a Context for that display, so WINDOW_SERVICE is used
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // A LayoutInflater instance is created to retrieve the
        // LayoutInflater for the floating_layout xml
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate a new view hierarchy from the floating_layout xml
        floatView = inflater.inflate(R.layout.floating_layout, null) as ViewGroup

        floatView!!.clipChildren = true
        editText = floatView!!.findViewById(R.id.edit_texts)
        textView = floatView!!.findViewById(R.id.text_views)
        etBase   = floatView!!.findViewById(R.id.edit_text_bases)
        cbInvertir=floatView!!.findViewById(R.id.cb_invertirs)
        llTransparence=floatView!!.findViewById(R.id.ll_transparence)
        sbTransparence=floatView!!.findViewById(R.id.sb_transparence)
        llShadow = floatView!!.findViewById(R.id.ll_shadow)
        sbTransparence.max = 100
        sbTransparence.progress = 100
        var progress = 1F
        sbTransparence.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                progress = p0!!.progress.toFloat() / 100F

                if (progress >= 0.25F){
                    floatView!!.alpha = progress
                } else progress = 0.25F

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {          }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                val shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
                fadeOut(llTransparence, shortAnimationDuration.toLong() * 2L)
                llShadow.visibility = View.GONE
            }
        })
        editText.addTextChangedListener { respuesta() }

        // The Buttons and the EditText are connected with
        // the corresponding component id used in floating_layout xml file
        val closeBtn = floatView!!.findViewById<ImageView>(R.id.btn_close)


        // WindowManager.LayoutParams takes a lot of parameters to set the
        // the parameters of the layout. One of them is Layout_type.
        val LAYOUT_TYPE: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // If API Level is more than 26, we need TYPE_APPLICATION_OVERLAY
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            // If API Level is lesser than 26, then we can
            // use TYPE_SYSTEM_ERROR,
            // TYPE_SYSTEM_OVERLAY, TYPE_PHONE, TYPE_PRIORITY_PHONE.
            // But these are all
            // deprecated in API 26 and later. Here TYPE_TOAST works best.
            WindowManager.LayoutParams.TYPE_TOAST
        }

        // Now the Parameter of the floating-window layout is set.
        // 1) The Width of the window will be 55% of the phone width.
        // 2) The Height of the window will be 58% of the phone height.
        // 3) Layout_Type is already set.
        // 4) Next Parameter is Window_Flag. Here FLAG_NOT_FOCUSABLE is used. But
        // problem with this flag is key inputs can't be given to the EditText.
        // This problem is solved later.
        // 5) Next parameter is Layout_Format. System chooses a format that supports
        // translucency by PixelFormat.TRANSLUCENT
        floatWindowLayoutParam = WindowManager.LayoutParams(
            (width * 0.50f).toInt(),
            (height * 0.45f).toInt(),
            LAYOUT_TYPE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // The Gravity of the Floating Window is set.
        // The Window will appear in the center of the screen
        floatWindowLayoutParam!!.gravity = Gravity.CENTER

        // X and Y value of the window is set
        floatWindowLayoutParam!!.x = 0
        floatWindowLayoutParam!!.y = 0

        // The ViewGroup that inflates the floating_layout.xml is
        // added to the WindowManager with all the parameters
        windowManager!!.addView(floatView, floatWindowLayoutParam)

        closeBtn.setOnClickListener {
            stopSelf()
            // The window is removed from the screen
            windowManager!!.removeView(floatView)
        }

        // The EditText string will be stored
        // in currentDesc while writing
        // Another feature of the floating window is, the window is movable.
        // The window can be moved at any position on the screen.
        floatView!!.setOnTouchListener(object : OnTouchListener {
            val floatWindowLayoutUpdateParam: WindowManager.LayoutParams = floatWindowLayoutParam as WindowManager.LayoutParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        floatView!!.alpha = 1F
                        x = floatWindowLayoutUpdateParam.x.toDouble()
                        y = floatWindowLayoutUpdateParam.y.toDouble()

                        // returns the original raw X
                        // coordinate of this event
                        px = event.rawX.toDouble()

                        // returns the original raw Y
                        // coordinate of this event
                        py = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        floatWindowLayoutUpdateParam.x = (x + event.rawX - px).toInt()
                        floatWindowLayoutUpdateParam.y = (y + event.rawY - py).toInt()

                        // updated parameter is applied to the WindowManager
                        windowManager!!.updateViewLayout(floatView, floatWindowLayoutUpdateParam)
                    }
                    MotionEvent.ACTION_UP ->{
                        floatView!!.alpha = progress
                    }
                }
                return false
            }
        })

        // Floating Window Layout Flag is set to FLAG_NOT_FOCUSABLE,
        // so no input is possible to the EditText. But that's a problem.
        // So, the problem is solved here. The Layout Flag is
        // changed when the EditText is touched.
    }

    // It is called when stopService()
    // method is called in MainActivity
    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        // Window is removed from the screen
        windowManager!!.removeView(floatView)
    }
    fun onClick(view: View){
        val focus = etBase.isFocused
        when(view.id){
            R.id.btn_7s->{
                if (focus) etBase.text.append('7') else editText.text.append('7')
            }R.id.btn_8s->{
            if (focus) etBase.text.append('8') else editText.text.append('8')
        }R.id.btn_9s->{
            if (focus) etBase.text.append('9') else editText.text.append('9')
        }R.id.btn_4s->{
            if (focus) etBase.text.append('4') else editText.text.append('4')
        }R.id.btn_5s->{
            if (focus) etBase.text.append('5') else editText.text.append('5')
        }R.id.btn_6s->{
            if (focus) etBase.text.append('6') else editText.text.append('6')
        }R.id.btn_1s->{
            if (focus) etBase.text.append('1') else editText.text.append('1')
        }R.id.btn_2s->{
            if (focus) etBase.text.append('2') else editText.text.append('2')
        }R.id.btn_3s->{
            if (focus) etBase.text.append('3') else editText.text.append('3')
        }R.id.btn_0s->{
            if (focus) etBase.text.append('0') else editText.text.append('0')
        }R.id.btn_points->{
            if (focus) etBase.text.append('.') else editText.text.append('.')
        }R.id.deletes->{
            if (focus){ etBase.setText(""); textView.text = ""
            } else { editText.setText(""); textView.text = "" }
        }R.id._rs->{
            respuesta()
        }R.id.btn_transparence->{
            if(llTransparence.visibility == View.INVISIBLE){
                fadeIn(llTransparence)
                llShadow.visibility = View.VISIBLE
            } else {
                val longAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)
                fadeOut(llTransparence, longAnimationDuration.toLong())
            }
        }R.id.ll_shadow->{
            val longAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)
            fadeOut(llTransparence, longAnimationDuration.toLong())
            llShadow.visibility = View.GONE
        }R.id.btn_minimizes->{
            stopSelf()
            windowManager!!.removeView(floatView)
            val backToHome = Intent(this@FloatingWindowCFG, MainActivity::class.java)
            backToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(backToHome)
        }
        }
    }
    private fun fadeIn(view: View) {
        val shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        view.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(shortAnimationDuration.toLong()).setListener(null)
        }
        view.visibility = View.VISIBLE
    }

    private fun fadeOut(view: View, time: Long) {
        view.apply {
            alpha = 1f
            animate().alpha(0f).setDuration(time).setListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE
                }
            })
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
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}