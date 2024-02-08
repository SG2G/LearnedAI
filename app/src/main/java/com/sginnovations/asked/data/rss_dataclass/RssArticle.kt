package com.sginnovations.asked.data.rss_dataclass

data class Article(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val category: String,
    val imageUrl: String
)
