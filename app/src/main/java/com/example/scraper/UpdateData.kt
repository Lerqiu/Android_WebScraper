import android.util.Log
import android.widget.Toast
import com.example.helpers.OftenUsedMethods
import com.example.webscraper.MainActivity
import com.example.webscraper.R
import com.share.email.EmailNotification
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.util.*

object UpdateData {

    private fun runOneTime_P() {
        try {
            for (i in DataManagement.getWebsites()) {
                if (WebSiteScraperManagement.DoExistWebScraper(i.link)) {
                    try {

                        val scraper = WebSiteScraperManagement.FindWebScraper(i.link)
                        if (scraper.checkDataUpdate(i)) {
                            val newData= scraper.getData()
                            DataManagement.updateNovel(newData)
                            if (i.notification) {
                                EmailNotification.send(
                                    "Nastąpiła aktualizacja: " + i.Title + "<br>" +
                                            "Link do strony głównej" + i.link + "<br>" +
                                            " Najnowszy rozdział to :" + i.lastNewChapter.Number + i.lastNewChapter.Name + "<br>" +
                                            "Link: " + i.lastNewChapter.link,
                                    i.Title + " " + i.lastNewChapter.Number + "<br>" +
                                            "Opublikowany przez: " + i.lastNewChapter.PublishedBy
                                )
                            }
                            addToLog("Nastąpiła aktualizacja: " + i.Title + " Najnowszy rozdział to :" + i.lastNewChapter.Number)

                        }
                    }catch (e:Exception){
                        Log.e("Error",e.toString())
                    }
                }
            }
        } catch (e: Exception) {
            this.addToLog("Nastąpił error z aktualizacjią \n" + e)
        }
    }

    fun runOneTime() {

        if (OftenUsedMethods.isNetworkAvailable()) {
            Thread(Runnable {
                runOneTime_P()
            }).start()
        } else {
            try {
                Toast.makeText(
                    MainActivity.STATICMainActivity,
                    MainActivity.STATICMainActivity?.getString(R.string.Error_network_is_not_available),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
        }
    }

    fun addToLog(s: String) {
        Log.i("Informacje dodatkowe", s)
    }

    fun addNewNovel(link: String) {

        if (OftenUsedMethods.isNetworkAvailable()) {
            Thread(Runnable {
                if (!DataManagement.isNovelAdded(link)) {
                    try {
                        val scraper = WebSiteScraperManagement.FindWebScraper(link)
                        val novel = scraper.getData()
                        DataManagement.addNewNovel(novel)
                    } catch (e: Exception) {
                        OftenUsedMethods.showToast("Error")
                        Log.e("Error", e.toString())
                    }
                }
            }).start()
        } else {
            try {
                Toast.makeText(
                    MainActivity.STATICMainActivity,
                    MainActivity.STATICMainActivity?.getString(R.string.Error_network_is_not_available),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
        }

    }
}