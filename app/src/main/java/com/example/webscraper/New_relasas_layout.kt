package com.example.webscraper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.listAdapters.NewRelasesCustomAdapter
import java.lang.Exception

class NewNovelsRelases_layout(private val mainActivity: MainActivity) : Fragment() {
    private var fragmentView: View? = null
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentView = inflater.inflate(R.layout.new_novels_layout, container, false)
        expandableListView = fragmentView?.findViewById(R.id.list_view)

        if (fragmentView != null && expandableListView != null) {
            try {
                val newRelase = DataManagement.newRelases()
                adapter = NewRelasesCustomAdapter(
                    requireContext(),
                    newRelase ,
                    mainActivity
                )
                expandableListView!!.setAdapter(adapter)


                } catch (e: Exception) {
                println(e)
            }
        }

        return fragmentView!!
    }
}