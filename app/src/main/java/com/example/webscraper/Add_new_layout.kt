package com.example.webscraper

import DataWasUpdatedSignal
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.*
import androidx.fragment.app.Fragment
import java.lang.Exception

class Add_new_layout(private val mainActivity: MainActivity) : Fragment(),DataWasUpdatedSignal {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_new_novels_layout, container, false)

        setAddButton(view)
        setSupportedList(view)

        return view
    }

    fun setAddButton(view:View){
        val button = view.findViewById<ImageButton>(R.id.add_button)
        button.setOnClickListener { v ->
            val link = view.findViewById<EditText>(R.id.link).text.toString()
            if(URLUtil.isValidUrl(link)) {
                try{
                    UpdateData.addNewNovel(link)
                }catch (e:Exception){
                    println(e)
                    Toast.makeText(
                        requireContext(),
                        mainActivity.getString(R.string.Invalid_link),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mainActivity.hideKeyboard()
                view.findViewById<EditText>(R.id.link).setText("")
                Toast.makeText(
                    requireContext(),
                    mainActivity.getString(R.string.Adding_new_novel_was_started),
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    requireContext(),
                    mainActivity.getString(R.string.Invalid_link),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setSupportedList(view: View) {
        val arrayAdapter: ArrayAdapter<*>
        val mListView = view.findViewById<ListView>(R.id.list_of_supported)
        arrayAdapter = ArrayAdapter(
            mainActivity,
            android.R.layout.simple_list_item_1, WebSiteScraperManagement.getSupportedWebsites()
        )
        mListView.adapter = arrayAdapter
    }

    override fun signalRecived() {

    }
}