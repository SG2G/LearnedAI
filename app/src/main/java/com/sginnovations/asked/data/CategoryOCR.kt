package com.sginnovations.asked.data

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import com.sginnovations.asked.R

interface CategoryOCR {
    val root: String
    val prefix: String
    fun getName(context: Context): String
    fun getPrefix(context: Context): String

}
object All : CategoryOCR {
    override val root = "All"
    override val prefix = "All"
    override fun getName(context: Context) = context.getString(R.string.category_all)
    override fun getPrefix(context: Context) = ""
}
object MathCategoryOCR : CategoryOCR {
    override val root = "Math"
    override val prefix = "Math"
    override fun getName(context: Context) = context.getString(R.string.category_math)
    override fun getPrefix(context: Context) = ""
}
object TextCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Text"
    override fun getName(context: Context) = context.getString(R.string.category_text)
    override fun getPrefix(context: Context) = ""
}
object TranslateCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Translate"
    override fun getName(context: Context) = context.getString(R.string.category_translate)
    override fun getPrefix(context: Context) =
        context.getString(R.string.category_prefix_translate_to_this_text)
}
object SummaryCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Summary"
    override fun getName(context: Context) = context.getString(R.string.category_summary)
    override fun getPrefix(context: Context) =
        context.getString(R.string.category_prefix_summary_this_text)
}
object GrammarCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Grammar"
    override fun getName(context: Context) = context.getString(R.string.category_grammar)
    override fun getPrefix(context: Context) =
        context.getString(R.string.category_prefix_correct_the_grammar)
}
object Soon : CategoryOCR {
    override val root = "Text"
    override val prefix = "Grammar"
    override fun getName(context: Context) = "Soon..."
    override fun getPrefix(context: Context) = "Soon..."
}
