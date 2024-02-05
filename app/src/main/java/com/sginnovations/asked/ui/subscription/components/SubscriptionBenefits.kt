package com.sginnovations.asked.ui.subscription.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun SubscriptionBenefits() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 8.dp),
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth(0.5f)
//        ) {
//            TitleBenefit(
//                painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
//                text = stringResource(R.string.subscription_unlimited)
//            )
//            SubTitleBenefit(text = stringResource(R.string.subscription_unlimited_text))
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            TitleBenefit(
//                painterResource = painterResource(id = R.drawable.camera_svgrepo_filled),
//                text = stringResource(R.string.subscription_camera_title)
//            )
//            SubTitleBenefit(text = stringResource(R.string.subscription_camera_text))
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            TitleBenefit(
//                painterResource = painterResource(id = R.drawable.subscription_star2),
//                text = stringResource(R.string.subscription_exclusive_functions)
//            )
//            SubTitleBenefit(text = stringResource(id = R.string.subscription_higher_word_limit))
//        }
//
//        Column {
//            TitleBenefit(
//                painterResource = painterResource(id = R.drawable.sofa_svgrepo_filled),
//                text = stringResource(R.string.subscription_assistant_title)
//            )
//            SubTitleBenefit(text = stringResource(R.string.subscription_assistant_text))
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            TitleBenefit(
//                painterResource = painterResource(id = R.drawable.book_bookmark_svgrepo_filled),
//                text = stringResource(R.string.subscription_guide_title)
//            )
//            SubTitleBenefit(text = stringResource(R.string.subscription_guide_text))
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TitleBenefit(
                    painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
                    text = stringResource(R.string.subscription_unlimited)
                )
                SubTitleBenefit(text = stringResource(R.string.subscription_unlimited_text))
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TitleBenefit(
                    painterResource = painterResource(id = R.drawable.camera_svgrepo_filled),
                    text = stringResource(R.string.subscription_camera_title)
                )
                SubTitleBenefit(text = stringResource(R.string.subscription_camera_text))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TitleBenefit(
                    painterResource = painterResource(id = R.drawable.sofa_svgrepo_filled),
                    text = stringResource(R.string.subscription_assistant_title)
                )
                SubTitleBenefit(text = stringResource(R.string.subscription_assistant_text))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TitleBenefit(
                    painterResource = painterResource(id = R.drawable.book_bookmark_svgrepo_filled),
                    text = stringResource(R.string.subscription_guide_title)
                )
                SubTitleBenefit(text = stringResource(R.string.subscription_guide_text))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TitleBenefit(
                    painterResource = painterResource(id = R.drawable.subscription_star2),
                    text = stringResource(R.string.subscription_exclusive_functions)
                )
                SubTitleBenefit(text = stringResource(id = R.string.subscription_higher_word_limit))
            }
        }
    }
}