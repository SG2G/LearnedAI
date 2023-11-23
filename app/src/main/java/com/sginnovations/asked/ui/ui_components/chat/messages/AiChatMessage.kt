package com.sginnovations.asked.ui.ui_components.chat.messages

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import java.util.regex.Pattern

private const val TAG = "ChatAiMessage"
@Composable
fun ChatAiMessage(
    assistantMessage: String,
    haveIcon: Boolean = true,

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

    val backgroundColor = MaterialTheme.colorScheme.primaryContainer

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(backgroundColor)
            .padding(if (haveIcon) 16.dp else 0.dp)
            .fillMaxSize()
    ) {
        if (haveIcon) IconAssistantMsg()

        //val assistantMessageMarkdown = assistantMessage.replace("\n", "<br>")
        Log.d(TAG, "message: $assistantMessage")
        val markdownText = replaceMathOneBackslashSymbols(assistantMessage)

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
                val node = markwon.parse(markdownText)
                val renderedMarkdown = markwon.render(node)
                markwon.setParsedMarkdown(view, renderedMarkdown)
            }
        )
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