package com.example.webscraper

import Chapter
import DataWasUpdatedSignal
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.helpers.OnBookMarkClick
import com.example.listAdapters.CustomExpandableListAdapter
import java.lang.Exception

class AllNovels_layout(private val mainActivity: MainActivity) : Fragment(), DataWasUpdatedSignal,
    OnBookMarkClick {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_novels_layout, container, false)

        expandableListView = view.findViewById(R.id.all_novels_layout_list_view)
        try {
            if (expandableListView != null) {
                adapter = CustomExpandableListAdapter(
                    requireContext(),
                    DataManagement.getWebsites(),
                    mainActivity,
                    this
                )
                expandableListView!!.setAdapter(adapter)

            }
        } catch (e: Exception) {
            Log.e("Error",e.toString())
        }

        UpdateData.addToLog("Stworzenie fragmentu: AllNovels_layout")

        return view
    }

    fun reloadLayout(){
        mainActivity.setMainLayout_view_all()
    }

    override fun signalRecived() {

    }

    override fun handleOnBookMarkClick(chapter: Chapter?) {

    }
}