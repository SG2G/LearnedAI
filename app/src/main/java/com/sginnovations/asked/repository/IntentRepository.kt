package com.sginnovations.asked.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.State
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.UserData
import javax.inject.Inject

class IntentRepository @Inject constructor() {

    fun sendEmail(context: Context, userAuth: State<UserData?>) {
        //TODO TRANSLATE
        val email = context.getString(R.string.asked_email)
        val subject = context.getString(
            R.string.asked_subject_your_custom_subject_my_uid,
            userAuth.value?.userId
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
        val packageManager = context.packageManager

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
}