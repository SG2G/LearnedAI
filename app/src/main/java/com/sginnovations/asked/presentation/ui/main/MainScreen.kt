package com.sginnovations.asked.presentation.ui.main

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.ParentalAssist
import com.sginnovations.asked.ParentalGuidance
import com.sginnovations.asked.R
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.presentation.ui.utils.StatusBarColorBlue

private val backGroundColor = Color(0xFF3C5AFA)

//FF3C5AFA
@Composable
fun MainScreenStateFul(
    onClick: (String) -> Unit,

    onNavigateProfile: () -> Unit,
    onNavigateSettings: () -> Unit,
) {

    MainScreenStateLess(
        onClick = { onClick(it) },

        onNavigateProfile = { onNavigateProfile() },
        onNavigateSettings = { onNavigateSettings() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenStateLess(
    onClick: (String) -> Unit,

    onNavigateProfile: () -> Unit,
    onNavigateSettings: () -> Unit,
) {
    val context = LocalContext.current

    StatusBarColorBlue()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateProfile()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_svgrepo_filled),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },

                actions = {
                    IconButton(onClick = {
                        onNavigateSettings()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    backGroundColor
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = backGroundColor
        ) {
            BoxWithConstraints {
                val maxWidth = constraints.maxWidth.dp
                val cardSize = maxWidth / 8

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainCard(
                        onClick = onClick,
                        context = context,
                        feature = ChatsHistory,
                        cardSize = cardSize,
                        modifier = Modifier
                            .offset(x = (20).dp)  // Adjust this value as needed
                            .zIndex(1f)
                    )
                    MainCard(
                        onClick = onClick,
                        context = context,
                        feature = ParentalAssist,
                        cardSize = cardSize,
                        modifier = Modifier
                            .zIndex(2f) // Highest zIndex for the middle card
                    )
                    MainCard(
                        onClick = onClick,
                        context = context,
                        feature = ParentalGuidance,
                        cardSize = cardSize,
                        modifier = Modifier
                            .offset(x = (-20).dp)  // Adjust this value as needed
                            .zIndex(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainCard(
    onClick: (String) -> Unit,
    context: Context,

    feature: ScreensDestinations,
    cardSize: Dp,
    modifier: Modifier = Modifier,
) {

    ElevatedCard(
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.background
        ),
//        elevation = CardDefaults.elevatedCardElevation(
//            8.dp
//        ),
        modifier = modifier
            .size(cardSize)  // Aplica el tamaño calculado
            .padding(0.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick(feature.route) })
            }
            .border(5.dp, Color(0xFF3C5AFA), CircleShape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            feature.icon?.let { painterResource(id = it) }?.let {
                Icon(
                    painter = it, contentDescription = null,
                    modifier = Modifier.size(cardSize * 0.25f)
                )
            }
            Spacer(modifier = Modifier.height(cardSize * 0.05f))
            Text(
                text = feature.getBottomName(context),
                modifier = Modifier.padding(horizontal = cardSize * 0.1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreenStateFul(
        onClick = {},
        onNavigateProfile = {},
        onNavigateSettings = {},
    )
}