package com.sginnovations.asked.ui.ui_components.chat.messages

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.Constants.Companion.DEFAULT_PROFILE_URL
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.chat.IconUserMsg
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import java.util.regex.Pattern

private const val TAG = "ChatUserMessage"
@Composable
fun ChatUserMessage(
    userName: String?,
    userProfileUrl: String?,

    message: String,

    onSetClip: (String) -> Unit,
) {
    val context = LocalContext.current
    val copyMsg = stringResource(R.string.copy_copied)

    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(
                JLatexMathPlugin.create(
                    42f,
                    JLatexMathPlugin.BuilderConfigure { builder ->
                        builder.inlinesEnabled(true)
                    }
                )
            )
            .build()
    }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (userName != null && userProfileUrl != null) {
            IconUserMsg(userProfileUrl)
        } else { IconUserMsg(DEFAULT_PROFILE_URL) }

        Log.d(TAG, "ChatUserMessage: message: $message")
        val phrase = Constants.MATH_PREFIX_PROMPT
        val markdownText2 = replaceMathTwoBackslashSymbols(removePhrase(message, phrase))
        Log.d(TAG, "ChatUserMessage: markdownText2: $markdownText2")
        val markdownText1 = cleanMathExpression(markdownText2)
        Log.d(TAG, "ChatUserMessage: markdownTex1t: $markdownText1")
        //val markdownText = addDollarSigns(markdownText1)
        //Log.d(TAG, "ChatUserMessage: markdownText: $markdownText")

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CHAT_MSG_PADDING),
            factory = { ctx ->
                TextView(ctx).apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(Color.WHITE)
                    textSize = 14f
                    typeface = ResourcesCompat.getFont(ctx, R.font.monasans_regular)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            },
            update = { view ->
                val node = markwon.parse(markdownText1)
                val renderedMarkdown = markwon.render(node)
                markwon.setParsedMarkdown(view, renderedMarkdown)
            }
        )
    }
}

fun removePhrase(text: String, phrase: String): String {
    return text.replace(phrase, "")
}

fun cleanMathExpression(expression: String): String {
    if (expression.contains("\\\\")) {
        return expression.replace("\\\\", "\\")
    }
    return expression
}

//fun addDollarSigns(text: String): String {
//    return if (!text.contains("$$")) {
//        "$$$text$$"
//    } else {
//        text
//    }
//}

