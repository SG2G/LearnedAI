package com.sginnovations.asked.presentation.ui.onboarding.type

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.ui.onboarding.components.ImageAnyFormat
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

@Composable
fun OnBoardingDefault(
    onBoardingPage: OnBoardingPage,
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
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val textSizee = MaterialTheme.typography.bodyMedium.fontSize.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = onBoardingPage.getTitle(context),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        if (!onBoardingPage.getSubTitle(context).isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = onBoardingPage.getSubTitle(context),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        onBoardingPage.getImage(context)?.let {
            ImageAnyFormat(
                image = it
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!onBoardingPage.getDescription(context).isNullOrEmpty()) {
            AndroidView(
                modifier = Modifier
                    .padding(Constants.CHAT_MSG_PADDING),
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
                    val node = onBoardingPage.getDescription(context)?.let { markwon.parse(it) }
                    val renderedMarkdown = node?.let { markwon.render(it) }
                    if (renderedMarkdown != null) {
                        markwon.setParsedMarkdown(view, renderedMarkdown)
                    }
                }
            )
        } else {
            val features = onBoardingPage.getFeatures(context)

            Column(
                modifier = Modifier
                    .padding(Constants.CHAT_MSG_PADDING),
            ) {
                if (features != null) {
                    for (feature in features) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = feature.icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = feature.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = feature.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}