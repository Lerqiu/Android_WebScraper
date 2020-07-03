package com.example.webscraper

import DataWasUpdatedSignal
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.listAdapters.NewRelasesCustomAdapter
import java.lang.Exception

class New_relases_layout(private val mainActivity: MainActivity) : Fragment(),DataWasUpdatedSignal {
    private var fragmentView: View? = null
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var updateUIHandler: Handler? = null

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
                Log.e("Error",e.toString())
            }
        }
        setUpdateUIHandlerListener()
        UpdateData.addToLog("Stworzenie fragmentu: New_relases_layout")

        return fragmentView!!
    }

    private fun setUpdateUIHandlerListener() {
        if (updateUIHandler != null) return

        updateUIHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(inputMessage: Message) {
                reloadLayout()
            }
        }
    }

    private fun reloadLayout(){
        mainActivity.setMainLayout_new_relases()
    }

    override fun signalRecived() {
        updateUIHandler?.sendEmptyMessage(0)
    }
}