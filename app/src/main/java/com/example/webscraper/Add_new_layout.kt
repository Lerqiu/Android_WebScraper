package com.example.webscraper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Add_new_layout(mainActivity: MainActivity): Fragment() {
    private val mainActivity = mainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_novels_layout,container,false)
    }
}