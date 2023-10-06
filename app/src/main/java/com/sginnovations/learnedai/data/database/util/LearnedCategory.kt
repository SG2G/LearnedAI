package com.sginnovations.learnedai.data.database.util

import com.sginnovations.learnedai.Constants.Companion.CATEGORY_MATH

interface LearnedCategory {
    val category: String
}

object Math: LearnedCategory {
    override val category = CATEGORY_MATH
}