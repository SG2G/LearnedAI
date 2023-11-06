package com.sginnovations.asked.ui.newconversation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun NewConversationStateFul(
    vmChat: ChatViewModel,
    vmCamera: CameraViewModel,
    vmAds: AdsViewModel,

    onNavigateChat: (Int) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val text = remember { mutableStateOf("") }
    text.value = vmCamera.imageMath.value // TODO SHOULD SUPPORT THE 2 OR MORE CATEGORIES

    val idConversation = vmChat.idConversation.intValue

    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()


    if (vmCamera.isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

    }

    LaunchedEffect(Unit) {
        vmAds.loadInterstitialAd(context) //TODO MAYBE TOO MUCH WE NEED TO CLEAR CACHE
    }

    NewConversationStateLess(
        text = text,

        onClick = {
            scope.launch {
                if (NetworkUtils.isOnline(context)) {
                    // Show ad
                    if (activity != null) {
                        vmAds.showInterstitialAd(activity)
                    }

                    // GPT call
                    val deferred = async { vmChat.sendMessageToOpenaiApi(text.value) }
                    deferred.await()
                    Log.i("NewConversation", "Continuing the code, Sending $idConversation")

                    onNavigateChat(idConversation)
                } else {
                    Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

}

@Composable
fun NewConversationStateLess(
    text: MutableState<String>,

    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_brain_96),
                    text = "Resolve"
                )
                SubTitleChatUseExample(text = "Take photos of your text or mathematical problems and learn by talking to the chat.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_write_64),
                    text = "Write"
                )
                SubTitleChatUseExample(text = "Give me a summary of the book 'To Kill a Mockingbird'.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_magic_wand_64),
                    text = "Creativity"
                )
                SubTitleChatUseExample(text = "Create a slogan for a dogs social media.")
            }
        }

    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Row(
            modifier = Modifier
                .scale(0.8f)
                .padding(start = 16.dp)
        ) {
            TokenIcon()
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "FREE message")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier
                    .weight(1f)
                    .imePadding(),
                placeholder = { Text(text = "Enter your text.", fontSize = 14.sp) },
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = Int.MAX_VALUE,
            )
            IconButton(
                onClick = { onClick() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun TitleChatUseExample(painterResource: Painter, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text, color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )
    }
}

@Composable
fun SubTitleChatUseExample(text: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = text, color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(
            fontSize = 16.sp
        )
    )
}
