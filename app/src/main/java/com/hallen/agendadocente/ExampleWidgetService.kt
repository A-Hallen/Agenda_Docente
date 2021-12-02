package com.hallen.agendadocente

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.hallen.agendadocente.horario.HSaredPreferences

class ExampleWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val list  = arrayListOf<String>()
        //Instantiate the database

        //Now we read the Data

        list.add("T") //0
        list.add("L") //1
        list.add("M") //2
        list.add("X") //3
        list.add("J") //4
        list.add("V") //5
        list.add("-") //6
        list.add("-") //7

        val prefs = HSaredPreferences(applicationContext)

        prefs.saveCell("10`1", "00")
        prefs.saveCell("12`2", "05")
        prefs.saveCell("12`1", "85")

        for(i in 0 until 10){
            for(e in 0 until 8){
                val content = prefs.getCell("$i$e")
                list.add(content)
            }
        }
        return ExampleWidgetItemFactory(applicationContext, list)
    }

    internal inner class ExampleWidgetItemFactory(private val context: Context, list:ArrayList<String>) :
        RemoteViewsFactory {

        private val exampleData = list
        /* private val exampleData = arrayOf(
            "T", "L",     "M",    "X",    "J",     "V",    "",     "",
            "1",  "",     "12º3", "",     "",      "10º1", "7:45", "8:30",
            "2",  "",     "12º2", "",     "",      "",     "8:35", "9:20",
            "3",  "",     "10º2", "",     "",      "",     "9:25", "10:10",
            "R",  "",     "",     "",     "",      "",     "", "",
            "4",  "10º1", "",     "10º2", "",      "",     "10:25", "11:10",
            "5",  "12º2", "",     "",     "12º3",  "",     "11:15", "12:00",
            "M",  "",     "",     "",     "",      "",     "",      "",
            "1",  "",     "",     "",     "",      "",     "12:15", "1:00",
            "2",  "",     "",     "",     "",      "",     "1:05", "1:50",
            "3",  "",     "",     "",     "",      "",     "1:55", "2:40",
            "4",  "",     "",     "",     "",      "",     "3:00", "3:45",
            "5",  "",     "",     "",     "",      "",     "3:50", "4:35"
        )
        */

        override fun onCreate() {}
        override fun onDataSetChanged() {}
        override fun onDestroy() {}
        override fun getCount(): Int {
            return exampleData.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.grid_item)
            views.setTextViewText(R.id.tv_item, exampleData[position])
            return views
        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.loading_remote_view)
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}