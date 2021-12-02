package com.hallen.agendadocente

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

        Log.i("PREFS", "The onGetViewFactory function from ExampleWidgetService has been triggered")


        for(i in 1 until 14){
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

        override fun onCreate() {}
        override fun onDataSetChanged() {}
        override fun onDestroy() {}
        override fun getCount(): Int {
            return exampleData.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            Log.i("PREFS", "The getViewAt function from ExampleWidgetFactory class has been triggered")
            return RemoteViews(context.packageName, R.layout.grid_item).apply {
                setTextViewText(R.id.tv_item, exampleData[position])
                val fillIntent = Intent().apply {
                    Bundle().also { extras ->
                        extras.putInt(EXTRA_ITEM, position)
                        putExtras(extras)
                    }
                }
                setOnClickFillInIntent(R.id.tv_item, fillIntent)
            }
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