package com.sginnovations.asked.data.network


import android.util.Log
import com.sginnovations.asked.data.rss_dataclass.Article
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

private const val TAG = "RssFeedCloudService"
class CloudService(private val httpClient: HttpClient) {

    suspend fun getArticles(urlString: String): List<Article> {
        val httpClient = HttpClient()
        Log.d(TAG, "Fetching RSS feed from $urlString")

        try {
            val response = httpClient.get(urlString).bodyAsText()
            Log.d(TAG, "RSS feed fetched successfully. response -> $response")
            return parseRssFeed(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching RSS feed: ${e.localizedMessage}")
            return emptyList()
        }
    }

    private fun parseRssFeed(response: String): List<Article> {
        try {
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(response.byteInputStream())
            doc.documentElement.normalize()

            Log.d(TAG, "Parsing RSS feed")

            val list = mutableListOf<Article>()

            val items = doc.getElementsByTagName("item")
            val maxItems = 10

            for (i in 0 until items.length) {
                if (list.size >= maxItems) {
                    break
                }
                val item = items.item(i) as Element
                val title = item.getElementsByTagName("title").item(0).textContent.trim()
                val link = item.getElementsByTagName("link").item(0).textContent
                val description = item.getElementsByTagName("description").item(0).textContent
                val pubDate = item.getElementsByTagName("pubDate").item(0).textContent
                val category = item.getElementsByTagName("category").item(0).textContent
                val enclosure = item.getElementsByTagName("enclosure").item(0) as Element
                val imageUrl = enclosure.getAttribute("url") // Extrae la URL de la imagen

                list.add(Article(title, link, description, pubDate, category, imageUrl))
                Log.d(TAG, "Added article: $title")
            }

            Log.d(TAG, "RSS feed parsed successfully. Total articles found: ${list.size}")
            return list
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing RSS feed: ${e.localizedMessage}")
            return emptyList()
        }
    }

}
