package com.sginnovations.asked.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.sign_in.UserData
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
                onNavigateRefCode = { onNavigateRefCode() }
    )
}

@Composable
fun StateLessProfile(
    vmToken: TokenViewModel,

    userAuth: State<UserData?>,

    onSignOut: () -> Unit,

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


        Text(text = "Tokens: ${tokens}")
        Button(onClick = {
            scope.launch {
                vmToken.oneLessToken()
            }
        }
        ) {
            Text(text = "Rest 1")
        }

        LogOutButton(
            onClick = { onSignOut() }
        )
        Button(onClick = { onSignOut() }) {
            Text(text = "Sign Out")
        }
        Button(onClick = { vmToken.testGivePoints() }) {
            Text(text = "Give Tokens")
        }
        Button(onClick = { onNavigateRefCode() }) {
            Text(text = "Ref coded")
        }
    }
}

@Composable
fun ProfilePicture(userAuthPhotoUrl: String?) {
    Box(
        modifier = Modifier
            .size(width = 65.dp, height = 65.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        AsyncImage(
            model = userAuthPhotoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun LogOutButton(
    onClick: () -> Unit,
) {
    var showConfirmation by remember { mutableStateOf(false) }

    if (showConfirmation) {
        ConfirmActionDialog(
            onConfirm = { onClick() },
            onDismiss = { showConfirmation = false }
        )
    }

    TextButton(
        onClick = {
            Log.i(TAG, "LogOutButton: Log Out Button Clicked")
            showConfirmation = !showConfirmation
        },
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout_fill0_wght400_grad0_opsz48),
                contentDescription = "ExitToApp",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp)
            )
            Text(
                text = "Log out",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
            )
        }
    }
}

@Composable
fun ConfirmActionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Action", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text(text = "Are you sure you want to Log Out?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Log out")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}