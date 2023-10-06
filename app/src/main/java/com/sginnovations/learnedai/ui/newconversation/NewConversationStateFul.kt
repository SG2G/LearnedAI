package com.sginnovations.learnedai.ui.newconversation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConversationStateFul(
    vmChat: ChatViewModel,

    onNavigateChat: (Int) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val idConversation = vmChat.idConversation.intValue
    val isNewConversation = vmChat.isNewConversation.value
    val isLoading = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            scope.launch {
                Log.i(
                    "Testing",
                    "Testings: ${vmChat.idConversation.intValue}"
                )
            }
        }
        ) {
            Text(text = "Get ID")
        }
        Button(onClick = { scope.launch { Log.i("Testing", "ALL DB: ${vmChat.getAlldb()}") } }
        ) {
            Text(text = "DB")
        }
        if (isLoading.value) {
            CircularProgressIndicator()
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
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
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )

        }
    }
}