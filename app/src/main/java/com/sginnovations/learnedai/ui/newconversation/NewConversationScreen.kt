package com.sginnovations.learnedai.ui.newconversation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.viewmodel.CameraViewModel
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConversationStateFul(
    vmChat: ChatViewModel,
    vmCamera: CameraViewModel,

    onNavigateChat: (Int) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val idConversation = vmChat.idConversation.intValue
    val isNewConversation = vmChat.isNewConversation.value

    LaunchedEffect(vmCamera.imageText.value) {
        text.value = vmCamera.imageText.value
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = Int.MAX_VALUE
            )
            IconButton(
                onClick = {
                    scope.launch {
                        val deferred = async { vmChat.sendMessageToOpenaiApi(text.value) }
                        deferred.await()

                        Log.i("NewConversation", "Continuing the code")

                        if (isNewConversation) {
                            vmChat.isNewConversation.value = false
                            Log.i("NewConversation", "Sending ${idConversation}")
                            onNavigateChat(idConversation)
                        }
                    }
                },
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