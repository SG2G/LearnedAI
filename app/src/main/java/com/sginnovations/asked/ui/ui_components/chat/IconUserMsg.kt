package com.sginnovations.asked.ui.ui_components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sginnovations.asked.R

///**
// * Photo
// */
//@Composable
//fun IconUserMsg(
//    photoUrl: String,
//) {
//    AsyncImage(
//        model = photoUrl,
//        contentDescription = null,
//        modifier = Modifier
//            .background(Color.Transparent)
//            .clip(CircleShape)
//            .size(32.dp)
//    )
//}

/**
 * Default Photo Photo
 */
@Composable
fun IconAssistantMsg() {
    val image: Painter = painterResource(id = R.drawable.asked_assistant)

    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .background(Color.Transparent)
            .clip(CircleShape)
            .size(32.dp)
    )
}
/**
 * Default Photo Photo
 */
@Composable
fun IconMsg() {
    val image: Painter = painterResource(id = R.drawable.asked30)

    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .background(Color.Transparent)
            .clip(CircleShape)
            .size(32.dp)
    )
}

//    AsyncImage(
//        model = ASSISTANT_PROFILE_URL,
//        contentDescription = null,
//        modifier = Modifier
//            .background(Color.Transparent)
//            .clip(CircleShape)
//            .size(32.dp)
//    )
