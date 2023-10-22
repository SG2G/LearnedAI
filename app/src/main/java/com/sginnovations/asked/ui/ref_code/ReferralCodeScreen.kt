package com.sginnovations.asked.ui.ref_code

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.sginnovations.asked.viewmodel.AuthViewModel

private const val TAG = "ReferralCodeStateFul"
@Composable
fun ReferralCodeStateFul(
    vmAuth: AuthViewModel
) {
    val context = LocalContext.current

    val userId = vmAuth.userAuth.collectAsState().value?.userId
    Log.i(TAG, "ReferralCodeStateFul: $userId")
    val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
        .setLink(Uri.parse("https://www.askedai.com/?invitedby=$userId"))
        .setDomainUriPrefix("https://askedai.page.link")
        .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
        .buildDynamicLink()
    val dynamicLinkUri = dynamicLink.uri


    ReferralCodeStateLess(
        dynamicLinkUri = dynamicLinkUri

    ) {
        sendReferralLink(context, dynamicLinkUri) //TODO VIEWMODEL
    }
}



@Composable
fun ReferralCodeStateLess( //TODO NEA FIRST TIME YOU CREATE AN ACCOUNT THE POITNS ARE NOT GOOD SETTED.
    dynamicLinkUri: Uri,

    onInviteFriend: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Some photo", color = MaterialTheme.colorScheme.onBackground)
        Text(text = dynamicLinkUri.toString(), color = MaterialTheme.colorScheme.onBackground)

        Button(onClick = { onInviteFriend() }) {
            Text(text = "Invite Friend")
        }
    }
}

fun sendReferralLink(context: Context, dynamicLinkUri: Uri) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "¡Únete a nuestra aplicación! Usa mi enlace de referencia: $dynamicLinkUri")
        type = "text/plain"
    }
    context.startActivity(sendIntent)
}
