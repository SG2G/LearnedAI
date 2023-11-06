package com.sginnovations.asked.data.mathpix

data class MathpixResponse(
//    val confidence: Double,
    val data: List<DataItem>,
//    val isHandwritten: Boolean,
//    val isPrinted: Boolean,
//    val text: String,
//    val version: String
)

data class DataItem(
    val type: String,
    val value: String
)

