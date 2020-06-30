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

class List_item_child(convertView: View, website: WebSiteData, context : Context) {

    init {

        convertView.findViewById<TextView>(R.id.list_item_child_layout_text).text = website.Author

        var chapter_text = website.lastNewChapter.Name
        if (chapter_text.length != 0 && website.lastNewChapter.Number.length != 0)
            chapter_text += " <=> " + website.lastNewChapter.Number
        else
            chapter_text += website.lastNewChapter.Number

        convertView.findViewById<TextView>(R.id.list_item_child_layout_chapter).text =
            chapter_text

        if (website.sendEmail == false || DataManagement.isEmailAccepted() == false)
            convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                .setColorFilter(Color.LTGRAY)

        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
            .setOnClickListener(View.OnClickListener { view ->
                if(DataManagement.isEmailAccepted()){
                    if(website.sendEmail == true){
                        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                            .setColorFilter(Color.LTGRAY)
                        website.sendEmail =false
                    }
                    else
                    {
                        convertView.findViewById<ImageButton>(R.id.ImageButton_message)
                            .setColorFilter(convertView.findViewById<ImageButton>(R.id.ImageButton_web).colorFilter)
                        website.sendEmail =true
                    }
                }
            })
        convertView.findViewById<ImageButton>(R.id.ImageButton_web)
            .setOnClickListener(View.OnClickListener { view ->
                println("Ustaw web")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(website.link)
                context.startActivity(intent)
            })
        convertView.findViewById<ImageButton>(R.id.ImageButton_tags)
            .setOnClickListener(View.OnClickListener { view ->
                println("Ustaw tags")
            })
    }
}