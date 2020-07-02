package com.example.webscraper

import Chapter
import WebSiteData
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.helpers.OftenUsedMethods

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
        convertView.findViewById<TextView>(R.id.latestCh).text =
            OftenUsedMethods.getChapterDescription(website.lastNewChapter)
    }
    fun setLastRChapter(){
        convertView.findViewById<TextView>(R.id.lastRCh).text = OftenUsedMethods.getChapterDescription(website.lastReadedChapter)
    }

    fun setOpenWebsite(){
        convertView.findViewById<ImageButton>(R.id.open_website)
            .setOnClickListener(View.OnClickListener { view ->
               OftenUsedMethods.openWebsite(mainActivity,website.link)
            })
    }
    fun setBookmark(){
        convertView.findViewById<ImageButton>(R.id.click_bookmark)
            .setOnClickListener(View.OnClickListener { view ->
                if (chapter != website.lastReadedChapter) {
                    DataManagement.markAsReadNovel(website, chapter)
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