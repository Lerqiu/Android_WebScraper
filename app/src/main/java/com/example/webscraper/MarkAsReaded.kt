package com.example.webscraper

import WebSiteData
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import com.example.helpers.OftenUsedMethods
import java.util.*

class MarkAsReaded : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras

        if (extras?.containsKey("N_ID") == true) {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
                extras["N_ID"] as Int
            )
        }

        if (extras?.containsKey("MarkAsReaded") == true && extras.containsKey("Chapter") == true) {
            println("Sygnał uzyskany 1")

            val isEmpty = DataManagement.isEmpty()
            if (isEmpty)
                DataManagement.loadDataFromDisk(context)

            DataManagement.markChapterAsReaded(
                extras["MarkAsReaded"] as String,
                extras["Chapter"] as String
            )

            if (isEmpty)
                DataManagement.saveDataToDisk(context)
        } else if (extras?.containsKey("OpenWebsite") == true) {
            println("Sygnał uzyskany 2")
            val link = extras["OpenWebsite"] as String
            if (URLUtil.isValidUrl(link)){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse(link)
                context.startActivity(intent)
            }

        }


    }
}
