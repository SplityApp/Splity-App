package com.igorj.splity.ui.composable.main.groupDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun NewExpenseSplitMethodPicker(
    modifier: Modifier = Modifier,
    selectedSplitMethod: SplitMethod,
    onSplitMethodSelected: (SplitMethod) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(localColorScheme.secondaryContainer)
            .height(42.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedSplitMethod == SplitMethod.Equally) {
                        localColorScheme.primary
                    } else {
                        localColorScheme.secondaryContainer
                    }
                )
                .clickable {
                    onSplitMethodSelected(SplitMethod.Equally)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Equally",
                style = typography.bodyMedium,
                color = localColorScheme.secondary
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedSplitMethod == SplitMethod.Amount) {
                        localColorScheme.primary
                    } else {
                        localColorScheme.secondaryContainer
                    }
                )
                .clickable {
                    onSplitMethodSelected(SplitMethod.Amount)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Amount",
                style = typography.bodyMedium,
                color = localColorScheme.secondary
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedSplitMethod == SplitMethod.Percentage) {
                        localColorScheme.primary
                    } else {
                        localColorScheme.secondaryContainer
                    }
                )
                .clickable {
                    onSplitMethodSelected(SplitMethod.Percentage)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Percentage",
                style = typography.bodyMedium,
                color = localColorScheme.secondary
            )
        }
    }
}

enum class SplitMethod {
    Equally,
    Amount,
    Percentage
}

@Preview
@Composable
private fun NewExpenseSplitMethodPickerPreview() {
    var selectedSplitMethod by remember {
        mutableStateOf(SplitMethod.Equally)
    }

    NewExpenseSplitMethodPicker(
        selectedSplitMethod = selectedSplitMethod,
        onSplitMethodSelected = { selectedSplitMethod = it }
    )
}
