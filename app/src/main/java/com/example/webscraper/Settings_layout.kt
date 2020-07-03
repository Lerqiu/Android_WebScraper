package com.example.webscraper

import DataWasUpdatedSignal
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.helpers.OftenUsedMethods
import java.lang.NumberFormatException

class Settings_layout(mainActivity: MainActivity) : Fragment(), DataWasUpdatedSignal {
    private val mainActivity = mainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_layout, container, false)



        setTextEnterEmail(view)
        setSwitchNotificationEmail(view)
        setSwitchNotificationStandard(view)
        setTextEnterTimeForCheck(view)
        setSwitchNotificationCheckingUpdate(view)

        UpdateData.addToLog("Stworzenie fragmentu: Settings")

        return view
    }

    fun setTextEnterEmail(view: View) {
        val email_text = view.findViewById<EditText>(R.id.textView_email)
        email_text.setText(DataManagement.getEmail())
        email_text.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_text.text).matches()) {
                    DataManagement.setEmail(email_text.text.toString())
                    mainActivity.hideKeyboard()
                    mainActivity.changeDrawerEmail()
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.New_email_set),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.Error_wrong_email_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return@OnKeyListener true
            }
            false
        })
    }

    fun setSwitchNotificationEmail(view: View) {
        val switch_email = view.findViewById<Switch>(R.id.switch_email)
        switch_email.isChecked = DataManagement.isEmailAccepted()
        switch_email.setOnCheckedChangeListener { buttonView, isChecked ->
            DataManagement.setEmailAcceptedTo(isChecked)
        }
    }

    fun setSwitchNotificationStandard(view: View) {
        val switch_notification = view.findViewById<Switch>(R.id.switch_notification)
        switch_notification.isChecked = DataManagement.getNotificationStatus()
        switch_notification.setOnCheckedChangeListener { buttonView, isChecked ->
            DataManagement.setNotificationStatus(isChecked)
        }
    }

    fun setTextEnterTimeForCheck(view: View) {
        val time_text = view.findViewById<EditText>(R.id.text_enter_time)
        time_text.setText((DataManagement.getPeriodOfChecking() / 60).toString())
        time_text.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {

                var isNumeric = true
                var number = 600

                try {
                    val num = time_text.text.toString().toInt()
                    if(num >0){
                        number = num
                    }
                    else
                        number = DataManagement.getPeriodOfChecking()
                        Toast.makeText(
                            requireContext(),
                            requireContext().resources.getString(R.string.Error_incorrect_format),
                            Toast.LENGTH_SHORT
                        ).show()
                } catch (e: NumberFormatException) {
                    isNumeric = false
                }

                if (isNumeric) {
                    DataManagement.setPeriodOfChecking(number * 60)
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.New_value_set),
                        Toast.LENGTH_SHORT
                    ).show()
                    mainActivity.setService()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.Error_incorrect_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return@OnKeyListener true
            }
            false
        })
    }

    fun setSwitchNotificationCheckingUpdate(view: View) {
        val switch_checking = view.findViewById<Switch>(R.id.switch_checking)
        switch_checking.isChecked = DataManagement.getCheckPeriodic()
        switch_checking.setOnCheckedChangeListener { buttonView, isChecked ->
            DataManagement.setCheckPeriodic(isChecked)
        }
    }

    override fun signalRecived() {
    }
}