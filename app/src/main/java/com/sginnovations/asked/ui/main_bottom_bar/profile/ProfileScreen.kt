package com.sginnovations.asked.ui.main_bottom_bar.profile

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.UserData
import com.sginnovations.asked.ui.ui_components.profile.DeleteAccountButton
import com.sginnovations.asked.ui.ui_components.profile.LogOutButton
import com.sginnovations.asked.ui.ui_components.profile.ProfileButton
import com.sginnovations.asked.ui.ui_components.profile.ProfilePicture
import com.sginnovations.asked.ui.ui_components.tokens.PointsDisplay
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "StateFulProfile"

@Composable
fun StateFulProfile(
    vmToken: TokenViewModel,
    vmAuth: AuthViewModel,

    onNavigateUserNotLogged: () -> Unit,

    onNavigateRefCode: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val userAuth = vmAuth.userAuth.collectAsState()

    StateLessProfile(
        vmToken = vmToken,

        userAuth = userAuth,

        onSignOut = {
            scope.launch {
                vmAuth.signOut()
                onNavigateUserNotLogged()
            }
        },
        onDeleteAccount = {
            scope.launch {  }

        },
        onNavigateRefCode = { onNavigateRefCode() },
        onNavigateSubscriptions = { onNavigateSubscriptions() }
    )
}

@Composable
fun StateLessProfile(
    vmToken: TokenViewModel,

    userAuth: State<UserData?>,

    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,

    onNavigateRefCode: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val tokens = vmToken.tokens.collectAsStateWithLifecycle()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            TextButton(
                onClick = { /*nNavigateSettings()*/ },
                shape = RoundedCornerShape(10),
            ) {
                ProfilePicture(userAuthPhotoUrl = userAuth.value?.profilePictureUrl) //TODO ?
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = userAuth.value?.userName!!,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    PointsDisplay(modifier = Modifier.scale(0.8f),tokens = tokens, showPlus = false) { vmToken.switchPointsVisibility() }
                }
                Button(
                    onClick = { onNavigateSubscriptions() },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Upgrade")
                }
            }
        }

        LogOutButton(onClick = { onSignOut() })
        DeleteAccountButton(onClick = { onDeleteAccount() })

        Box(modifier = Modifier.padding(8.dp)) {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                ProfileButton(
                    text = "Get more Tokens",
                    painterResource = painterResource(id = R.drawable.logout_fill0_wght400_grad0_opsz48),
                    onClick = { vmToken.switchPointsVisibility() }
                )
                ProfileButton(
                    text = "Invite Friends",
                    painterResource = painterResource(id = R.drawable.logout_fill0_wght400_grad0_opsz48),
                    onClick = { onNavigateRefCode() }
                )
            }
        }


        /**
         * Testing
         */
        Text(text = "Testing")
        Button(onClick = {
            scope.launch {
                vmToken.oneLessToken()
            }
        }
        ) {
            Text(text = "Rest 1")
        }
        Button(onClick = { onSignOut() }) {
            Text(text = "Sign Out")
        }
        Button(onClick = { vmToken.testGivePoints() }) {
            Text(text = "Give Tokens")
        }
        Button(onClick = { onNavigateRefCode() }) {
            Text(text = "Ref coded / subscription")
        }
    }
}

