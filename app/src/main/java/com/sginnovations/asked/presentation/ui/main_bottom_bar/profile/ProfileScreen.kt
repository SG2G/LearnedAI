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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.presentation.ui.ui_components.profile.LogOutButton
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfileButton
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfileName
import com.sginnovations.asked.presentation.ui.ui_components.profile.ProfilePicture
import com.sginnovations.asked.presentation.viewmodel.AuthViewModel
import com.sginnovations.asked.presentation.viewmodel.IntentViewModel
import com.sginnovations.asked.presentation.viewmodel.NotificationViewModel
import com.sginnovations.asked.utils.CheckIsPremium
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "StateFulProfile"

@Composable
fun StateFulProfile(
    vmNotification: NotificationViewModel,
    vmAuth: AuthViewModel,
    vmIntent: IntentViewModel,

    onNavigateUserNotLogged: () -> Unit,

    onNavigateSubscriptions: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userAuth = vmAuth.userAuth.collectAsState()

    StateLessProfile(
        vmNotification = vmNotification,
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

        onNavigateSubscriptions = { onNavigateSubscriptions() }
    )
}

@Composable
fun StateLessProfile(
    vmNotification: NotificationViewModel,
    userAuth: State<UserData?>,

    onSignOut: () -> Unit,
    onSendEmail: () -> Unit,
    onRateUs: () -> Unit,
    onManageSubscription: () -> Unit,

    onNavigateSubscriptions: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val isPremium = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isPremium.value = scope.async { CheckIsPremium.checkIsPremium() }.await() }

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
                    }
                    if (isPremium.value) {
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
                    text = stringResource(R.string.premium),
                    painterResource = painterResource(id = R.drawable.premium_svgrepo_com),
                    onClick = { onNavigateSubscriptions() }
                )
//                Divider(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    color = Color.DarkGray
//                )
//                val showCheckNotificationPermission = remember { mutableStateOf(false) }
//                val notificationPermissionGranted = remember { mutableStateOf(false) }
//
//
//                ProfileButton(
//                    text = "Notification",
//                    imageVector = Icons.Filled.RingVolume,
//                    onClick = {
//                        if (notificationPermissionGranted.value) {
//                            vmNotification.showBasicNotification()
//                        } else {
//                            showCheckNotificationPermission.value = true
//                        }
//                    }
//                )
//                if (showCheckNotificationPermission.value) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        CheckPermissions(
//                            permsAsked = Manifest.permission.POST_NOTIFICATIONS,
//                            permName = "Notification", //TODO TRANSLATE
//                            onPermissionGranted = {
//                                notificationPermissionGranted.value = true
//                                vmNotification.scheduleWaterReminder()
//                            }
//                        )
//                    } else {
//                        notificationPermissionGranted.value = true
//                        vmNotification.scheduleWaterReminder()
//                    }
//                }
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
