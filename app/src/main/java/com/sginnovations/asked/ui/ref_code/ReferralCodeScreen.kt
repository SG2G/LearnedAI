package com.sginnovations.asked.ui.ref_code

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.sginnovations.asked.R
import com.sginnovations.asked.viewmodel.AuthViewModel

private const val TAG = "ReferralCodeStateFul"

@Composable
fun ReferralCodeStateFul(
    vmAuth: AuthViewModel,
) {
    val context = LocalContext.current

    val userId = vmAuth.userAuth.collectAsState().value?.userId
    val deepLink = Uri.parse("https://askedai.page.link/?invitedby=$userId")
    val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
        .setLink(deepLink)
        .setDomainUriPrefix("https://askedai.page.link")
        .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
        .buildDynamicLink()

    val dynamicLinkUri = dynamicLink.uri

    Log.i(TAG, "dynamicLink: $dynamicLinkUri")

    ReferralCodeStateLess() {
        sendReferralLink(context, dynamicLinkUri) //TODO VIEWMODEL
    }
}


@Composable
fun ReferralCodeStateLess(
    onInviteFriend: () -> Unit,
) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier.scrollable(scroll, Orientation.Vertical),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.share_invite_friends),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.share_for_you_and_your_friend),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painterResource(id = R.drawable.referral_image_nobackground),
            contentDescription = "referral_image ",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.share_earn_10_tokens_immediately),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.share_are_you_going_to_run_out_of_your_tokens),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { onInviteFriend() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(R.string.share_invite),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.secondaryContainer
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.share_how_to_earn_the_rewards),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.share_share_the_invitation_link_by_clicking_invite_when_your_friend_creates_an_account_both_of_you_will_receive_the_reward_as_simple_as_that),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun sendReferralLink(context: Context, dynamicLinkUri: Uri) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.sharemsg_join_asked_the_best_app_for_easy_learning_use_my_referral_link,
                dynamicLinkUri
            )
        )
        type = "text/plain"
    }
    context.startActivity(sendIntent)
}
