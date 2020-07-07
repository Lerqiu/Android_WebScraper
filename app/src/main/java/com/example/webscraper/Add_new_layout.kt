package com.example.webscraper

import DataWasUpdatedSignal
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.helpers.OftenUsedMethods
import java.lang.Exception

class Add_new_layout(private val mainActivity: MainActivity, private val startString: String = "") :
    Fragment(), DataWasUpdatedSignal {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_new_novels_layout, container, false)

        setStartText(view)
        setAddButton(view)
        setSupportedList(view)

        UpdateData.addToLog("Stworzenie fragmentu: Add_new_layout")

        return view
    }

    fun setStartText(view: View){
        view.findViewById<EditText>(R.id.link).setText(startString)
    }

    fun setAddButton(view: View) {
        val button = view.findViewById<ImageButton>(R.id.add_button)
        button.setOnClickListener { v ->
            val link = view.findViewById<EditText>(R.id.link).text.toString()
            if (!OftenUsedMethods.isNetworkAvailable()) {
                UpdateData.addToLog("Próba dodania novelki " + link + " bez połączenia z internetem")
                Toast.makeText(
                    requireContext(),
                    mainActivity.getString(R.string.Error_network_is_not_available),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (URLUtil.isValidUrl(link)) {
                    try {
                        UpdateData.addNewNovel(link)
                    } catch (e: Exception) {
                        Log.e("Error", e.toString())
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
                } else {
                    Toast.makeText(
                        requireContext(),
                        mainActivity.getString(R.string.Invalid_link),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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