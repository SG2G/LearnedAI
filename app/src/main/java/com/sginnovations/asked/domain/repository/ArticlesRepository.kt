package com.sginnovations.asked.domain.repository

import com.sginnovations.asked.data.rss_dataclass.Article

interface ArticlesRepository {
    suspend fun getArticles(url: String): List<Article>
}
