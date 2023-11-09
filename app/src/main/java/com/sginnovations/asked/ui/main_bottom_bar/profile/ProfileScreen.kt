package com.sginnovations.asked.ui.main_bottom_bar.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.ui.ui_components.profile.LogOutButton
import com.sginnovations.asked.ui.ui_components.profile.ProfileButton
import com.sginnovations.asked.ui.ui_components.profile.ProfileName
import com.sginnovations.asked.ui.ui_components.profile.ProfilePicture
import com.sginnovations.asked.ui.ui_components.tokens.TokenDisplay
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
        onSendEmail = { sendEmail(context, userAuth) },
        onRateUs = { rateUs(context) },

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

    onNavigateRefCode: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
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
                    ProfileName(userAuth.value?.userName.toString())
                    TokenDisplay(
                        modifier = Modifier.scale(0.8f),
                        tokens = tokens,
                        showPlus = false
                    ) { vmToken.switchPointsVisibility() }
                }
                Button(
                    onClick = { onNavigateSubscriptions() },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Upgrade", color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }

        ElevatedCard(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column {
                ProfileButton(
                    text = stringResource(R.string.profile_get_more_tokens),
                    painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
                    onClick = { vmToken.switchPointsVisibility() }
                )
                ProfileButton(
                    text = stringResource(R.string.profile_invite_friends),
                    painterResource = painterResource(id = R.drawable.share_fill0_wght400_grad0_opsz48),
                    onClick = { onNavigateRefCode() }
                )
            }
        }
        ElevatedCard(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column {
                ProfileButton(
                    text = "Rate Us",
                    imageVector = Icons.Filled.StarRate,
                    onClick = { onRateUs() }
                )

                ProfileButton(
                    text = "Email us",
                    imageVector = Icons.Filled.Email,
                    onClick = { onSendEmail() }
                )

            }
        }
        LogOutButton(onClick = { onSignOut() })

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "uid: ${userAuth.value?.userId}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

fun sendEmail(context: Context, userAuth: State<UserData?>) { //TODO INTENT VIEWMODEL?
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.asked_email)))
        putExtra(Intent.EXTRA_SUBJECT, "Your Custom Subject. My uid: ${userAuth.value?.userId}")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
    val packageManager = context.packageManager

    val email = context.getString(R.string.asked_email)
    val subject = "Your Custom Subject. My uid: ${userAuth.value?.userId}"

// Intent for Gmail
    val gmailIntent = Intent(Intent.ACTION_VIEW).apply {
        `package` = "com.google.android.gm"
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }

// Intent for Outlook
    val outlookIntent = Intent(Intent.ACTION_VIEW).apply {
        `package` = "com.microsoft.office.outlook"
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }

// Check if intents can be handled and start activities
    if (gmailIntent.resolveActivity(packageManager) != null) {
        context.startActivity(gmailIntent)
    } else if (outlookIntent.resolveActivity(packageManager) != null) {
        context.startActivity(outlookIntent)
    } else {
        // Fallback to implicit intent if both apps are not installed
        val implicitIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (implicitIntent.resolveActivity(packageManager) != null) {
            context.startActivity(implicitIntent)
        }
    }
}

fun rateUs(context: Context) {
    val uri: Uri = Uri.parse("market://details?id=${context.packageName}")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    context.startActivity(goToMarket)
}

