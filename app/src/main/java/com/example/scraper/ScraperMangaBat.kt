import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDateTime
import java.util.*

class MangaBat(link: String) : WebScrapers() {
    override var link = link
    override val linkFormat: String = """https://read.mangabat.com/"""
    val secondLinkFormat="https://manganelo.com/manga/"

    override fun checkLinkFormat(): Boolean {
        return (link.commonPrefixWith(linkFormat).length == linkFormat.length) ||
                (link.commonPrefixWith(secondLinkFormat).length == secondLinkFormat.length)
    }

    override fun getData(): WebSiteData {
        val doc = Jsoup.connect(link).get()

        var data =
            WebSiteData(
                link,
                doc.select("div.story-info-right h1").text(),
                doc.select("div.story-info-right tbody tr").get(1).child(1).select("a").text(),
                Date()
            )

        for(chapter in doc.select("div.panel-story-chapter-list ul.row-content-chapter li")){
            data.addChapterAtEnd(Chapter("",chapter.select("a.chapter-name").text(),"",chapter.select("a.chapter-name").attr("href").toString(),
                chapter.select("span.chapter-time").attr("title").toString()))
        }

        for(genre in doc.select("div.story-info-right tbody tr").get(3).child(1).select("a")){
            data.addGenres(genre.text())
        }
        return data
    }

    override fun checkDataUpdate(data: WebSiteData): Boolean {
        try {
            val doc = Jsoup.connect(link).get()
            val chapter = doc.select("div.panel-story-chapter-list ul.row-content-chapter li").get(0)
            val ChapterName = chapter.select("a.chapter-name").text()
            val ChapterLink = chapter.select("a.chapter-name").attr("href").toString()
            val ChapterTimeOfAdd = chapter.select("span.chapter-time").attr("title").toString()

            return !((ChapterLink == data.lastNewChapter.link) && (ChapterName == data.lastNewChapter.Name) &&
                    (ChapterTimeOfAdd == data.lastNewChapter.timeOfAdd))
        }catch (e:Exception){
            println(e)
            return true
        }
    }
}
