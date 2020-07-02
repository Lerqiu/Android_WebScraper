package com.example.helpers

import Chapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import android.widget.Toast
import com.example.webscraper.R

object OftenUsedMethods {

    fun openWebsite(activity: Activity,link: String) {
        var link = link
        if (link.subSequence(0, 4) != "http")
            link = """http://""" + link

        if (URLUtil.isValidUrl(link)) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            activity.startActivity(intent)
        } else {
            Toast.makeText(
                activity.applicationContext,
                activity.getString(R.string.Invalid_link) + link,
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun getChapterDescription(chapter:Chapter): String {
        var chapter_text = chapter.Name
        if (chapter_text.length == 0)
            chapter_text = chapter.Number
        return chapter_text
    }
}