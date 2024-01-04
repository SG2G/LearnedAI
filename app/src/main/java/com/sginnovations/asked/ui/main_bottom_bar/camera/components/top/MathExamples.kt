package com.sginnovations.asked.ui.main_bottom_bar.camera.components.top

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sginnovations.asked.R

@Composable
fun MathExamples(
    onShowCategoryExamples: () -> Unit,
) {
    Text(
        text = stringResource(R.string.camera_math_examples),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable { onShowCategoryExamples() }
    )
}