package com.cumpatomas.brunosrecipes.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cumpatomas.brunosrecipes.data.localdb.LocalDatabase
import com.cumpatomas.brunosrecipes.data.localdb.NewsDao
import com.cumpatomas.brunosrecipes.data.localdb.entities.NewsEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NewsDaoTest {
    private lateinit var database: LocalDatabase
    private lateinit var dao: NewsDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getNewsDao()

    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertNew() = runTest {
        val newExample = NewsEntity(id = 2, title = "nonumes", link = "yahoo.com")

        val newsList = listOf<NewsEntity>(
            NewsEntity(id = 1, title = "delicata", link = "audire.com"),
            newExample,
            NewsEntity(id = 3, title = "cumpa", link = "google.com")
        )

        dao.insertNews(newsList)

        val getNewsList = dao.getNewsList()

        assertThat(getNewsList).contains(newExample)
    }

    @Test

    fun updateNew() = runTest {
        val newsList = listOf<NewsEntity>(
            NewsEntity(id = 1, title = "delicata", link = "audire.com"),
            NewsEntity(id = 2, title = "nonumes", link = "yahoo.com"),
            NewsEntity(id = 3, title = "cumpa", link = "google.com")
        )

        dao.insertNews(newsList)

        dao.updateNews(id = 3, updatedTitle = "Bruno", updatedLink = "spotify.com")
        val newList = dao.getNewsList()
        assertThat(newList.map { it.title }).contains("Bruno")
    }

}

