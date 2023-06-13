package com.cumpatomas.brunosrecipes.domain

import android.annotation.SuppressLint
import com.cumpatomas.brunosrecipes.data.localdb.NewsDao
import com.cumpatomas.brunosrecipes.data.localdb.entities.NewsEntity
import com.cumpatomas.brunosrecipes.data.localdb.entities.toEntity
import com.cumpatomas.brunosrecipes.domain.model.NewsModel
import com.cumpatomas.brunosrecipes.domain.model.toDomain
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import javax.inject.Inject

class ScrapNews @Inject constructor(private val newsDao: NewsDao) {

    suspend operator fun invoke(): List<NewsModel> {
        val newsList = mutableListOf<NewsModel>()

        try {

            val url = Jsoup.connect("https://www.clara.es/blogs/carlos-rios").get()

            coroutineScope {

                val newsTitle = url.getElementsByTag("h4")

                for (title in newsTitle) {
                    val link = title.select("a[href]").attr("href")
                    newsList.add(
                        NewsModel(
                            title = title.text(),
                            link = "https://www.clara.es/$link"
                        )
                    )
                }
            }
            val newsListEntity: List<NewsEntity> = newsList.map { it.toEntity() }
            newsDao.insertNews(newsListEntity)
            val outputList = newsDao.getNewsList().map { it.toDomain() }

            return outputList.shuffled()

        } catch (e: Exception) {

            return emptyList()

        }
    }
}