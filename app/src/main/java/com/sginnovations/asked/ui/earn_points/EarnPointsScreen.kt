@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.earn_points

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants.Companion.AD_REWARD_NUM_TOKEN
import com.sginnovations.asked.Constants.Companion.INVITE_REWARD_NUM_TOKEN
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.tokens.PointsDisplay
import com.sginnovations.asked.ui.ui_components.tokens.TokensCard
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "EarnPointsStateFul"
@Composable
fun EarnPointsStateFul(
    vmToken: TokenViewModel,
    vmAds: AdsViewModel,

    onNavigateSubscriptions: () -> Unit,
    onNavigateRefCode: () -> Unit,
) {
    val context = LocalContext.current
    fun Context.getActivity(): Activity? {
        return when(this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }
    val activity = context.getActivity()

    val tokens = vmToken.tokens.collectAsState()

    LaunchedEffect(Unit) {
        Log.i(TAG, "Loading Ad in LauncherEffect")
        vmAds.loadRewardedAd(context)
    }

    EarnPointsStateLess(
        tokens = tokens,

        onShowAd = {
            if (activity != null) {
                vmAds.showRewardedAd(activity)
            }
        },
        onNavigateSubscriptions = { onNavigateSubscriptions() },
        onNavigateRefCode = { onNavigateRefCode() },
        onSwitchVisibility = { vmToken.switchPointsVisibility() },
    )
}

@Composable
fun EarnPointsStateLess(
    tokens: State<Long>,

    onNavigateSubscriptions: () -> Unit,
    onNavigateRefCode: () -> Unit,
    onShowAd: () -> Unit,

    onSwitchVisibility: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onSwitchVisibility() },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                PointsDisplay(
                    tokens = tokens,
                    showPlus = false
                ) {}
            }
            Text(text = "Hola")

            Space(n = 16)
            TokensCard(
                num = "∞",
                text = stringResource(R.string.earn_token_unlimited_points),
                buttonText = stringResource(R.string.earn_token_see_more),
                borderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                buttonColor = MaterialTheme.colorScheme.background,
                cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onNavigateSubscriptions() }
            )
            Space(16)
            TokensCard(
                num = "+$AD_REWARD_NUM_TOKEN",
                text = stringResource(R.string.earn_token_watch),
                buttonText = stringResource(R.string.earn_token_watch),
                borderColor = Color.Transparent,
                buttonColor = MaterialTheme.colorScheme.background,
                cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onShowAd() }
            )
            Space(16)
            TokensCard(
                num = "+$INVITE_REWARD_NUM_TOKEN",
                text = stringResource(R.string.earn_token_invite_friends),
                buttonText = stringResource(R.string.earn_token_button_invite),
                borderColor = Color.Transparent,
                buttonColor = MaterialTheme.colorScheme.background,
                cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onNavigateRefCode() }
            )
        }
    } // ModalBottom
}

@Composable
fun Space(n: Int) {
    Spacer(modifier = Modifier.height(n.dp))
}



