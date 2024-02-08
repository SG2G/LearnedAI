package com.sginnovations.asked.domain.usecase

import com.sginnovations.asked.data.repository.ArticlesRepositoryImpl
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepositoryImpl,
) {
    suspend operator fun invoke(url: String) = repository.getArticles(url)
}
