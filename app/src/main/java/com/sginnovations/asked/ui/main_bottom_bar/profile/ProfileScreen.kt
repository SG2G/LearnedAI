package com.sginnovations.asked.ui.main_bottom_bar.profile

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.auth.sign_in.UserData
import com.sginnovations.asked.ui.ui_components.profile.DeleteAccountButton
import com.sginnovations.asked.ui.ui_components.profile.LogOutButton
import com.sginnovations.asked.ui.ui_components.profile.ProfilePicture
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
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val intent = remember { (context as Activity).intent }

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
            scope.launch {
                vmAuth.deleteAccount()
            }

        },
        onNavigateRefCode = { onNavigateRefCode() }
    )
}

@Composable
fun StateLessProfile(
    vmToken: TokenViewModel,

    userAuth: State<UserData?>,

    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,

    onNavigateRefCode: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val tokens by vmToken.tokens.collectAsState()

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
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        LogOutButton(
            onClick = { onSignOut() }
        )
        DeleteAccountButton(onClick = { onDeleteAccount() })

        /**
         * Testing
         */
        Text(text = "Tokens: ${tokens}")
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

