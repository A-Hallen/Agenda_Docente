package com.hallen.agendadocente.horario

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.hallen.agendadocente.HoraryWidget
import com.hallen.agendadocente.R
import com.hallen.agendadocente.User

class HoraryActivityAdapter(private val context:Context, private var list:ArrayList<User>): BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = View.inflate(context, R.layout.grid_activity_item, null)
        val editText:EditText = view.findViewById(R.id.tv_activity_item)
        val item = list[position].name
        editText.id = list[position].id + 8
        editText.setText(item)
        customice(editText, list[position], context)
        return view
    }

    private fun customice(editText: EditText, user:User, context: Context) {
        if (user.row == 0){
            editText.background = getBackground(context)
            editText.isEnabled = false
            editText.setTextColor(context.resources.getColor(R.color.green))
        }
        editText.addTextChangedListener { guardarPreferencias(editText, user) }
    }

    private fun guardarPreferencias(editText: EditText, user: User) {
        val text = editText.text.toString()
        val col = user.col
        val row = user.row
        Log.i("GUARDAR_PREFERENCIAS", user.row.toString())
        val pref = HSaredPreferences(context)
        val nameOfCell = row.toString() + col.toString()
        pref.saveCell(text, nameOfCell)
        reloadWidget()
    }

    private fun reloadWidget() {
        val intent = Intent(context, HoraryWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, HoraryWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }

    private fun getBackground(context: Context): Drawable {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getDrawable(R.drawable.horary_header_background)!!
            } else {
                ContextCompat.getDrawable(context, R.drawable.horary_header_background)!!
            }
    }
}