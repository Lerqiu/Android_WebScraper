package com.example.webscraper

import DataWasUpdatedSignal
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import java.lang.Exception

class Unread_layout : Fragment(), DataWasUpdatedSignal {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManagement.setUpdateSignalReciver(this)
        val view = inflater.inflate(R.layout.unread_novels_layout, container, false)
        expandableListView = view.findViewById(R.id.all_novels_layout_list_view)
        try {
            if (expandableListView != null) {
                adapter = CustomExpandableListAdapter(
                    requireContext(),
                    DataManagement.listNotReadedNovel()
                )
                expandableListView!!.setAdapter(adapter)
            }
        } catch (e: Exception) {
            println(e)
        }
        return view
    }

    override fun signalRecived() {
        println("""<===========================================================>""")
    }
}