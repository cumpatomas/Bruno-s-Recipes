package com.cumpatomas.brunosrecipes.domain

import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup

class GetNews {

    suspend operator fun invoke(): List<NewsModel>{
        val newsList = mutableListOf<NewsModel>()

  val url = Jsoup.connect("https://www.clara.es/blogs/carlos-rios").get()

        coroutineScope {

            val newsTitle = url.getElementsByTag("h4")
            val newsPhotos = url.getElementsByClass("col-xs-12.col-sm-4")

            for (title in newsTitle) {
                val link = title.select("a[href]").attr("href")
                newsList.add(NewsModel(title = title.text(), link = "https://www.clara.es/$link"))
            }
        }
        return newsList
    }
}