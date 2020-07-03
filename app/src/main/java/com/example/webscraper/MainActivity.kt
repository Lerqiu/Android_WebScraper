package com.example.webscraper

import WebSiteData
import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var STATICMainActivity: MainActivity? = null
    }

    private var drawer: DrawerLayout? = null
    private var toolBar: Toolbar? = null
    private var drawerLayoutView: View? = null
    private var updateUIHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        STATICMainActivity = this

        setToolBar()
        setDrawer()//Left menu

        val extras = intent.extras

        if (extras != null && extras.containsKey("OpenFragment")) {
            setMainLayout_info_about_novel(
                WebSiteData(
                    extras["OpenFragment"] as String,
                    "",
                    "",
                    Date()
                )
            )
        } else
            if (savedInstanceState == null)
                setMainLayout_new_relases()

        setUpdateUIHandlerListener()

        loadDataBase()
    }

    private fun setToolBar() {
        toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)
        toolBar?.findViewById<ImageView>(R.id.toolbar_logo)?.setOnClickListener { v ->
            UpdateData.runOneTime()
        }
        toolBar?.findViewById<ImageButton>(R.id.toolbar_logo_dark)?.setOnClickListener { v ->
            DataManagement.removeLastChapter()
        }

    }

    private fun setDrawer() {
        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        drawerLayoutView = navigationView
        navigationView.setNavigationItemSelectedListener(this)

        val actionBarDT = ActionBarDrawerToggle(
            this,
            drawer,
            toolBar,
            R.string.nevigation_drawer_open,
            R.string.nevigation_drawer_close
        )
        drawer?.addDrawerListener(actionBarDT)
        actionBarDT.syncState()
    }

    fun loadDataBase() {
        Thread(Runnable {
            DataManagement.loadDataFromDisk(this.applicationContext)
            SystemClock.sleep(1000)
            updateUIHandler?.sendEmptyMessage(0)
        }).start()
    }

    override fun onPause() {
        Thread(Runnable {
            DataManagement.saveDataToDisk(this.applicationContext)
        }).start()
        super.onPause()
    }


    override fun onBackPressed() {
        if (drawer != null && drawer!!.isDrawerOpen(GravityCompat.START))
            drawer?.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    fun changeToolBarText(newTitle: String) {
        supportActionBar?.title = newTitle
    }

    fun changeDrawerEmail() {
        drawerLayoutView?.findViewById<TextView>(R.id.drawer_mail)?.text = DataManagement.getEmail()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {
            R.id.nev_new -> setMainLayout_new_relases()
            R.id.nev_add -> setMainLayout_add_new()
            R.id.nev_unread -> setMainLayout_unread()
            R.id.nev_all -> setMainLayout_view_all()
            R.id.nev_settings -> setMainLayout_settings()
        }
        drawer?.closeDrawer(GravityCompat.START)
        return true
    }

    //<====================================================>
    fun setMainLayout_new_relases() {
        val layout = New_relases_layout(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(getString(R.string.New_releases))
    }

    fun setMainLayout_add_new() {
        val layout = Add_new_layout(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(getString(R.string.Add_new))
    }

    fun setMainLayout_unread() {
        val layout = Unread_layout(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(getString(R.string.Unread))
    }

    fun setMainLayout_view_all() {
        val layout = AllNovels_layout(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(getString(R.string.View_all))
    }

    fun setMainLayout_settings() {
        val layout = Settings_layout(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(getString(R.string.Settings))
    }

    fun setMainLayout_info_about_novel(novel: WebSiteData) {
        val layout =
            Info_of_novel(this, DataManagement.copyWebsite(DataManagement.getWebsite(novel)))
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            layout
        ).commit()
        DataManagement.setUpdateSignalReciver(layout)
        changeToolBarText(novel.Title)
    }

    private fun setUpdateUIHandlerListener() {
        if (updateUIHandler != null) return

        updateUIHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(inputMessage: Message) {
                when (inputMessage.what) {
                    0 -> changeDrawerEmail()
                }
            }
        }
    }
}


fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}