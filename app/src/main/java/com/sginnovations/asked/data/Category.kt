package com.sginnovations.asked.data

import android.content.Context
import com.sginnovations.asked.R

interface Category {
    val root: String
    fun getName(context: Context): String

}
object All : Category {
    override val root = "All"
    override fun getName(context: Context) = context.getString(R.string.category_all)
}
object Text : Category {
    override val root = "Text"
    override fun getName(context: Context) = context.getString(R.string.category_text)
}
object Math : Category {
    override val root = "Math"
    override fun getName(context: Context) = context.getString(R.string.category_math)
}
