package com.example.webscraper

import DataWasUpdatedSignal
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.webscraper.ExpandableListData.data
import java.lang.Exception

class AllNovels_layout : Fragment(), DataWasUpdatedSignal {

    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManagement.setUpdateSignalReciver(this)
        val view = inflater.inflate(R.layout.all_novels_layout, container, false)

        /* for (i in DataManagement.getWebsites()) {
             for (g in i.chapters)
                 println(g)
         }*/
        expandableListView = view.findViewById(R.id.all_novels_layout_list_view)
        try {
            if (expandableListView != null) {
                val listData = data
                titleList = ArrayList(listData.keys)
                adapter = CustomExpandableListAdapter(
                    requireContext(),
                    titleList as ArrayList<String>,
                    listData
                )
                expandableListView!!.setAdapter(adapter)
                /*
                expandableListView!!.setOnGroupExpandListener { groupPosition ->
                    Toast.makeText(
                        requireContext(),
                        (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                expandableListView!!.setOnGroupCollapseListener { groupPosition ->
                    Toast.makeText(
                        requireContext(),
                        (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(
                                titleList as
                                        ArrayList<String>
                                )
                                [groupPosition]]!!.get(
                            childPosition
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }*/
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