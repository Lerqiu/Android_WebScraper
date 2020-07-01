package com.example.webscraper

import Chapter
import WebSiteData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class New_relases_item_view_child(
    private val convertView: View,
    private val website: WebSiteData,
    private val chapter: Chapter,
    private val context: Context,
    private val mainActivity: MainActivity
) {

    init {
        setLatestChapter()
        setLastRChapter()
        setOpenWebsite()
        setOpenBrowser()
        setBookmark()
    }
    fun setLatestChapter(){
        var chapterText = website.lastNewChapter.Name
        if (chapterText .length == 0)
            chapterText  = website.lastNewChapter.Number
        convertView.findViewById<TextView>(R.id.latestCh).text = chapterText
    }
    fun setLastRChapter(){
        var chapterText = website.lastReadedChapter.Name
        if (chapterText .length == 0)
            chapterText  = website.lastReadedChapter.Number
        convertView.findViewById<TextView>(R.id.lastRCh).text = chapterText
    }

    fun setOpenWebsite(){
        convertView.findViewById<ImageButton>(R.id.open_website)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(website.link)
                context.startActivity(intent)
            })
    }
    fun setBookmark(){
        convertView.findViewById<ImageButton>(R.id.click_bookmark)
            .setOnClickListener(View.OnClickListener { view ->
                if (website.lastNewChapter != website.lastReadedChapter) {
                    DataManagement.markAsReadNovel(website, website.lastNewChapter)
                    convertView.findViewById<ImageButton>(R.id.click_bookmark)
                        .setColorFilter(Color.LTGRAY)
                    convertView.findViewById<ImageButton>(R.id.click_bookmark).isClickable =
                        false
                }
            })
    }

    fun setOpenBrowser() {
       convertView.findViewById<ImageButton>(R.id.open_info)
            .setOnClickListener(View.OnClickListener { view ->
                mainActivity.setMainLayout_info_about_novel(website)
            })
    }
}