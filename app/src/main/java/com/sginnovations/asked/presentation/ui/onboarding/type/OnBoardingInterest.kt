package com.sginnovations.asked.presentation.ui.onboarding.type

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingType

data class SelectableInterest(
    val name: String,
    @DrawableRes val icon: Int,
    var selected: Boolean = false
)

@Composable
fun InterestSelection(
    interests: List<SelectableInterest>,
    selectedInterests: List<SelectableInterest>,
    onSelectionChange: (SelectableInterest) -> Unit
) {
    val scrollState = rememberScrollState()

    // Determina si se ha alcanzado el límite de selección.
    val selectionLimitReached = selectedInterests.size >= 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        interests.forEach { interest ->
            val isSelected = interest in selectedInterests
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
                    .clickable { onSelectionChange(interest) },
                shape = RoundedCornerShape(10.dp),
                border = if (isSelected) BorderStroke(
                    2.dp,
                    Color(0xFF155CE9)
                ) else BorderStroke(1.dp, Color(0xFFC0BFBF)),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        // Cambia el color de fondo aquí.
                        .background(if (isSelected) Color(0xFFE6E8F4) else if (selectionLimitReached && !isSelected) Color.LightGray else Color.Transparent)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = interest.icon),
                        contentDescription = "Icon for ${interest.name}",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(interest.name, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        modifier = Modifier.padding(end = 8.dp),
                        checked = isSelected,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF155CE9)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InterestSelectorScreen(
    onBoardingPage: OnBoardingPage,
) {

    val interests = remember {
        listOf(
            SelectableInterest("Art", R.drawable.language_camera),
            SelectableInterest("Music", R.drawable.language_camera),
            SelectableInterest("Sports", R.drawable.language_camera),
            SelectableInterest("dqw", R.drawable.language_camera),
            SelectableInterest("dqwqw", R.drawable.language_camera),
            SelectableInterest("412da", R.drawable.language_camera)
        )
    }
    // State to keep track of selected interests.
    var selectedInterests by remember { mutableStateOf(listOf<SelectableInterest>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Interest",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "select x",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        InterestSelection(
            interests = interests,
            selectedInterests = selectedInterests
        ) { selectedInterest ->
            // Toggle the selected state.
            val currentlySelected = selectedInterest.selected
            val newList: List<SelectableInterest>
            if (currentlySelected) {
                // If already selected, we deselect it.
                newList = selectedInterests - selectedInterest
            } else {
                // If not selected, add it to the list if less than 3 are selected.
                if (selectedInterests.size < 3) {
                    newList = selectedInterests + selectedInterest
                } else {
                    // If we already have 3 selected, return to not select new.
                    return@InterestSelection
                }
            }
            // Update the list for UI.
            selectedInterests = newList
            // Update the individual interest selected state.
            interests.find { it.name == selectedInterest.name }?.selected = !currentlySelected
        }
    }
}

object InterestSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
    override fun getTitle(context: Context) = "Interest"
    override fun getSubTitle(context: Context) = "Select x interest max 3"

}

@Preview(showBackground = true)
@Composable
fun PreviewInterestSelectorScreen() {
    InterestSelectorScreen(InterestSelect)
}
