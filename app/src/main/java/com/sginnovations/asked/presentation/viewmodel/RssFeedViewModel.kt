package com.sginnovations.asked.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.data.rss_dataclass.Article
import com.sginnovations.asked.domain.usecase.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RssFeedViewModel"
@HiltViewModel
class RssFeedViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase
    // Inject other services if needed
): ViewModel() {

    private val _uiState = MutableStateFlow<UiResult<HomeUiState>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<HomeUiState>> = _uiState.asStateFlow()

    fun loadArticles(url: String) {
        Log.d(TAG, "loadArticles: starting")
        viewModelScope.launch {
            try {
                val articles = getArticlesUseCase(url)
                _uiState.value = UiResult.Success(HomeUiState(articles))
            } catch (exception: Exception) {
                _uiState.value = UiResult.Fail(exception)
            }
        }
    }
}

sealed class UiResult<out T> {
    object Loading : UiResult<Nothing>()
    data class Success<T>(val data: T) : UiResult<T>()
    data class Fail(val error: Throwable) : UiResult<Nothing>()
}

data class HomeUiState(
    val articles: List<Article>
)

