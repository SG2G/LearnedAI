package com.sginnovations.asked.presentation.ui.ui_components.chat.messages

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.chat.components.messageOptionsIcon
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconMsg
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

private const val TAG = "ChatAiMessage"

@Composable
fun ChatAiMessage(
    assistantMessage: String,
    isAssistant: Boolean = false,

    onReportMessage: (String) -> Unit,
    onSetClip: (String) -> Unit,
) {
    val context = LocalContext.current

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

    val backgroundColor = MaterialTheme.colorScheme.background

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (isAssistant) IconAssistantMsg() else IconMsg()

        //val assistantMessageMarkdown = assistantMessage.replace("\n", "<br>")
        Log.d(TAG, "message: $assistantMessage")
        val markdownText = replaceMathOneBackslashSymbols(assistantMessage)
        val markdownText2 = markdownText

        val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
        val textSizee = MaterialTheme.typography.bodyMedium.fontSize.value

        Column {
            ElevatedCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                AndroidView(
                    modifier = Modifier
                        .padding(CHAT_MSG_PADDING),
                    factory = { ctx ->
                        TextView(ctx).apply {
                            setBackgroundColor(Color.TRANSPARENT)
                            setTextColor(textColor)
                            textSize = textSizee
                            typeface = ResourcesCompat.getFont(ctx, R.font.monasans_regular)
                            movementMethod = LinkMovementMethod.getInstance()
                        }
                    },
                    update = { view ->
                        val node = markwon.parse(markdownText2)
                        val renderedMarkdown = markwon.render(node)
                        markwon.setParsedMarkdown(view, renderedMarkdown)
                    }
                )
            }

            messageOptionsIcon(
                onReportMessage = {
                    onReportMessage(markdownText)
                },
                onSetClip = {
                    onSetClip(markdownText)
                }
            )

        }
    }

}

/**
 * Replace "\(" to "$$"
 */
fun replaceMathOneBackslashSymbols(text: String): String {
    return text.replace("\\(", "$$").replace("\\)", "$$").replace("\\[", "$$").replace("\\]", "$$")
}

/**
 * Replace "\\(" to "$$"
 */
fun replaceMathTwoBackslashSymbols(text: String): String {
    return text.replace("\\\\(", "$$").replace("\\\\)", "$$")
}

/**
 * Replace "\\" to "\"
 */
//fun cleanMathExpression(expression: String): String {
//    if (expression.contains("\\\\")) {
//        return expression.replace("\\\\", "\\")
//    }
//    return expression
//}
//.replace("$$\\(", "$$").replace("\\)$$", "$$")

/**
 * Works for block
 */