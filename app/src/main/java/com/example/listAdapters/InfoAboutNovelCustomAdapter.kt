package com.example.listAdapters

import Chapter
import WebSiteData
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.webscraper.Info_about_novel_child
import com.example.webscraper.List_item_child
import com.example.webscraper.MainActivity
import com.example.webscraper.R


class InfoAboutNovelCustomAdapter internal constructor(
    private val context: Context,
    private val websites: WebSiteData,
    private val mainActivity: MainActivity
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return getGroup(listPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.info_about_novel_list_child, null)
            val item = Info_about_novel_child(
                convertView,
                websites,
                context,
                mainActivity,
                this.websites.chapters[listPosition]
            )
        }

        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }

    override fun getGroup(listPosition: Int): Any {
        return this.websites.chapters[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.websites.chapters.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = (getGroup(listPosition) as Chapter)
        var TitleText = if (listTitle.Name.length == 0) listTitle.Number else listTitle.Name
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.list_item_layout_text)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = TitleText
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}