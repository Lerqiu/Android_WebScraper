package com.example.webscraper

import DataWasUpdatedSignal
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.listAdapters.CustomExpandableListAdapter
import java.lang.Exception

class Unread_layout(private val mainActivity: MainActivity) : Fragment(), DataWasUpdatedSignal, OnBookMarkClick {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManagement.setUpdateSignalReciver(this)
        val view = inflater.inflate(R.layout.unread_novels_layout, container, false)
        expandableListView = view.findViewById(R.id.unread_novels_layout_list_view)
        try {
            if (expandableListView != null) {
                adapter = CustomExpandableListAdapter(
                    requireContext(),
                    DataManagement.listNotReadedNovel(),
                    mainActivity,
                    this
                )
                expandableListView!!.setAdapter(adapter)
            }
        } catch (e: Exception) {
            println(e)
        }
        return view
    }

    fun reloadLayout(){
        mainActivity.setMainLayout_unread()
    }

    override fun signalRecived() {
        println("""<===========================================================>""")
    }

    override fun handleOnBookMarkClick() {
        reloadLayout()
    }
}