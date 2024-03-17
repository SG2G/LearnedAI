package com.sginnovations.asked.presentation.ui.onboarding.type

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingMultipleSelection
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel

@Composable
fun InterestSelection(
    interests: List<OnBoardingMultipleSelection>,
    selectedInterests: List<OnBoardingMultipleSelection>,
    onSelectionChange: (OnBoardingMultipleSelection) -> Unit,
) {
    val scrollState = rememberScrollState()

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
                        .background(if (isSelected) Color(0xFFE6E8F4) else if (selectionLimitReached && !isSelected) Color.LightGray else Color.Transparent)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = interest.icon),
                        contentDescription = "Icon for ${interest.name}",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        interest.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start),
                        fontWeight = FontWeight.SemiBold
                    )
//                    Spacer(modifier = Modifier.weight(1f))
                    CircleCheckbox(
                        interest = interest,
                        selected = isSelected

                    ) { onSelectionChange(it) }
//                    Checkbox(
//                        modifier = Modifier.padding(end = 8.dp),
//                        checked = isSelected,
//                        onCheckedChange = null,
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = Color(0xFF155CE9)
//                        )
//                    )
                }
            }
        }
    }
}

@Composable
fun CircleCheckbox(
    interest: OnBoardingMultipleSelection,
    selected: Boolean,
    enabled: Boolean = true,

    onSelectionChange: (OnBoardingMultipleSelection) -> Unit,
) {

    val color = MaterialTheme.colorScheme
    val imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if (selected) color.primary else Color(0xFFCCCCCC)
    val background = if (selected) Color.Transparent else Color.Transparent //Color(0xFFF8F8F8)

    IconButton(
        onClick = { onSelectionChange(interest) },
        enabled = enabled
    ) {
        Icon(
            imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox"
        )
    }
}

@Composable
fun OnBoardingMultipleSelect(
    vmOnBoarding: OnBoardingViewModel,
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current

    val childName = vmOnBoarding.childName

    val options = remember { onBoardingPage.getMultipleOptions(context) }

    var selectedOptions by remember { mutableStateOf(listOf<OnBoardingMultipleSelection>()) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text =
            if (onBoardingPage.getTitle(context) == context.getString(R.string.child_interest_title)) {
                context.getString(
                    R.string.child_interest_title,
                    childName.value.ifEmpty { stringResource(R.string.your_child) }
                )
            } else {
                onBoardingPage.getTitle(context)
            },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = onBoardingPage.getSubTitle(context),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (options != null) {
            InterestSelection(
                interests = options,
                selectedInterests = selectedOptions
            ) { selectedInterest ->
                // Toggle the selected state.
                val currentlySelected = selectedInterest.selected
                val newList: List<OnBoardingMultipleSelection>
                if (currentlySelected) {
                    newList = selectedOptions - selectedInterest
                } else {
                    if (selectedOptions.size < 3) {
                        newList = selectedOptions + selectedInterest
                    } else {
                        return@InterestSelection
                    }
                }
                // Update the list for UI.
                selectedOptions = newList
                // Update the individual interest selected state.
                options.find { it.name == selectedInterest.name }?.selected = !currentlySelected
            }
        }
    }
}

//object InterestSelect : OnBoardingPage {
//    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
//    override fun getTitle(context: Context) = context.getString(R.string.what_are_your_goals)
//    override fun getSubTitle(context: Context) =
//        context.getString(R.string.select_up_to_3_goals_for_more_accurate_customization)
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewInterestSelectorScreen() {
//    OnBoardingInterest(InterestSelect)
//}
