package com.example.helpers

import Chapter
import DataManagement
import UpdateData
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.webscraper.MainActivity
import com.example.webscraper.R
import newRelase


object OftenUsedMethods {

    fun openWebsite(activity: Context, link: String) {
        var link = link
        if (link.subSequence(0, 4) != "http")
            link = """http://""" + link

        if (URLUtil.isValidUrl(link)) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            activity.startActivity(intent)
            UpdateData.addToLog("Otwarcie strony: " + link)
        } else {
            Toast.makeText(
                activity.applicationContext,
                activity.getString(R.string.Invalid_link) + link,
                Toast.LENGTH_LONG
            ).show()
        }


    }

    fun getChapterDescription(chapter: Chapter): String {
        var chapter_text = chapter.Name
        if (chapter_text.length == 0)
            chapter_text = chapter.Number
        return chapter_text
    }

    private var NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    fun setNotification(website: newRelase) {

        if (DataManagement.getNotificationStatus() == false) return

        try {
            val mainActivity: MainActivity = MainActivity.STATICMainActivity!!
            NOTIFICATION_ID++


            //<------->
            val intent = Intent(mainActivity, MainActivity::class.java).apply {
                putExtra("OpenFragment", website.web.link)
                setAction("OpenFragment")
            }
            val pendingIntent: PendingIntent? = TaskStackBuilder.create(mainActivity).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            //<------->
            val intentMarkAsReaded = Intent(mainActivity, MarkAsReaded::class.java).apply {
                putExtra("MarkAsReaded", website.web.link)
                putExtra("Chapter", website.chap.toString())
                putExtra("N_ID", NOTIFICATION_ID)
                setAction("MarkAsRead " + NOTIFICATION_ID.toString())
            }
            val pendingIntentMarkAsReaded: PendingIntent? = PendingIntent.getBroadcast(
                mainActivity,
                0,
                intentMarkAsReaded,
                PendingIntent.FLAG_ONE_SHOT
            )

            //<------->
            val intentOpenWebsite = Intent(mainActivity, MarkAsReaded::class.java).apply {
                putExtra("OpenWebsite", website.chap.link)
                putExtra("N_ID", NOTIFICATION_ID)
                setAction("OpenWebsite " + NOTIFICATION_ID.toString())
                //setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pendingIntentOpenWebsite: PendingIntent? = PendingIntent.getBroadcast(
                mainActivity,
                0,
                intentOpenWebsite,
                PendingIntent.FLAG_ONE_SHOT
            )


            var notificationManager: NotificationManager
            notificationManager =
                mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
                )

                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.description = "Messages Notification"
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(mainActivity, PRIMARY_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_book_info)
                setContentTitle(website.web.Title)
                setContentText(
                    mainActivity.getString(R.string.Latest_chapter) + " " + getChapterDescription(
                        website.chap
                    )
                )
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                addAction(
                    R.drawable.ic_bookmark,
                    mainActivity.getString(R.string.Mark_as_read),
                    pendingIntentMarkAsReaded
                ).setAutoCancel(true)
                addAction(
                    R.drawable.ic_book_info,
                    mainActivity.getString(R.string.Open_novel),
                    pendingIntentOpenWebsite
                ).setAutoCancel(true)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            // displaying the notification with NotificationManagerCompat.
            with(NotificationManagerCompat.from(mainActivity.application)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        } catch (e: Exception) {
            Log.e("Powiadomienia", "Problem tworzenia\n" + e)
        }

    }

    fun isNetworkAvailable(): Boolean {
        var state = false
        try {
            val connectivityManager =
                MainActivity.STATICMainActivity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
            state = activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
        return state
    }

}
