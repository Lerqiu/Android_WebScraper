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
import com.example.helpers.OftenUsedMethods
import com.example.helpers.OnBookMarkClick
import com.example.webscraper.*


class InfoAboutNovelCustomAdapter internal constructor(
    private val context: Context,
    website: WebSiteData,
    private val mainActivity: MainActivity,
    private val mainFragment: OnBookMarkClick
) : BaseExpandableListAdapter() {
    private val website: WebSiteData =DataManagement.copyWebsite(website)


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
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.info_about_novel_list_child, null)
            val item = Info_about_novel_child(
                convertView,
                DataManagement.copyWebsite(DataManagement.getWebsite(website)),
                context,
                mainActivity,
                getGroup(listPosition) as Chapter,
                mainFragment
            )

        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }

    override fun getGroup(listPosition: Int): Any {
        return this.website.chapters[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.website.chapters.size
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
        val chapter = (getGroup(listPosition) as Chapter)
        var TitleText = OftenUsedMethods.getChapterDescription(chapter)
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