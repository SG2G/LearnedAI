package com.sginnovations.asked.data

import android.content.Context
import androidx.compose.runtime.MutableState
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
    override val prefix = ""
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
    override fun getName(context: Context) = "Translate"
    override fun getPrefix(context: Context) = "Translate this text: "
}
object SummaryCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Summary"
    override fun getName(context: Context) = "Summary"
    override fun getPrefix(context: Context) = "Summary this text: "
}
object GrammarCategoryOCR : CategoryOCR {
    override val root = "Text"
    override val prefix = "Grammar"
    override fun getName(context: Context) = "Grammar"
    override fun getPrefix(context: Context) = "Correct the grammar: "
}
