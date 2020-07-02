package com.share.email

import android.util.Log
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
/*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
*/

object EmailNotification {
    private var host: String = "smtp.gmail.com"
    private var port: Int = 465
    private var username: String = "lerqiua@gmail.com"
    private var password: String = "Altron0070"
    private var senderEmail: String = "WebScraper"

    fun sendMail(to: String, subject: String, content: String) {
        /*
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = true
        props["mail.smtp.socketFactory.port"] = port
        props["mail.smtp.socketFactory.fallback"] = "false"
        props["mail.smtp.ssl.trust"] = host

        val session = Session.getDefaultInstance(
            props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            }
        )
        try {
            val message = MimeMessage(session)

            message.setFrom(InternetAddress(senderEmail))

            message.replyTo = InternetAddress.parse(senderEmail)

            message.addRecipient(Message.RecipientType.TO, InternetAddress(to))

            message.subject = subject

            message.setContent(content, "text/html; charset=utf-8")
            Transport.send(message)
        } catch (exc: MessagingException) {
            throw RuntimeException(exc)
        }*/

        MaildroidX.Builder()
            .smtp(host)
            .smtpAuthentication(true)
            .smtpUsername(username)
            .smtpPassword(password)
            .port(port.toString())
            .type(MaildroidXType.HTML)
            .to(to)
            .from(senderEmail)
            .subject( subject)
            .body(content)
            .mail()
    }

    fun getReceiver(): String {
        return DataManagement.getEmail()
    }

    fun getReciverSeting(): Boolean {
        return DataManagement.isEmailAccepted()
    }


    fun send(content: String, title: String = "Novel update") {
        Runnable{
            val reciver = getReceiver()
            if (getReciverSeting() && reciver.length > 0) {
                try {
                    sendMail(getReceiver(), title, content)
                } catch (e: Exception) {
                    UpdateData.addToLog("Problem z wysłaniem wiadomości \n" + e)
                }
            }
        }.run()
    }
}