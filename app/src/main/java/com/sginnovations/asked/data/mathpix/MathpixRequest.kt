package com.sginnovations.asked.data.mathpix

data class MathpixRequest(
    val src: String,  // Image URL
    val formats: List<String>,  // List of formats (e.g., text, data, html)
    val data_options: DataOptions
)

data class DataOptions(
    val include_asciimath: Boolean
)

