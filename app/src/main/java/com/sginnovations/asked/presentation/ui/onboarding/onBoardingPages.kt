package com.sginnovations.asked.presentation.ui.onboarding

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.sginnovations.asked.R

data class OnBoardingFeatures(
    val icon: Int,
    val title: String,
    val subtitle: String,
)
data class OnBoardingOneSelection(
    val text1: String,
    val text2: String? = null,
    var selected: Boolean = false
)
data class OnBoardingMultipleSelection(
    val name: String,
    @DrawableRes val icon: Int,
    var selected: Boolean = false
)
data class OnBoardingResponses(
    val yesText: String,
    val noText: String,
)

enum class OnBoardingType { Default, GenderSelect, SingleSelect, MultipleSelect, Quote, Personalization, Response, Section, ChildName }

class OnBoarding(
    val context: Context,
) : AppCompatActivity() {

    fun getAllPages(): List<OnBoardingPage> {
        return listOf(
            WelcomeScreen,
            //CameraResult,
            CameraUse,
            //AssistantResult,
            AssistantUse,
            //GuideResult,
            GuideUse,
//            PrivacyScreen,

//            PersonalizationScreen,

            KidAgeSelect,
            ChildNameSelect,
            InterestSelect,
            ChildInterestSelect,
            TimeGoalSelect,

            AgeSelect,
            GenderScreen,

            GoalsSelect,

            QuoteOneScreen,
            QuoteTwoScreen,
            QuoteThreeScreen,
            QuoteResponse,

            FeelingReachingGoalsSelect,
            RewardAfterReachingGoalsSelect,

            FinalScreen,
            CreatingPersonalization,
        )
    }

    fun getNumberOfPages(): Int {
        return getAllPages().size
    }
}

interface OnBoardingPage {
    fun getType(context: Context): OnBoardingType = OnBoardingType.Default
    fun getTitle(context: Context): String
    fun getSubTitle(context: Context): String
    fun getDescription(context: Context): String? = null
    fun getImage(context: Context): Int? = null
    fun getFeatures(context: Context): List<OnBoardingFeatures>? = null
    fun getMultipleOptions(context: Context): List<OnBoardingMultipleSelection>? = null
    fun getOneOptions(context: Context): List<OnBoardingOneSelection>? = null
    fun getResponses(context: Context): OnBoardingResponses? = null
}

object PersonalizationScreen : OnBoardingPage {
    override fun getType(context: Context) = OnBoardingType.Section
    override fun getTitle(context: Context) = context.getString(R.string.personalize_title)
    override fun getSubTitle(context: Context) = context.getString(R.string.personalize_subtitle)

}

/**
 * Features
 */
object WelcomeScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_1)
    override fun getSubTitle(context: Context) = context.getString(R.string.subscription_pvu)
    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_description_1)

    override fun getImage(context: Context) = R.drawable.onboarding_welcome

}
object CameraUse : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_camera_use)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_camera_use)

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
object AssistantUse : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_title_assistant_use)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_assistant_use)

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
object GuideUse : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_guide_use)
    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_subtitle_guide_use)

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

/**
 * Personalization
 */
