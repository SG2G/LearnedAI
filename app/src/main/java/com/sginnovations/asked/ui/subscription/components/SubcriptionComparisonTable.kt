package com.sginnovations.asked.ui.subscription.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

data class Feature(
    val name: String,
    val basic: String,
    val premium: String,
)

@Composable
fun SubscriptionComparisonTable(features: List<Feature>) {
    Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
        Text(
            text = stringResource(R.string.subscriptions_need_to_compare),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("", modifier = Modifier.weight(2f), textAlign = TextAlign.Center)
            Text("Basic", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(
                "Premium",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
        //Divider(modifier = Modifier.padding(vertical = 8.dp))
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        features.forEach { feature ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    feature.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    feature.basic,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Check",
                    tint = Color(0xFF469C29),
                    modifier = Modifier.weight(1f)
                )
//                Text(
//                    feature.premium,
//                    style = MaterialTheme.typography.bodySmall,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.weight(1f)
//                )
            }
            //Divider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}