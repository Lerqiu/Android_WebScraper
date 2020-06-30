package com.example.webscraper

import WebSiteData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null
    private var toolBar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
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

        if (savedInstanceState == null)
            setMainLayout_new_relases()

        Thread(Runnable {
            DataManagement.loadDataFromDisk(this.applicationContext)
            UpdateData.addNewNovel("""https://www.readlightnovel.org/mutagen""")
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
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            NewNovels_layout(this)
        ).commit()
        changeToolBarText(getString(R.string.New_releases))
    }

    fun setMainLayout_add_new() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            Add_new_layout(this)
        ).commit()
        changeToolBarText(getString(R.string.Add_new))
    }

    fun setMainLayout_unread() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            Unread_layout(this)
        ).commit()
        changeToolBarText(getString(R.string.Unread))
    }

    fun setMainLayout_view_all() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            AllNovels_layout(this)
        ).commit()
        changeToolBarText(getString(R.string.View_all))
    }

    fun setMainLayout_settings() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            Settings_layout(this)
        ).commit()
        changeToolBarText(getString(R.string.Settings))
    }

    fun setMainLayout_info_about_novel(novel: WebSiteData) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            Info_of_novel(this,novel)
        ).commit()
        changeToolBarText(novel.Title)
    }

}