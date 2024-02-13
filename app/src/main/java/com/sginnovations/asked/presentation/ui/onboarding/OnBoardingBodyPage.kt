package com.sginnovations.asked.presentation.ui.onboarding

import android.app.Activity
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

@Composable
fun OnBoardingBodyPage(
    onBoardingPage: OnBoardingPage
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = onBoardingPage.getTitle(context),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        if (!onBoardingPage.getSubTitle(context).isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = onBoardingPage.getSubTitle(context),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ImageAnyFormat(
            image = onBoardingPage.getImage(context)
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                val node = markwon.parse(onBoardingPage.getDescription(context))
                val renderedMarkdown = markwon.render(node)
                markwon.setParsedMarkdown(view, renderedMarkdown)
            }
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
fun ImageAnyFormat(
    modifier: Modifier = Modifier,
    image: Int,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = image).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(0.9f),
    )
}