package com.sginnovations.asked.data.report

data class Report(
    val reason: String,
    val comments: String?,
    val timestamp: Long = System.currentTimeMillis()
)
