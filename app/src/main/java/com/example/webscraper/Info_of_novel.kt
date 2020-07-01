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
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.example.listAdapters.CustomExpandableListAdapter
import com.example.listAdapters.InfoAboutNovelCustomAdapter
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.info_about_novels_layout.*
import java.lang.Exception


class Info_of_novel(private val mainActivity: MainActivity, private val webSiteData: WebSiteData) :
    Fragment(), OnBookMarkClick {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var thisView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_about_novels_layout, container, false)
        this.thisView = view

        setShowHideArror(view)
        setTitle(view)
        setAuthor(view)
        setTags(view)
        setLinkText(view)
        setCopyButton(view)
        setBrowserButton(view)
        setLastReaded(view)
        setExpedabeleList(view)

        return view
    }

    private fun setShowHideArror(view: View) {
        val arrow = view.findViewById<ImageView>(R.id.showHide)
        val viewPanel = view.findViewById<ScrollView>(R.id.scrollView)

        arrow.setOnClickListener { V ->
            when (viewPanel.visibility) {
                View.GONE -> {
                    arrow.setImageResource(R.drawable.ic_arrow_up)
                    viewPanel.visibility = View.VISIBLE
                }
                View.VISIBLE -> {
                    arrow.setImageResource(R.drawable.ic_arrow_down)
                    viewPanel.visibility = View.GONE
                }
            }
        }
    }

    private fun setTitle(view: View) {
        view.findViewById<TextView>(R.id.title).text = webSiteData.Title
    }

    private fun setAuthor(view: View) {
        view.findViewById<TextView>(R.id.author).text = webSiteData.Author
    }

    private fun setTags(view: View) {
        val tags = view.findViewById<FlexboxLayout>(R.id.tags)

        if (webSiteData.genres.size == 0) {
            val textV = TextView(mainActivity)
            textV.setText("N/A")
            tags.addView(textV)
        } else {
            for (i in webSiteData.genres) {
                val textV = TextView(mainActivity)
                textV.setText(i)
                textV.setPadding(10, 5, 10, 5)
                tags.addView(textV)
            }
        }

    }

    private fun setLinkText(view: View) {
        view.findViewById<TextView>(R.id.link).text = webSiteData.link
    }

    private fun setCopyButton(view: View) {
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
    }

    private fun setBrowserButton(view: View) {
        view.findViewById<ImageButton>(R.id.open_browser)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(webSiteData.link)
                mainActivity.startActivity(intent)
            })
    }

    private fun setLastReaded(view: View) {
        var chapter_text = webSiteData.lastReadedChapter.Name
        if (chapter_text.length == 0)
            chapter_text = webSiteData.lastReadedChapter.Number

        view.findViewById<TextView>(R.id.lastReaded).text = chapter_text

        view.findViewById<ImageButton>(R.id.lastReaded_browserButton)
            .setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(webSiteData.lastReadedChapter.link)
                mainActivity.startActivity(intent)
            })
    }


    private fun setExpedabeleList(view: View) {
        expandableListView = view.findViewById(R.id.list_view)
        try {
            if (expandableListView != null) {
                adapter = InfoAboutNovelCustomAdapter(
                    requireContext(),
                    webSiteData,
                    mainActivity,
                    this
                )
                expandableListView!!.setAdapter(adapter)
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun handleOnBookMarkClick() {
        if (thisView != null) {
            setLastReaded(thisView!!)
        }
    }
}