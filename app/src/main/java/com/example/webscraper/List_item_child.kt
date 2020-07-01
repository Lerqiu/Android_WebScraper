package com.example.webscraper

import WebSiteData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class List_item_child(
    val convertView: View,
    val website: WebSiteData,
    val context: Context,
    val mainActivity: MainActivity,
    val mainFragmentBookMarkClick: OnBookMarkClick
) {

    init {
        setAuthor()
        setChapterDescription()
        setEmailButton()
        setWebButton()
        setBookMarkButton()
        setInfoButton()
    }

    fun setAuthor() {
        convertView.findViewById<TextView>(R.id.list_item_child_layout_text).text = website.Author
    }

    fun setChapterDescription() {
        var chapter_text = website.lastNewChapter.Name
        if (chapter_text.length != 0 && website.lastNewChapter.Number.length != 0)
            chapter_text += " <=> " + website.lastNewChapter.Number
        else
            chapter_text += website.lastNewChapter.Number

        convertView.findViewById<TextView>(R.id.list_item_child_layout_chapter).text =
            chapter_text
    }

    fun setEmailButton() {
        if (website.sendEmail == false || DataManagement.isEmailAccepted() == false)
            convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                .setColorFilter(Color.LTGRAY)

        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
            .setOnClickListener(View.OnClickListener { view ->
                if (DataManagement.isEmailAccepted()) {
                    if (website.sendEmail == true) {
                        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                            .setColorFilter(Color.LTGRAY)
                        website.sendEmail = false
                    } else {
                        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                            .setColorFilter(convertView.findViewById<ImageButton>(R.id.ImageButton_web).colorFilter)
                        website.sendEmail = true
                    }
                }
            })
    }

    fun setWebButton() {
        convertView.findViewById<ImageButton>(R.id.ImageButton_web)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(website.link)
                context.startActivity(intent)
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
                }
                mainFragmentBookMarkClick.handleOnBookMarkClick()
            })
    }

    fun setInfoButton() {
        convertView.findViewById<ImageButton>(R.id.ImageButton_info)
            .setOnClickListener(View.OnClickListener { view ->
                mainActivity.setMainLayout_info_about_novel(website)
            })
    }
}