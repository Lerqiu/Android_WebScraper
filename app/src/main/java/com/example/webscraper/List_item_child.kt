package com.example.webscraper

import WebSiteData
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.helpers.OftenUsedMethods
import com.example.helpers.OnBookMarkClick

class List_item_child(
    val convertView: View,
    val website: WebSiteData,
    val context: Context,
    val mainActivity: MainActivity,
    val mainFragmentBookMarkClick: OnBookMarkClick
) {

    init {
        setAuthor()
        setChapterLastReaded()
        setChapterLatest()
        setNotificationButton()
        setWebButton()
        setBookMarkButton()
        setInfoButton()
    }

    fun setAuthor() {
        convertView.findViewById<TextView>(R.id.list_item_child_layout_text).text = website.Author
    }

    fun setChapterLastReaded() {
        val chapter_text = OftenUsedMethods.getChapterDescription(website.lastReadedChapter)

        if (chapter_text.length > 0)
            convertView.findViewById<TextView>(R.id.lastReaded).text =
                chapter_text
    }

    fun setChapterLatest() {
        val chapter_text = OftenUsedMethods.getChapterDescription(website.lastNewChapter)
        if (chapter_text.length > 0)
            convertView.findViewById<TextView>(R.id.list_item_child_layout_chapter).text =
                chapter_text
    }

    fun setNotificationButton() {
        val button = convertView.findViewById<ImageButton>(R.id.ImageButton_notifications)
        if (DataManagement.getWebsiteNotifications(website) == false)
            button.setColorFilter(Color.LTGRAY)

        button.setOnClickListener(View.OnClickListener { view ->
            if (DataManagement.getWebsiteNotifications(website)) {
                button.setColorFilter(Color.LTGRAY)
                DataManagement.setWebsiteNotifications(website, false)
            } else {
                button.setColorFilter(convertView.findViewById<ImageButton>(R.id.ImageButton_web).colorFilter)
                DataManagement.setWebsiteNotifications(website, true)
            }
        })
    }

    fun setWebButton() {
        convertView.findViewById<ImageButton>(R.id.ImageButton_web)
            .setOnClickListener(View.OnClickListener { view ->
                OftenUsedMethods.openWebsite(mainActivity, website.link)
            })
    }

    fun setBookMarkButton() {
        if (website.lastNewChapter == website.lastReadedChapter) {
            convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
                .setColorFilter(Color.LTGRAY)
            convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark).isClickable = false
        }
        convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
            .setOnClickListener(View.OnClickListener { view ->
                if (website.lastNewChapter != website.lastReadedChapter) {
                    DataManagement.markAsReadNovel(website, website.lastNewChapter)
                    convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
                        .setColorFilter(Color.LTGRAY)
                    convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark).isClickable =
                        false
                    website.lastReadedChapter = website.lastNewChapter //operacje na kopii
                    this.setChapterLastReaded()
                }
                mainFragmentBookMarkClick.handleOnBookMarkClick(website.lastNewChapter)
            })
    }

    fun setInfoButton() {
        convertView.findViewById<ImageButton>(R.id.ImageButton_info)
            .setOnClickListener(View.OnClickListener { view ->
                mainActivity.setMainLayout_info_about_novel(website)
            })
    }
}