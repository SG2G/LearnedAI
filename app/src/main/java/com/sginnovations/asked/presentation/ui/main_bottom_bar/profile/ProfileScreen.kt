package com.sginnovations.asked.presentation.ui.main_bottom_bar.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.presentation.ui.ui_components.profile.LogOutButton
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfileButton
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfileName
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfilePicture
import com.sginnovations.asked.presentation.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.presentation.viewmodel.AuthViewModel
import com.sginnovations.asked.presentation.viewmodel.IntentViewModel
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "StateFulProfile"

@Composable
fun StateFulProfile(
    vmToken: TokenViewModel,
    vmAuth: AuthViewModel,
    vmIntent: IntentViewModel,

    onNavigateUserNotLogged: () -> Unit,

    onNavigateRefCode: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
    val context = LocalContext.current
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

        onSendEmail = { vmIntent.sendEmail(context, userAuth) },
        onRateUs = { vmIntent.rateUs(context) },
        onManageSubscription = { vmIntent.manageSubscription(context) },

        onNavigateRefCode = { onNavigateRefCode() },
        onNavigateSubscriptions = { onNavigateSubscriptions() }
    )
}

@Composable
fun StateLessProfile(
    vmToken: TokenViewModel,

    userAuth: State<UserData?>,

    onSignOut: () -> Unit,
    onSendEmail: () -> Unit,
    onRateUs: () -> Unit,
    onManageSubscription: () -> Unit,

    onNavigateRefCode: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
    val tokens = vmToken.tokens.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val cardShape = RoundedCornerShape(25.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)

    ) {
        /**
         * Profile User
         */
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = cardShape
        ) {
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
                        ProfileName(userAuth.value?.userName.toString())
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TokenDisplay(
                                modifier = Modifier.scale(0.8f),
                                tokens = tokens,
                                showPlus = false
                            ) { vmToken.switchPointsVisibility() }

                            Text(
                                modifier = Modifier.clickable {
                                    onManageSubscription()
                                },
                                text = stringResource(R.string.profile_manage_subscription),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    Button(
                        onClick = { onNavigateSubscriptions() },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.profile_upgrade),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        /**
         * Profile Buttons
         */
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = cardShape
        ) {
            Column {
                ProfileButton(
                    text = stringResource(R.string.profile_get_more_tokens),
                    painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
                    onClick = { vmToken.switchPointsVisibility() }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.DarkGray
                )
                ProfileButton(
                    text = stringResource(R.string.profile_invite_friends),
                    painterResource = painterResource(id = R.drawable.share_fill0_wght400_grad0_opsz48),
                    onClick = { onNavigateRefCode() }
                )
            }
        }
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = cardShape
        ) {
            Column {
                ProfileButton(
                    text = stringResource(R.string.profile_rate_us),
                    imageVector = Icons.Filled.StarRate,
                    onClick = { onRateUs() }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.DarkGray
                )
                ProfileButton(
                    text = stringResource(R.string.profile_email_us),
                    imageVector = Icons.Filled.Email,
                    onClick = { onSendEmail() }
                )

            }
        }
        LogOutButton(onClick = { onSignOut() })
        /**
         * Static
         */
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "uid: ${userAuth.value?.userId}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
