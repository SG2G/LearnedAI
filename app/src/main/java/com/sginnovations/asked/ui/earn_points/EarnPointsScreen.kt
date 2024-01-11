@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.earn_points

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.ui.ui_components.tokens.TokensCard
import com.sginnovations.asked.viewmodel.RemoteConfigViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "EarnPointsStateFul"

@Composable
fun EarnPointsStateFul(
    vmToken: TokenViewModel,

    onNavigateSubscriptions: () -> Unit,
    onNavigateRefCode: () -> Unit,
) {
    val tokens = vmToken.tokens.collectAsState()

    EarnPointsStateLess(
        tokens = tokens,

        onNavigateSubscriptions = { onNavigateSubscriptions() },
        onNavigateRefCode = { onNavigateRefCode() },
        onSwitchVisibility = { vmToken.switchPointsVisibility() },
    )
}

@Composable
fun EarnPointsStateLess(
    vmRemoteConfig: RemoteConfigViewModel = hiltViewModel(), //TODO REMOTE CONFIG VM

    tokens: State<Long>,

    onNavigateSubscriptions: () -> Unit,
    onNavigateRefCode: () -> Unit,

    onSwitchVisibility: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp / 5

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onSwitchVisibility() },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = screenHeight),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TokenDisplay(
                    tokens = tokens,
                    showPlus = false
                ) {}
                Text(
                    text = stringResource(R.string.earn_token_earn_more_tokens), color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TokensCard(
                num = stringResource(R.string.infinite),
                text = stringResource(R.string.earn_token_unlimited_points),
                buttonText = stringResource(R.string.earn_token_see_more),
                borderColor = MaterialTheme.colorScheme.primary,
                buttonColor = MaterialTheme.colorScheme.background,
                cardContainerColor = MaterialTheme.colorScheme.surface,
                onClick = { onNavigateSubscriptions() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TokensCard(
                num = "+" + vmRemoteConfig.getInviteRewardTokens(),
                text = stringResource(R.string.earn_token_invite_friends),
                buttonText = stringResource(R.string.earn_token_button_invite),
                borderColor = Color.Transparent,
                buttonColor = MaterialTheme.colorScheme.background,
                cardContainerColor = MaterialTheme.colorScheme.surface,
                onClick = { onNavigateRefCode() }
            )

        }
    } // ModalBottom
}
