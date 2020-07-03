package com.example.webscraper

import Chapter
import DataWasUpdatedSignal
import WebSiteData
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.helpers.OftenUsedMethods
import com.example.helpers.OnBookMarkClick
import com.example.listAdapters.InfoAboutNovelCustomAdapter
import com.google.android.flexbox.FlexboxLayout
import java.lang.Exception


class Info_of_novel(private val mainActivity: MainActivity, private val webSiteData: WebSiteData) :
    Fragment(), OnBookMarkClick, DataWasUpdatedSignal {
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
        setNotificationButton(view)
        setDeleteWebsite(view)
        setTags(view)
        setLinkText(view)
        setCopyButton(view)
        setBrowserButton(view)
        setLastReaded(view, webSiteData.lastReadedChapter)
        setExpedabeleList(view)

        UpdateData.addToLog("Stworzenie fragmentu: Info_of_novel")

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

    fun setNotificationButton(convertView: View) {
        val website=webSiteData
        val button = convertView.findViewById<ImageButton>(R.id.notifications)
        if (DataManagement.getWebsiteNotifications(website) == false)
            button.setColorFilter(Color.LTGRAY)

        button.setOnClickListener(View.OnClickListener { view ->
            if (DataManagement.getWebsiteNotifications(website)) {
                button.setColorFilter(Color.LTGRAY)
                DataManagement.setWebsiteNotifications(website, false)
            } else {
                button.setColorFilter(convertView.findViewById<ImageButton>(R.id.delete).colorFilter)
                DataManagement.setWebsiteNotifications(website, true)
            }
        })
    }

    private fun setDeleteWebsite(view: View) {
        view.findViewById<ImageButton>(R.id.delete)
            .setOnClickListener(View.OnClickListener { it ->
                DataManagement.removeNovel(webSiteData.link)
                mainActivity.setMainLayout_new_relases()
            })
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
                OftenUsedMethods.openWebsite(mainActivity,webSiteData.link)
            })
    }

    private fun setLastReaded(view: View, chapter: Chapter) {

        view.findViewById<TextView>(R.id.lastReaded).text = OftenUsedMethods.getChapterDescription(chapter)

        view.findViewById<ImageButton>(R.id.lastReaded_browserButton)
            .setOnClickListener(View.OnClickListener { view ->
                OftenUsedMethods.openWebsite(mainActivity, chapter.link)
            })
    }


    private fun setExpedabeleList(view: View) {
        expandableListView = view.findViewById(R.id.list_view)
        try {
            if (expandableListView != null) {
                adapter = InfoAboutNovelCustomAdapter(
                    requireContext(),
                    DataManagement.copyWebsite(webSiteData),
                    mainActivity,
                    this
                )
                expandableListView!!.setAdapter(adapter)
            }
        } catch (e: Exception) {
            Log.e("Error",e.toString())
        }
    }

    override fun handleOnBookMarkClick(chapter: Chapter?) {
        if (thisView != null && chapter != null)
            setLastReaded(thisView!!, chapter)
    }

    override fun signalRecived() {

    }
}