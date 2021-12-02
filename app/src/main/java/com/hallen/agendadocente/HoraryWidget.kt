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
val EXTRA_ITEM_POSITION = "extraItemPosition"
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
    val intent:Intent = Intent(context, HoraryActivity::class.java)
    val pendingIntent:PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    views.setOnClickPendingIntent(R.id.parentWidgetLayout, pendingIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}