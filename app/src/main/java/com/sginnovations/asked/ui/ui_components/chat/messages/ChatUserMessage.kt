package com.sginnovations.asked.ui.ui_components.chat.messages

import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

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

    // Color
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val backgroundColor = Color.Transparent.toArgb()

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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.End,
    ) {

        Log.d(TAG, "ChatUserMessage: message: $message")
        val phrase = Constants.MATH_PREFIX_PROMPT
        val markdownText2 = replaceMathTwoBackslashSymbols(removePhrase(message, phrase))
        Log.d(TAG, "ChatUserMessage: markdownText2: $markdownText2")
        val markdownText1 = cleanMathExpression(markdownText2)
        Log.d(TAG, "ChatUserMessage: markdownTex1t: $markdownText1")
//            val markdownText = addDollarSigns(markdownText1)
//            Log.d(TAG, "ChatUserMessage: markdownText: $markdownText")

        ElevatedCard(
            modifier = Modifier.padding(start = 32.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xFF4081E4)
            )
        ) {
            AndroidView(
                modifier = Modifier.padding(CHAT_MSG_PADDING),
                factory = { ctx ->
                    TextView(ctx).apply {
                        setBackgroundColor(backgroundColor)
                        setTextColor(textColor)
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
//        if (userName != null && userProfileUrl != null) {
//            IconUserMsg(userProfileUrl)
//        } else {
//            IconUserMsg(DEFAULT_PROFILE_URL)
//        }
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

