import android.content.Context
import com.example.helpers.OftenUsedMethods
import java.io.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.collections.ArrayList

interface DataWasUpdatedSignal {
    fun signalRecived()
}

object DataManagement {
    val dataFileName = "data.bin"
    var data: Data = Data()

    private val sharedLock = Semaphore(1, true)
    private var sendUpdateSignalTo: DataWasUpdatedSignal? = null

    fun setUpdateSignalReciver(obj: DataWasUpdatedSignal) {
        sendUpdateSignalTo = obj
    }

    private fun sendUpdateSignal() {
        sendUpdateSignalTo?.signalRecived()
    }

    fun saveDataToDisk(applicationContext: Context) {
        try {
            sharedLock.acquire()
            ObjectOutputStream(
                File(
                    applicationContext.filesDir,
                    this.dataFileName
                ).outputStream()
            ).use { it -> it.writeObject(data) }
            //UpdateData.addToLog("Zapisano dane.")
        } finally {
            sharedLock.release()
        }
    }


    fun loadDataFromDisk(applicationContext: Context) {
        try {
            sharedLock.acquire()
            try {
                ObjectInputStream(
                    File(
                        applicationContext.filesDir,
                        this.dataFileName
                    ).inputStream()
                ).use {
                    val d = it.readObject()
                    if (d is Data)
                        this.data = d
                }
                UpdateData.addToLog("Wczytano dane.")
                sendUpdateSignal()
            } catch (e: Exception) {
                UpdateData.addToLog("Próba wczytania danych nieudana. Brak pliku.")
            }
        } finally {
            sharedLock.release()
        }
    }

    fun getPeriodOfChecking(): Int {
        try {
            sharedLock.acquire()
            return data.time
        } finally {
            sharedLock.release()
        }
    }

    fun setPeriodOfChecking(e: Int) {
        try {
            sharedLock.acquire()
            if (e >= 0) {
                data.time = e
                sendUpdateSignal()
            }
        } finally {
            sharedLock.release()
        }
    }

    fun getEmail(): String {
        try {
            sharedLock.acquire()
            return data.email
        } finally {
            sharedLock.release()
        }
    }

    fun getNotificationStatus_P(): Boolean {

            return data.showNotification

    }

    fun getNotificationStatus(): Boolean {
        try {
            sharedLock.acquire()
            return getNotificationStatus_P()
        } finally {
            sharedLock.release()
        }
    }

    fun setNotificationStatus(newSetting: Boolean) {
        try {
            sharedLock.acquire()
            data.showNotification = newSetting
        } finally {
            sharedLock.release()
        }
    }

    fun getCheckPeriodic(): Boolean {
        try {
            sharedLock.acquire()
            return data.checkPeriodic
        } finally {
            sharedLock.release()
        }
    }

    fun setCheckPeriodic(newSetting: Boolean) {
        try {
            sharedLock.acquire()
            data.checkPeriodic = newSetting
        } finally {
            sharedLock.release()
        }
    }

    fun setEmail(e: String) {
        try {
            sharedLock.acquire()
            data.email = e
        } finally {
            sharedLock.release()
        }
    }

    fun isEmailAccepted(): Boolean {
        try {
            sharedLock.acquire()
            return this.data.sendEmail
        } finally {
            sharedLock.release()
        }
    }

    fun setEmailAcceptedTo(e: Boolean) {
        try {
            sharedLock.acquire()
            if (this.data.sendEmail != e) {
                this.data.sendEmail = e
                sendUpdateSignal()
            }

        } finally {
            sharedLock.release()
        }
    }

    fun getWebsites(): MutableList<WebSiteData> {
        try {
            sharedLock.acquire()
            return this.data.WebSites.toMutableList()
        } finally {
            sharedLock.release()
        }
    }

