package com.example.webscraper

import WebSiteData
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.listAdapters.CustomExpandableListAdapter
import com.example.listAdapters.InfoAboutNovelCustomAdapter
import com.google.android.flexbox.FlexboxLayout
import java.lang.Exception


class Info_of_novel(mainActivity: MainActivity, webSiteData: WebSiteData) : Fragment() {
    private val mainActivity = mainActivity
    private val webSiteData = webSiteData
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_about_novels_layout, container, false)

        view.findViewById<TextView>(R.id.title).text = webSiteData.Title
        view.findViewById<TextView>(R.id.author).text = webSiteData.Author
        val tags = view.findViewById<FlexboxLayout>(R.id.tags)
        for (i in webSiteData.genres) {
            val textV = TextView(mainActivity)
            textV.setText(i)
            tags.addView(textV)
        }

        view.findViewById<TextView>(R.id.link).text = webSiteData.link

        val clipboard = mainActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        view.findViewById<ImageButton>(R.id.copy)
            .setOnClickListener(View.OnClickListener { view ->
                val clip: ClipData = ClipData.newPlainText(webSiteData.Title, webSiteData.link)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.Copied),
                    Toast.LENGTH_SHORT
                ).show()
            })


        view.findViewById<ImageButton>(R.id.open_browser)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(webSiteData.link)
                mainActivity.startActivity(intent)
            })

        expandableListView = view.findViewById(R.id.list_view)
        try {
            if (expandableListView != null) {
                adapter = InfoAboutNovelCustomAdapter(
                    requireContext(),
                    webSiteData,
                    mainActivity
                )
                expandableListView!!.setAdapter(adapter)
            }
        } catch (e: Exception) {
            println(e)
        }

        return view
    }
}