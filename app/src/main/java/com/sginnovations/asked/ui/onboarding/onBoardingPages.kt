package com.sginnovations.asked.ui.onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon

class OnBoarding(
    val context: Context,
) : AppCompatActivity() {

    fun getAllPages(): List<OnBoardingPage> {
        return listOf(
            FirstScreen,
            SecondScreen,
            ThirdScreen,
            FourScreen,
            FifthScreen,
//            SixthScreen
        )
    }
}

interface OnBoardingPage {
    fun getTitle(context: Context): String
    fun getSubTitle(context: Context): String
    fun getDescription(context: Context): String
    val image: @Composable () -> Painter
}

object FirstScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_1)
    override fun getSubTitle(context: Context) = ""
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_1)
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.onboarding_welcome)
    }
}

object SecondScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_2)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_2)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_2)
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.onboarding_camera)
    }
}

object ThirdScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_titlte_3)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_3)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_3)
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.onboarding_assistant)
    }
}

object FourScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_4)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_4)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_4)
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.onboarding_guide)
    }
}
object FifthScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_5)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_5)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_5)
    override val image: @Composable () -> Painter = {
        painterResource(id = R.drawable.onboarding_security)
    }
}
//object SixthScreen : OnBoardingPage { //TODO DO IT
//    override fun getTitle(context: Context) = "Disfruta Asked"
//    override fun getSubTitle(context: Context) = "Un regalo para ti"
//    override fun getDescription(context: Context) = "Te doy 7 Tokens"
//    override val image: @Composable () -> Painter = {
//        painterResource(id = R.drawable.onboarding_security)
//    }
//}