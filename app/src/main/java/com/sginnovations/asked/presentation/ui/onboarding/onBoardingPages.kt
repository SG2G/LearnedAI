package com.sginnovations.asked.presentation.ui.onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.sginnovations.asked.R

class OnBoarding(
    val context: Context,
) : AppCompatActivity() {

    fun getAllPages(): List<OnBoardingPage> {
        return listOf(
            WelcomeScreen,
            CameraResult,
            CameraUse,
            AssistantResult,
            AssistantUse,
            GuideResult,
            GuideUse,
            PrivacyScreen,
            FinalScreen
        )
    }
    fun getNumberOfPages(): Int {
        return getAllPages().size
    }
}

interface OnBoardingPage {
    fun getTitle(context: Context): String
    fun getSubTitle(context: Context): String
    fun getDescription(context: Context): String
    fun getImage(context: Context): Int
}

object WelcomeScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_1)
    override fun getSubTitle(context: Context) = context.getString(R.string.subscription_pvu)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_1)
    override fun getImage(context: Context) = R.drawable.onboarding_welcome

}
object CameraResult: OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_camera_result)
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_camera_result)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_camera_result)
    override fun getImage(context: Context) = R.drawable.onboarding_camera_reward

}
object CameraUse : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_camera_use)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_camera_use)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_camera_use)
    override fun getImage(context: Context) = R.drawable.onboarding_camera

}
object AssistantResult : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_assistant_result)
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_assistant_result)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_assistant_result)
    override fun getImage(context: Context) = R.drawable.onboarding_assistant_reward

}


object AssistantUse : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_assistant_use)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_assistant_use)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_assistant_use)
    override fun getImage(context: Context) = R.drawable.onboarding_assistant_gif

}

object GuideResult : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_guide_result)
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_guide_result)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_guide_result)
    override fun getImage(context: Context) = R.drawable.onboarding_guide_reward

}

object GuideUse : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_guide_use)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_guide_use)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_guide_use)
    override fun getImage(context: Context) = R.drawable.onboarding_guide

}
object PrivacyScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_5)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_5)
    override fun getDescription(context: Context) = context.getString(R.string.onboarding_description_5)
    override fun getImage(context: Context) = R.drawable.onboarding_security

}

object FinalScreen : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_finalscreen_title)
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_finalscreen_subtitle)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_finalscreen_description)
    override fun getImage(context: Context) = R.drawable.happy_family_end

}