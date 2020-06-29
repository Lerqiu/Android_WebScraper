import android.content.Context
import java.io.*
import java.lang.Exception
import java.util.concurrent.Semaphore

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
            for (i in this.data.WebSites)
                if (i.link == new.link) {
                    i.chapters = new.chapters
                    if (i.chapters.size > 0)
                        i.lastNewChapter = i.chapters[0]
                }
            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }

    fun removeNovel(link: String): Boolean {
        try {
            sharedLock.acquire()
            if (isNovelAdded(link)) {
                this.data.WebSites =
                    this.data.WebSites.filter { it -> it.link != link } as MutableList<WebSiteData>
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

    fun listOfNovels(): List<String> {
        try {
            sharedLock.acquire()
            val li: MutableList<String> = ArrayList()
            for (i in this.data.WebSites)
                li.add(i.Title)
            return li
        } finally {
            sharedLock.release()
        }
    }

    fun listNotReadedNovel(): List<String> {
        try {
            sharedLock.acquire()
            val li: MutableList<String> = ArrayList()
            for (i in this.data.WebSites)
                if (i.lastReadedChapter.link != i.lastNewChapter.link)
                    li.add(i.Title)
            return li
        } finally {
            sharedLock.release()
        }
    }

    fun markAsReadNovel(link: String): Boolean {
        try {
            sharedLock.acquire()
            for (i in this.data.WebSites)
                if (i.link == link) {
                    UpdateData.addToLog("Zaktualizowano postęp w czytaniu." + i.lastReadedChapter.link + " -> " + i.lastNewChapter.link)
                    i.lastReadedChapter = i.lastNewChapter
                    sendUpdateSignal()
                    return true
                }
            return false
        } finally {
            sharedLock.release()
        }
    }

    fun removeLastChapter(link: String) {
        try {
            sharedLock.acquire()
            for (i in this.data.WebSites)
                if (i.link == link) {

                    if (i.chapters.size > 0) {
                        UpdateData.addToLog("Usuwanie ostatniego rozdziału:" + i.Title + " " + i.chapters[0].Number)
                        i.chapters.removeAt(0)
                        i.lastNewChapter = i.chapters[0]
                    }
                }
            sendUpdateSignal()
        } finally {
            sharedLock.release()
        }
    }
}
