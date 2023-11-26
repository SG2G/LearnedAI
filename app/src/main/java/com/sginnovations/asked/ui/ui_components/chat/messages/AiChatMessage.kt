package com.sginnovations.asked.ui.ui_components.chat.messages

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.chat.components.messageOptionsIcon
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    val backgroundColor = MaterialTheme.colorScheme.background

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

        Column {
            ElevatedCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                AndroidView(
                    modifier = Modifier
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

            messageOptionsIcon(
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