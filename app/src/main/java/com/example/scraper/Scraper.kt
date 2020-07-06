import java.lang.NullPointerException


abstract class WebScrapers {
    abstract val linkFormat: String
    abstract var link:String
    abstract fun getData(): WebSiteData
    abstract fun checkDataUpdate(data: WebSiteData): Boolean
    open fun checkLinkFormat(): Boolean {
        return (link.commonPrefixWith(linkFormat).length == linkFormat.length)
    }
}

object WebSiteScraperManagement {
    fun DoExistWebScraper(s: String) :Boolean{
        try {
            this.FindWebScraper(s)
            return true
        }catch (e:Exception){
            return false
        }
    }

    fun FindWebScraper(s: String): WebScrapers {
        if (NovelUpdates(s).checkLinkFormat())
            return NovelUpdates(s)
        if(ReadLightNovel(s).checkLinkFormat())
            return ReadLightNovel(s)
        if(MangaKakalot(s).checkLinkFormat())
            return MangaKakalot(s)
        if(MangaBat(s).checkLinkFormat())
            return MangaBat(s)
        //Kolejne klasy sprawdzamy podobnie
        throw NullPointerException()
    }

    fun getSupportedWebsites():List<String>{
        return arrayListOf("NovelUpdates","ReadLightNovel","MangaBat","Manganelo","MangaKakalot")
    }
}