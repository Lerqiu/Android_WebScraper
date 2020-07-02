package com.example.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import com.example.webscraper.*
import newRelase


class NewRelasesCustomAdapter internal constructor(
    private val context: Context,
    private val newRelases: List<newRelase>,
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
            convertView = layoutInflater.inflate(R.layout.new_relases_item_view_child, null)
            val item = New_relases_item_view_child(
                convertView,
                DataManagement.copyWebsite(DataManagement.getWebsite(this.newRelases[listPosition].web)),
                this.newRelases[listPosition].chap,
                context,
                mainActivity
            )
        }

        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }

    override fun getGroup(listPosition: Int): Any {
        return this.newRelases[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.newRelases.size
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
        //if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.new_relases_item_view, null)
            New_relases_item_view(
                convertView,
                DataManagement.copyWebsite(DataManagement.getWebsite(this.newRelases[listPosition].web)),
                this.newRelases[listPosition].chap,
                context,
                mainActivity
            )
       // }

        return convertView!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}