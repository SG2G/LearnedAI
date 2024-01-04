package com.sginnovations.asked.utils

import android.content.Context
import com.sginnovations.asked.R

object LanguageName {

    fun languageName(
        context: Context,
        languageCode: String,
    ): String {
        return when (languageCode) {
            "en" -> context.getString(R.string.language_english)
            "es" -> context.getString(R.string.language_spanish)
            "fr" -> context.getString(R.string.language_french)
            "other" -> context.getString(R.string.language_other)
            else -> languageCode
        }
    }
}