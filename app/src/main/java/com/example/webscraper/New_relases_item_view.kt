package com.example.webscraper

import Chapter
import WebSiteData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.*

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
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(chapter.link)
                context.startActivity(intent)
            })

    }
}