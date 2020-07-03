package com.example.webscraper

import Chapter
import WebSiteData
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.helpers.OftenUsedMethods
import com.example.helpers.OnBookMarkClick

class Info_about_novel_child(
    private val convertView: View,
    private val website: WebSiteData,
    private val context: Context,
    private val mainActivity: MainActivity,
    val chapter: Chapter,
    private val mainFragment: OnBookMarkClick
) {

    init {
        if (chapter.Number.length == 0) {
            convertView.findViewById<LinearLayout>(R.id.numberLL).visibility = View.GONE
        } else {
            convertView.findViewById<TextView>(R.id.number).text = chapter.Number
        }

        if (chapter.Name.length == 0) {
            convertView.findViewById<LinearLayout>(R.id.nameLL).visibility = View.GONE
        } else {
            convertView.findViewById<TextView>(R.id.name).text = chapter.Name
        }

        if (chapter.PublishedBy.length == 0) {
            convertView.findViewById<LinearLayout>(R.id.publishedLL).visibility = View.GONE
        } else {
            convertView.findViewById<TextView>(R.id.published).text = chapter.PublishedBy
        }

        if (chapter.timeOfAdd.length == 0) {
            convertView.findViewById<LinearLayout>(R.id.timeAddLL).visibility = View.GONE
        } else {
            convertView.findViewById<TextView>(R.id.timeAdd).text = chapter.timeOfAdd
        }
        setOpenBrowser()
        setBookmMark()
    }

    fun setOpenBrowser() {
        convertView.findViewById<ImageButton>(R.id.ImageButton_web)
            .setOnClickListener(View.OnClickListener { view ->
                OftenUsedMethods.openWebsite(mainActivity,chapter.link)
            })
    }

    fun setBookmMark() {
        if (chapter == website.lastReadedChapter) {
            convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
                .setColorFilter(Color.LTGRAY)
            convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark).isClickable = false
        }
        convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
            .setOnClickListener(View.OnClickListener { view ->
                if (chapter!= website.lastReadedChapter) {
                    DataManagement.markChapterAsReaded(website.link, chapter.toString())
                    convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
                        .setColorFilter(Color.LTGRAY)
                    convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark).isClickable =
                        false
                    mainFragment.handleOnBookMarkClick(chapter)
                    website.lastReadedChapter=chapter //operacje na kopii
                }
            })

        /*
        convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
            .setOnClickListener(View.OnClickListener { view ->
                website.lastReadedChapter = chapter
                DataManagement.markAsReadNovel(website, chapter)
                mainFragment.handleOnBookMarkClick(chapter)
            })*/
    }
}