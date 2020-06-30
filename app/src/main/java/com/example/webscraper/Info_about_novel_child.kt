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

class Info_about_novel_child(
    convertView: View,
    website: WebSiteData,
    context: Context,
    mainActivity: MainActivity,
    chapter: Chapter
) {

    init {


        if (chapter.Number.length == 0){
            convertView.findViewById<LinearLayout>(R.id.numberLL).visibility=View.GONE
        }else{
            convertView.findViewById<TextView>(R.id.number).text = chapter.Number
        }

        if (chapter.Name.length == 0){
            convertView.findViewById<LinearLayout>(R.id.nameLL).visibility=View.GONE
        }else{
            convertView.findViewById<TextView>(R.id.name).text = chapter.Name
        }

        if (chapter.PublishedBy.length == 0){
            convertView.findViewById<LinearLayout>(R.id.publishedLL).visibility=View.GONE
        }else{
            convertView.findViewById<TextView>(R.id.publishedLL).text = chapter.PublishedBy
        }

        if (chapter.timeOfAdd.length == 0){
            convertView.findViewById<LinearLayout>(R.id.timeAddLL).visibility=View.GONE
        }else{
            convertView.findViewById<TextView>(R.id.timeAdd).text = chapter.timeOfAdd
        }

        convertView.findViewById<ImageButton>(R.id.ImageButton_web)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(chapter.link)
                context.startActivity(intent)
            })

        convertView.findViewById<ImageButton>(R.id.ImageButton_bookMark)
            .setOnClickListener(View.OnClickListener { view ->
               website.lastReadedChapter = chapter
                DataManagement.markAsReadNovel(website,chapter)
                println(DataManagement.listNotReadedNovel())
            })

    }
}