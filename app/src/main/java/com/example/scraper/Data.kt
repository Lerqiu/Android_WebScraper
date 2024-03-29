import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

data class Chapter(
    val Number: String,
    val Name: String,
    val PublishedBy: String,
    val link: String,
    val timeOfAdd: String
) : Serializable {}

class WebSiteData(link: String, Title: String, Author: String, time: Date) : Serializable {
    val link: String = link
    val Title: String = Title
    val Author: String = Author
    var lastCheck: Date = time
    var notification = true
    var genres: MutableList<String> = ArrayList()
    var chapters: MutableList<Chapter> = ArrayList()

    var lastReadedChapter = Chapter("", "", "", "", "")
    var lastNewChapter = Chapter("", "", "", "", "")

    fun addGenres(s: String) {
        genres.add(s)
    }

    //index 0 to najnowszy
    fun addChapterAtBeginning(cha: Chapter) {
        chapters.add(0, cha)
    }

    fun addChapterAtEnd(cha: Chapter) {
        chapters.add(cha)
    }
}

data class newRelase(val web: WebSiteData, val chap: Chapter) : Serializable

class Data : Serializable {

    var email = ""
    var sendEmail = true
    var showNotification = true
    var checkPeriodic = false
    var time: Int = 60 * 10
    var WebSites: MutableList<WebSiteData> = ArrayList()
    var newChaptersReleses: MutableList<newRelase> = ArrayList()

    fun isEmpty(): Boolean {
        return (WebSites.size == 0) && (newChaptersReleses.size == 0) && (email == "") &&
                (sendEmail == true) && (showNotification == true) && (checkPeriodic == false) && (time == 60 * 10)
    }
}