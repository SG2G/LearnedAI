package com.sginnovations.asked.presentation.ui.onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import com.sginnovations.asked.R

data class OnBoardingFeatures(
    val icon: Int,
    val title: String,
    val subtitle: String,
)

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
    fun getDescription(context: Context): String? = null
    fun getImage(context: Context): Int

    fun getFeatures(context: Context): List<OnBoardingFeatures>? = null
}

object WelcomeScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_1)
    override fun getSubTitle(context: Context) = context.getString(R.string.subscription_pvu)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_1)

    override fun getImage(context: Context) = R.drawable.onboarding_welcome

}

object CameraResult : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_camera_result)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_camera_result)

    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_camera_result)

    override fun getImage(context: Context) = R.drawable.onboarding_camera_reward

}

object CameraUse : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_camera_use)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_camera_use)

    //override fun getDescription(context: Context) =
        //context.getString(R.string.onboarding_description_camera_use)

    override fun getImage(context: Context) = R.drawable.onboarding_camera

    override fun getFeatures(context: Context) = listOf(
        OnBoardingFeatures(
            icon = R.drawable.camera_svgrepo_outlined,
            title = context.getString(R.string.onboarding_camera_title_1),
            subtitle = context.getString(R.string.onboarding_camera_subtitle_1),
        ),
        OnBoardingFeatures(
            icon = R.drawable.lightbulb_bolt_svgrepo_com,
            title = context.getString(R.string.onboarding_camera_title_2),
            subtitle = context.getString(R.string.onboarding_camera_subtitle_2),
        ),
        OnBoardingFeatures(
            icon = R.drawable.magnifying_glass_8_svgrepo_com,
            title = context.getString(R.string.onboarding_camera_title_3),
            subtitle = context.getString(R.string.onboarding_camera_subtitle_3),
        ),
        OnBoardingFeatures(
            icon = R.drawable.chat_svgrepo_outlined,
            title = context.getString(R.string.onboarding_camera_title_4),
            subtitle = context.getString(R.string.onboarding_camera_subtitle_4),
        ),
    )

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
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_assistant_use)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_assistant_use)

    //override fun getDescription(context: Context) =
      //  context.getString(R.string.onboarding_description_assistant_use)

    override fun getImage(context: Context) = R.drawable.onboarding_assistant_gif

    override fun getFeatures(context: Context) = listOf(
        OnBoardingFeatures(
            icon = R.drawable.sofa_svgrepo_outlined,
            title = context.getString(R.string.onboarding_assistant_title_1),
            subtitle = context.getString(R.string.onboarding_assistant_subtitle_1),
        ),
        OnBoardingFeatures(
            icon = R.drawable.book_svgrepo_com,
            title = context.getString(R.string.onboarding_assistant_title_2),
            subtitle = context.getString(R.string.onboarding_assistant_subtitle_2),
        ),
        OnBoardingFeatures(
            icon = R.drawable.road_sign_two_way_svgrepo_com,
            title = context.getString(R.string.onboarding_assistant_title_3),
            subtitle = context.getString(R.string.onboarding_assistant_subtitle_3),
        ),
        OnBoardingFeatures(
            icon = R.drawable.all_hours_service_svgrepo_com,
            title = context.getString(R.string.onboarding_assistant_title_4),
            subtitle = context.getString(R.string.onboarding_assistant_subtitle_4),
        ),
    )
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
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_guide_use)

    //override fun getDescription(context: Context) =
      //  context.getString(R.string.onboarding_description_guide_use)

    override fun getImage(context: Context) = R.drawable.onboarding_guide

    override fun getFeatures(context: Context) = listOf(
        OnBoardingFeatures(
            icon = R.drawable.mortarboard_svgrepo_com,
            title = context.getString(R.string.onboarding_guide_title_1),
            subtitle = context.getString(R.string.onboarding_guide_subtitle_1),
        ),
        OnBoardingFeatures(
            icon = R.drawable.growing_plant_svgrepo_com,
            title = context.getString(R.string.onboarding_guide_title_2),
            subtitle = context.getString(R.string.onboarding_guide_subtitle_2),
        ),
        OnBoardingFeatures(
            icon = R.drawable.home_with_a_heart_svgrepo_com,
            title = context.getString(R.string.onboarding_guide_title_3),
            subtitle = context.getString(R.string.onboarding_guide_subtitle_3),
        ),
    )

}

object PrivacyScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_5)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_5)
    //override fun getDescription(context: Context) =
      //  context.getString(R.string.onboarding_description_5)

    override fun getImage(context: Context) = R.drawable.onboarding_security

    override fun getFeatures(context: Context) = listOf(
        OnBoardingFeatures(
            icon = R.drawable.lock_svgrepo_com,
            title = context.getString(R.string.onboarding_privacy_title_1),
            subtitle = context.getString(R.string.onboarding_privacy_subtitle_1),
        ),
        OnBoardingFeatures(
            icon = R.drawable.eye_closed_svgrepo_com,
            title = context.getString(R.string.onboarding_privacy_title_2),
            subtitle = context.getString(R.string.onboarding_privacy_subtitle_2),
        ),
    )
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