package com.hallen.agendadocente
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.hallen.agendadocente.horario.HoraryActivity

/**
 * Implementation of App Widget functionality.
 */
const val TOAST_ACTION = "com.hallen.agendadocente.HoraryWidget.TOAST_ACTION"
const val EXTRA_ITEM = "com.hallen.agendadocente.HoraryWidget.EXTRA_ITEM"

class HoraryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    //Esta funcion se va a activar cuando demos click en el widget de la app
    override fun onReceive(context: Context?, intent: Intent?) {
        val mrg:AppWidgetManager = AppWidgetManager.getInstance(context)
        if (intent?.action == TOAST_ACTION){
            val appWidgetId: Int = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID)
            //La variable viewIndex almacena la posicion del item que se a pulsado
            val viewIndex: Int = intent.getIntExtra(EXTRA_ITEM, 0)
            //Ahora vamos a lanzar un intent a la actividad HoraryActivity para que se configure el Widget ok?
            val horaryIntent:Intent = Intent(context, HoraryActivity::class.java)
            horaryIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //val pendingIntent:PendingIntent = PendingIntent.getActivity(context, 0, horaryIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            context?.startActivity(horaryIntent)
        }
        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    //val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.horary_widget)
    //views.setTextViewText(R.id.appwidget_text, widgetText)
    val serviceIntent = Intent(context, ExampleWidgetService::class.java)
    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
    views.setRemoteAdapter(R.id.gb_horary, serviceIntent)

    val toastPendingIntent: PendingIntent = Intent(
        context, HoraryWidget::class.java).run {
            //Set the action for the intent
            //When the user touch a particular view, it will have a particular effect of
            //broadcast toast action
            action = TOAST_ACTION
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    views.setPendingIntentTemplate(R.id.gb_horary, toastPendingIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}