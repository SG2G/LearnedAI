@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.ui_components.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Text

@Composable
fun CameraExamplesDialog(
    onDismissRequest: () -> Unit,

    category: MutableState<String>,
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        if (category.value == Text.root) {
            // Text

            Row(
                modifier = Modifier
                    .scrollable(scrollState, Orientation.Horizontal)
                    .background(
                        Color.Black
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(id = R.drawable.subscription_morechat),
                    contentDescription = null
                )
            }
        } else {
            // Math
            Text(text = "Holka que aps")
        }
    }
}

//@Composable
//fun ScrollableAlertDialog() {
//    val openDialog = remember { mutableStateOf(true) }
//    if (openDialog.value) {
//        AlertDialog(
//            onDismissRequest = { openDialog.value = false },
//            title = { Text(text = "Title") },
//            text = {
//                // Wrap the text with a scrollable Column
//                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//                    Text(text = "Very long content...")
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { openDialog.value = false }) {
//                    Text("Confirm")
//                }
//            }
//        )
//    }
//}
