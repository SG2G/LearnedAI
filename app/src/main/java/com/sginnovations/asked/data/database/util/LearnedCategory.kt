package com.sginnovations.asked.data.database.util

import com.sginnovations.asked.Constants.Companion.CATEGORY_MATH

interface LearnedCategory {
    val category: String
}

object Math: LearnedCategory {
    override val category = CATEGORY_MATH
}