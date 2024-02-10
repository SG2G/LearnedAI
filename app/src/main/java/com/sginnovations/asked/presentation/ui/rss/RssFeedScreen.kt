package com.sginnovations.asked.presentation.ui.rss

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sginnovations.asked.R
import com.sginnovations.asked.data.rss_dataclass.Article
import com.sginnovations.asked.presentation.viewmodel.RssFeedViewModel
import com.sginnovations.asked.presentation.viewmodel.UiResult
import java.util.Locale

private const val TAG = "RssFeedScreen"

fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}
@Composable
fun RssFeedScreen(vmRss: RssFeedViewModel) {

    /**
     * RSS For Spanish users
     */
    if (Locale.getDefault().language == "es") {
        val uiState = vmRss.uiState.collectAsState().value

        LaunchedEffect(Unit) {
            vmRss.loadArticles("https://www.serpadres.es/rss/educacion.xml")
        }

        when (uiState) {
            is UiResult.Loading -> LoadingScreen()
            is UiResult.Fail -> ErrorView(uiState.error)
            is UiResult.Success -> {
                ArticlesList(articles = uiState.data.articles)
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.coming_soon),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun ArticlesList(articles: List<Article>) {
    Log.d(TAG, "ArticlesList: $articles")
    LazyColumn {
        items(articles) { article ->
            ArticleItem(article)
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Image
            Image(
                painter = rememberAsyncImagePainter(article.imageUrl),
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )

                // Publication Date
                Text(
                    text = stringResource(R.string.published) +" "+ article.pubDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Description
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Read More Button
                OpenLinkButton(url = article.link)
            }
        }
    }
}

@Composable
fun ErrorView(error: Throwable) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.an_error_occurred, error.localizedMessage), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { /* Implement retry logic here */ }) {
//            Text("Retry")
//        }
    }
}

@Composable
fun OpenLinkButton(url: String) {
    val openUrlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                openUrlLauncher.launch(intent)
            },
            modifier = Modifier
                .padding(top = 8.dp),
        ) {
            Text("See more")
        }
    }

}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}