    fun addNewNovel(novel: WebSiteData) {
        try {
            sharedLock.acquire()

            novel.lastReadedChapter = novel.chapters.last()
            novel.lastNewChapter = novel.chapters[0]
            this.data.WebSites.add(novel)

            UpdateData.addToLog("Dodano novelkę.Link: " + novel.link + " Tytuł: " + novel.Title)
            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun updateNovel(new: WebSiteData) {
        try {
            sharedLock.acquire()

            if (new.chapters.size == 0) return

            for (i in this.data.WebSites)
                if (i.link == new.link) {

                    if (i.chapters.size == 0) {
                        for (chapters in new.chapters) {
                            val newRelase = newRelase(
                                i,
                                chapters
                            )
                            this.data.newChaptersReleses.add(0, newRelase)
                            OftenUsedMethods.setNotification(newRelase)
                        }
                        return
                    }

                    for (index in 0..(new.chapters.size - 1)) {
                        val newCh = new.chapters[index]
                        val oldCh =  i.lastNewChapter
                        if (newCh.Number == oldCh.Number && newCh.Name == oldCh.Name && newCh.timeOfAdd == oldCh.timeOfAdd && newCh.PublishedBy == oldCh.PublishedBy) {
                            for (updatedChapterIndex in (index - 1) downTo 0) {
                                val newRelase = newRelase(
                                    i,
                                    new.chapters[updatedChapterIndex]
                                )
                                this.data.newChaptersReleses.add(0, newRelase)
                                OftenUsedMethods.setNotification(newRelase)
                            }
                            i.chapters = new.chapters
                            if (i.chapters.size > 0)
                                i.lastNewChapter = i.chapters[0]

                            break
                        }
                    }
                }
        } finally {
            sharedLock.release()
            sendUpdateSignal()
        }
    }

    private fun removeFromNewRelases(link: String) {
        this.data.newChaptersReleses = this.data.newChaptersReleses.filter { it ->
            (it.web.link != link)
        }.toMutableList()
    }

    fun removeNovel(link: String): Boolean {
        try {
            sharedLock.acquire()
            if (isNovelAdded_P(link)) {
                this.data.WebSites =
                    this.data.WebSites.filter { it -> it.link != link } as MutableList<WebSiteData>
                removeFromNewRelases(link)
                UpdateData.addToLog("Usunięto: " + link)
                sendUpdateSignal()
                return true
            }
            return false
        } finally {
            sharedLock.release()
        }
    }

    fun isNovelAdded(link: String): Boolean {
        try {
            sharedLock.acquire()
            return isNovelAdded_P(link)
        } finally {
            sharedLock.release()
        }
    }

    private fun isNovelAdded_P(link: String): Boolean {
        for (i in this.data.WebSites)
            if (i.link == link)
                return true
        return false
    }

    fun listNotReadedNovel(): List<WebSiteData> {
        try {
            sharedLock.acquire()
            val li: MutableList<WebSiteData> = ArrayList()
            for (i in this.data.WebSites)
                if (i.lastReadedChapter.link != i.lastNewChapter.link)
                    li.add(i)
            return li
        } finally {
            sharedLock.release()
        }
    }

    fun markChapterAsReaded(
        webSiteLink: String,
        chapter_inString: String
    ): Boolean {
        try {
            sharedLock.acquire()

            for (i in this.data.WebSites)
                if (i.link == webSiteLink) {

                    var ind = -1
                    for (index in 0..(i.chapters.size - 1)) {
                        if (i.chapters[index].toString() == chapter_inString) {
                            ind = index
                            i.lastReadedChapter = i.chapters[index]
                            break
                        }
                    }

                    if (ind >= 0)
                        this.data.newChaptersReleses = this.data.newChaptersReleses.filter { it ->
                            (it.web.link != webSiteLink) || (
                                    i.chapters.indexOf(it.chap) < ind)
                        }.toMutableList()
                    sendUpdateSignal()

                    return true
                }
            return false
        } finally {
            sharedLock.release()
        }
    }

    private fun getWebsite_P(webSiteData: WebSiteData): WebSiteData {
        for (i in this.data.WebSites) {
            if (i.link == webSiteData.link)
                return i
        }
        throw IndexOutOfBoundsException()
    }

    fun getWebsite(webSiteData: WebSiteData): WebSiteData {
        try {
            sharedLock.acquire()

            return getWebsite_P(webSiteData)

            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun setWebsiteNotifications(webSiteData: WebSiteData, newState: Boolean) {
        try {
            sharedLock.acquire()

            val i = getWebsite_P(webSiteData)
            i.notification = newState

            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun getWebsiteNotifications(webSiteData: WebSiteData): Boolean {
        try {
            sharedLock.acquire()

            val i = getWebsite_P(webSiteData)
            return i.notification

            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun removeLastChapter() {
        try {
            sharedLock.acquire()
            for (i in this.data.WebSites)
                if (i.chapters.size > 0) {
                    UpdateData.addToLog("Usuwanie ostatniego rozdziału:" + i.Title + " " + i.chapters[0].Number)
                    i.chapters.removeAt(0)
                    i.lastNewChapter = i.chapters[0]

                }
            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun newRelases(): List<newRelase> {
        try {
            sharedLock.acquire()
            return this.data.newChaptersReleses.toMutableList()
        } finally {
            sharedLock.release()
        }
    }

    fun isEmpty(): Boolean {
        try {
            sharedLock.acquire()
            return this.data.isEmpty()
        } finally {
            sharedLock.release()
        }
    }


    fun copyWebsite(web: WebSiteData): WebSiteData {
        val newW = WebSiteData(web.link, web.Title, web.Author, web.lastCheck)
        newW.chapters = web.chapters.toMutableList()
        newW.lastReadedChapter = web.lastReadedChapter
        newW.genres = web.genres.toMutableList()
        newW.notification = web.notification
        newW.lastNewChapter = web.lastNewChapter
        return newW
    }
}
