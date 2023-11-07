package com.sginnovations.asked.data

interface Category {
    val name: String

}
object All : Category {
    override val name = "All"
}
object Text : Category {
    override val name = "Text"
}
object Math : Category {
    override val name = "Math"
}
