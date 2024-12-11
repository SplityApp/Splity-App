package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

data class DropdownPickerData(
    val leadingIcon: String? = null,
    val text: String,
)

@Composable
fun DropdownPicker(
    label: String,
    menuWidth: Dp = 120.dp,
    availableValues: List<DropdownPickerData>,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    expanded = !expanded
                }
        ) {
            DisplayTextField(
                modifier = Modifier.fillMaxWidth(),
                label = label,
                value = selectedValue,
            )
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
                    .rotate(if (expanded) 180f else 0f),
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = label,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(localColorScheme.secondaryContainer)
                .width(menuWidth)
        ) {
            availableValues.forEach { value ->
                DropdownMenuItem(
                    leadingIcon = {
                        Text(text = value.leadingIcon ?: "")
                    },
                    text = {
                        Text(
                            text = value.text,
                            style = typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        onValueSelected(value.text)
                        expanded = false
                    }
                )
            }
        }
    }
}