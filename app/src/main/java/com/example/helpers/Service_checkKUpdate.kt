package com.example.helpers

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger
import java.util.*

class Service_checkKUpdate : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        UpdateData.addToLog("Service_checkKUpdate: onCreate"+Date())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        UpdateData.addToLog("Service_checkKUpdate: onStartCommand"+Date())

        if(DataManagement.getCheckPeriodic()){
            UpdateData.runOneTime()
        }

        stopSelf()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        UpdateData.addToLog("Service_checkKUpdate: onDestroy"+Date())
    }
}
