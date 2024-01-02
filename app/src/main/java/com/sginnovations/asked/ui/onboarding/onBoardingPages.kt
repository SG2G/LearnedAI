package com.sginnovations.asked.ui.onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.sginnovations.asked.R

class OnBoarding(
    val context: Context,
) : AppCompatActivity() {

    fun getAllPages(): List<OnBoardingPage> {
        return listOf(
            FirstScreen,
            SecondScreen,
            ThirdScreen,
        )
    }
}

interface OnBoardingPage {
    fun getTitle(context: Context): String
    fun getDescription(context: Context): String
    val image: @Composable () -> Painter
}

object FirstScreen : OnBoardingPage {
    override fun getTitle(context: Context) = "title"
    override fun getDescription(context: Context) =
        "description"
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.chat_svgrepo_filled)
    }
}

object SecondScreen : OnBoardingPage {
    override fun getTitle(context: Context) = "title"
    override fun getDescription(context: Context) =
        "description"
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.chat_svgrepo_filled)
    }
}

object ThirdScreen : OnBoardingPage {
    override fun getTitle(context: Context) = "title"
    override fun getDescription(context: Context) =
        "description"
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.chat_svgrepo_filled)
    }
}

//object FourScreen : OnBoardingPage {
//    override fun getTitle(context: Context) = context.getString(R.string.settings)
//    override fun getDescription(context: Context) = "Description"
//    override val image: @Composable () -> Painter = {
//        painterResource(id = R.drawable.dark_mode)
//    }
//}