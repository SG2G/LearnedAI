package com.sginnovations.asked.data.database.util

import com.sginnovations.asked.Constants.Companion.ROLE_ASSISTANT
import com.sginnovations.asked.Constants.Companion.ROLE_USER

interface Role {
    val role: String
    val name: String
}

object User: Role {
    override val role = ROLE_USER
    override val name = "User"
}

object Assistant: Role {
    override val role = ROLE_ASSISTANT
    override val name = "LearnedAI"
}