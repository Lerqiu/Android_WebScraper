package com.example.webscraper

import WebSiteData
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageButton
import android.widget.TextView
import java.util.HashMap


import java.util.*


class CustomExpandableListAdapter internal constructor(
    private val context: Context,
    private val websites : List<WebSiteData>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        //return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
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
            convertView = layoutInflater.inflate(R.layout.list_item_child, null)
            val item = List_item_child(convertView,this.websites[listPosition],context)
        }

        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }

    override fun getGroup(listPosition: Int): Any {
        return this.websites[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.websites.size
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
        val listTitle = (getGroup(listPosition) as WebSiteData).Title
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.list_item_layout_text)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}