object GenderScreen : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.GenderSelect
    override fun getTitle(context: Context) = context.getString(R.string.select_your_gender)
    override fun getSubTitle(context: Context) = ""
}
object AgeSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
    override fun getTitle(context: Context) = context.getString(R.string.select_your_age_range)
    override fun getSubTitle(context: Context) = ""

    override fun getOneOptions(context: Context) = listOf(
        OnBoardingOneSelection(text1 = "18-24", text2 = null),
        OnBoardingOneSelection(text1 = "25-34", text2 = null),
        OnBoardingOneSelection(text1 = "35-44", text2 = null),
        OnBoardingOneSelection(text1 = "45-54", text2 = null),
        OnBoardingOneSelection(text1 = "+55", text2 = null),
    )
}
object KidAgeSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
    override fun getTitle(context: Context) = context.getString(R.string.select_your_child_s_age)
    override fun getSubTitle(context: Context) = ""

    override fun getOneOptions(context: Context) = listOf(
        OnBoardingOneSelection(text1 = "0-2", text2 = null),
        OnBoardingOneSelection(text1 = "3-5", text2 = null),
        OnBoardingOneSelection(text1 = "6-8", text2 = null),
        OnBoardingOneSelection(text1 = "9-11", text2 = null),
        OnBoardingOneSelection(text1 = "12-15", text2 = null),
        OnBoardingOneSelection(text1 = "+16", text2 = null),
    )
}
object TimeGoalSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
    override fun getTitle(context: Context) = context.getString(R.string.select_your_goal)
    override fun getSubTitle(context: Context) = ""

    override fun getOneOptions(context: Context) = listOf(
        OnBoardingOneSelection(text1 = context.getString(R.string.casual), text2 = context.getString(
            R.string.minutes_day_5
        )),
        OnBoardingOneSelection(text1 = context.getString(R.string.regular), text2 = context.getString(
            R.string.minutes_day_10
        )),
        OnBoardingOneSelection(text1 = context.getString(R.string.serious), text2 = context.getString(
            R.string.minutes_day_15
        )),
        OnBoardingOneSelection(text1 = context.getString(R.string.tireless), text2 = context.getString(
            R.string.minutes_day_20
        )),
    )
}

object ChildNameSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.ChildName
    override fun getTitle(context: Context) = ""
    override fun getSubTitle(context: Context) = ""
}

/**
 * Quotes
 */
object QuoteOneScreen : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Quote
    override fun getTitle(context: Context) = ""

    override fun getSubTitle(context: Context) = ""

    override fun getDescription(context: Context) = context.getString(R.string.quote_1)

}

object QuoteTwoScreen : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Quote
    override fun getTitle(context: Context) = ""

    override fun getSubTitle(context: Context) = ""

    override fun getDescription(context: Context) = context.getString(R.string.quote_2)

}
object QuoteThreeScreen : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Quote
    override fun getTitle(context: Context) = ""

    override fun getSubTitle(context: Context) = ""

    override fun getDescription(context: Context) = context.getString(R.string.quote_3)

}

object QuoteResponse: OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Response
    override fun getTitle(context: Context) = context.getString(R.string.together_in_every_step)

    override fun getSubTitle(context: Context) = ""
    override fun getImage(context: Context) = R.drawable.team_work_rafiki

    override fun getResponses(context: Context) =
        OnBoardingResponses(
            yesText = context.getString(R.string.good_response_1),
            noText = context.getString(R.string.bad_response_1),
        )
}

/**
 * Multiple Option Select
 */
object InterestSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.MultipleSelect
    override fun getTitle(context: Context) = context.getString(R.string.what_are_your_interest)
    override fun getSubTitle(context: Context) = context.getString(R.string.select_up_to_3_goals_for_more_accurate_customization)
    override fun getMultipleOptions(context: Context): List<OnBoardingMultipleSelection>? = listOf(
        OnBoardingMultipleSelection(context.getString(R.string.emotional_understanding), R.drawable.lightbulb_bolt_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.building_a_united_family), R.drawable.home_with_a_heart_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.parental_peace_of_mind), R.drawable.sofa_svgrepo_filled),
        OnBoardingMultipleSelection(context.getString(R.string.study_support), R.drawable.camera_svgrepo_filled),
        OnBoardingMultipleSelection(context.getString(R.string.quality_information), R.drawable.book_bookmark_svgrepo_filled),
        OnBoardingMultipleSelection(context.getString(R.string.guidance_and_teaching), R.drawable.compass_svgrepo_com),
    )
}
object ChildInterestSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.MultipleSelect
    override fun getTitle(context: Context) = context.getString(R.string.child_interest_title)
    override fun getSubTitle(context: Context) = context.getString(R.string.select_up_to_3_goals_for_more_accurate_customization)
    override fun getMultipleOptions(context: Context): List<OnBoardingMultipleSelection> = listOf(
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_1), R.drawable.book_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_2), R.drawable.brain_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_3), R.drawable.paint_palette_painting_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_4), R.drawable.dumbbell_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_5), R.drawable.lightbulb_bolt_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.child_interest_6), R.drawable.compass_svgrepo_com),

    )
}

object GoalsSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.MultipleSelect
    override fun getTitle(context: Context) = context.getString(R.string.what_are_your_goals)
    override fun getSubTitle(context: Context) = context.getString(R.string.select_up_to_3_goals_for_more_accurate_customization)
    override fun getMultipleOptions(context: Context) = listOf(
        OnBoardingMultipleSelection(context.getString(R.string.goal_1), R.drawable.support_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_2),R.drawable.growth_report_graph_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_3), R.drawable.shield_alt_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_4), R.drawable.heart_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_5), R.drawable.lightbulb_bolt_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_6), R.drawable.happy_face_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_7), R.drawable.home_with_a_heart_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_8), R.drawable.compass_svgrepo_com),
    )
}
object FeelingReachingGoalsSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.MultipleSelect
    override fun getTitle(context: Context) = context.getString(R.string.goal_reach_title)
    override fun getSubTitle(context: Context) = ""
    override fun getMultipleOptions(context: Context) = listOf(
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_1), R.drawable.rocket_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_2), R.drawable.growing_plant_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_3), R.drawable.book_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_4), R.drawable.compass_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_5), R.drawable.star_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_feeling_6), R.drawable.subscription_infinite),
    )
}
object RewardAfterReachingGoalsSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.MultipleSelect
    override fun getTitle(context: Context) = context.getString(R.string.goal_reach_reward_title)
    override fun getSubTitle(context: Context) = ""
    override fun getMultipleOptions(context: Context) = listOf(
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_1), R.drawable.beach_chair_summer_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_2), R.drawable.lotus_yoga_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_3), R.drawable.paint_palette_painting_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_4), R.drawable.popcorn_movie_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_5), R.drawable.fork_and_knife_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_6), R.drawable.suitcase_transport_svgrepo_com),
        OnBoardingMultipleSelection(context.getString(R.string.goal_reach_reward_7), R.drawable.shopping_bag_svgrepo_com),
    )
}

/**
 * End
 */
object FinalScreen : OnBoardingPage {
    override fun getTitle(context: Context) =
        context.getString(R.string.onboarding_finalscreen_title)

    override fun getSubTitle(context: Context) =
        context.getString(R.string.onboarding_finalscreen_subtitle)

    override fun getDescription(context: Context) =
        context.getString(R.string.onboarding_finalscreen_description)

    override fun getImage(context: Context) = R.drawable.happy_family_end

}

object CreatingPersonalization : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Personalization
    override fun getTitle(context: Context) = ""

    override fun getSubTitle(context: Context) = ""

    override fun getDescription(context: Context) = ""

}

/**
 * Other
 */

object PrivacyScreen : OnBoardingPage {
    override fun getTitle(context: Context) = context.getString(R.string.onboarding_title_5)
    override fun getSubTitle(context: Context) = context.getString(R.string.onboarding_subtitle_5)

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
//object AssistantResult : OnBoardingPage {
//    override fun getTitle(context: Context) =
//        context.getString(R.string.onboarding_title_assistant_result)
//
//    override fun getSubTitle(context: Context) =
//        context.getString(R.string.onboarding_subtitle_assistant_result)
//
//    override fun getDescription(context: Context) =
//        context.getString(R.string.onboarding_description_assistant_result)
//
//    override fun getImage(context: Context) = R.drawable.onboarding_assistant_reward
//
//}

//object CameraResult : OnBoardingPage {
//    override fun getTitle(context: Context) =
//        context.getString(R.string.onboarding_title_camera_result)
//
//    override fun getSubTitle(context: Context) =
//        context.getString(R.string.onboarding_subtitle_camera_result)
//
//    override fun getDescription(context: Context) =
//        context.getString(R.string.onboarding_description_camera_result)
//
//    override fun getImage(context: Context) = R.drawable.onboarding_camera_reward
//
//}

//object GuideResult : OnBoardingPage {
//    override fun getTitle(context: Context) =
//        context.getString(R.string.onboarding_title_guide_result)
//
//    override fun getSubTitle(context: Context) =
//        context.getString(R.string.onboarding_subtitle_guide_result)
//
//    override fun getDescription(context: Context) =
//        context.getString(R.string.onboarding_description_guide_result)
//
//    override fun getImage(context: Context) = R.drawable.onboarding_guide_reward
//
//}