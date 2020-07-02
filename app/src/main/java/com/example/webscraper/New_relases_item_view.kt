package com.example.webscraper

import Chapter
import WebSiteData
import android.content.Context
import android.view.View
import android.widget.*
import com.example.helpers.OftenUsedMethods

class New_relases_item_view(
    private val convertView: View,
    private val website: WebSiteData,
    private val chapter: Chapter,
    private val context: Context,
    private val mainActivity: MainActivity
) {

    init {
        convertView.findViewById<TextView>(R.id.title).text = website.Title

        var chapterText = chapter.Name
        if (chapterText.length == 0)
            chapterText = chapter.Number
        convertView.findViewById<TextView>(R.id.chapter).text = chapterText

        setOpenBrowser()
    }

    fun setOpenBrowser() {
        convertView.findViewById<ImageView>(R.id.open_browser)
            .setOnClickListener(View.OnClickListener { view ->
               OftenUsedMethods.openWebsite(mainActivity,chapter.link)
            })

    }
}