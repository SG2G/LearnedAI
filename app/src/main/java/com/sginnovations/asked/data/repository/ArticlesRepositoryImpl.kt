package com.sginnovations.asked.data.repository

import com.sginnovations.asked.data.network.CloudService
import com.sginnovations.asked.data.rss_dataclass.Article
import com.sginnovations.asked.domain.repository.ArticlesRepository

import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    private val cloudService: CloudService
) : ArticlesRepository {
    override suspend fun getArticles(url: String): List<Article> {
        return cloudService.getArticles(url)
    }
}

