package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun DisplayTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val targetWidth = (maxWidth * 0.8f).coerceAtMost(400.dp)
        Row(
            modifier = modifier
                .height(58.dp)
                .width(targetWidth)
                .clip(RoundedCornerShape(24.dp))
                .background(localColorScheme.secondaryContainer)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isEmpty()) {
                Text(
                    text = label,
                    style = typography.bodyMedium.copy(
                        color = localColorScheme.tertiary,
                    ),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = value,
                    style = typography.bodyMedium.copy(
                        color = localColorScheme.secondary,
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}