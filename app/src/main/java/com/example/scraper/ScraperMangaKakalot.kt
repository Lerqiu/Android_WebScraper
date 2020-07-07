import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDateTime
import java.util.*

class MangaKakalot(link: String) : WebScrapers() {
    override var link = link
    override val linkFormat: String = """https://mangakakalot.com/"""


    override fun getData(): WebSiteData {
        val doc = Jsoup.connect(link).get()

        var data =
            WebSiteData(
                link,
                doc.select("ul.manga-info-text li").get(0).select("h1").text(),
                doc.select("ul.manga-info-text li").get(1).select("a").text(),
                Date()
            )


        for(chapter in doc.select("div.chapter-list div")){
            data.addChapterAtEnd(Chapter("",chapter.child(0).child(0).text(),"",chapter.child(0).child(0).attr("href").toString(),
                ""))
        }

        for(genre in doc.select("ul.manga-info-text li").get(6).select("a")){
            data.addGenres(genre.text())
        }
        return data
    }

    override fun checkDataUpdate(data: WebSiteData): Boolean {
        try {
            val doc = Jsoup.connect(link).get()
            val chapter = doc.select("div.chapter-list div").get(0)
            val ChapterName = chapter.child(0).child(0).text()
            val ChapterLink = chapter.child(0).child(0).attr("href").toString()

            return !((ChapterLink == data.lastNewChapter.link) && (ChapterName == data.lastNewChapter.Name))
        }catch (e:Exception){
            println(e)
            return true
        }
    }
}