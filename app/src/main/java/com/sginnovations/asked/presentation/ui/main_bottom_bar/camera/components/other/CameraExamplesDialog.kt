@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR

val mathImageList = listOf(
    R.drawable.math_example_1, R.drawable.math_example_2,
    R.drawable.math_example_3, R.drawable.math_example_4,
    R.drawable.math_example_5, R.drawable.math_example_6,
    R.drawable.math_example_7, R.drawable.math_example_8,
    R.drawable.math_example_9, R.drawable.math_example_10,
    R.drawable.math_example_12, R.drawable.math_example_13,
    R.drawable.math_example_11, R.drawable.math_example_14,
    R.drawable.math_example_15, R.drawable.math_example_16,
    R.drawable.math_example_17, R.drawable.math_example_18,
)
val textImageList = listOf(
    R.drawable.math_example_1,
)

@Composable
fun CameraExamplesDialog(
    onDismissRequest: () -> Unit,

    cameraCategoryOCR: MutableState<CategoryOCR>,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        if (cameraCategoryOCR.value.root == TextCategoryOCR.root) {
            // Text
            ElevatedCard(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.camera_examples_example_of_text_based_problems),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    for (i in textImageList.indices step 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            for (j in i until minOf(i + 3, textImageList.size)) {
                                Image(
                                    painter = painterResource(id = textImageList[j]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Math
            ElevatedCard(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.camera_examples_example_of_math_problems),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    for (i in mathImageList.indices step 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            for (j in i until minOf(i + 3, mathImageList.size)) {
                                Image(
                                    painter = painterResource(id = mathImageList[j]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
