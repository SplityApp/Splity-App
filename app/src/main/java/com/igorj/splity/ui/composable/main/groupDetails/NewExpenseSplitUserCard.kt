package com.igorj.splity.ui.composable.main.groupDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.model.main.UserInfo
import com.igorj.splity.ui.composable.main.EmojiAvatar
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun NewExpenseSplitUserCard(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    isSelected: Boolean,
    amount: String,
    onSelect: () -> Unit,
    onAmountChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(localColorScheme.background)
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onSelect()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = if (isSelected) {
                Icons.Default.CheckBox
            } else {
                Icons.Default.CheckBoxOutlineBlank
            },
            contentDescription = "Select user",
            tint = localColorScheme.secondary
        )
        EmojiAvatar(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            emoji = userInfo.charImage,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = userInfo.username,
            style = typography.bodyMedium,
            color = localColorScheme.secondary
        )
        BasicTextField(
            modifier = Modifier.width(80.dp),
            value = amount,
            onValueChange = { newAmount ->
                if (newAmount.isEmpty()) {
                    onAmountChange(newAmount)
                    return@BasicTextField
                }
                if (newAmount.toDoubleOrNull() == null || (newAmount.contains(".") && newAmount.substringAfterLast(".").length > 2)) {
                    return@BasicTextField
                }
                val filteredAmount = newAmount.filter { it.isDigit() || it == '.' }
                onAmountChange(filteredAmount)
            },
            textStyle = typography.bodyMedium.copy(
                color = localColorScheme.secondary,
                textAlign = TextAlign.End
            ),
            decorationBox = { innerTextField ->
                if (amount.isEmpty()) {
                    Text(
                        text = "0.0",
                        style = typography.bodyMedium.copy(
                            color = localColorScheme.secondary,
                            textAlign = TextAlign.End
                        ),
                    )
                }
                innerTextField()
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onAny = {
                    onImeAction()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview
@Composable
private fun NewExpenseSplitUserCardPreview() {
    var isSelected by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }

    NewExpenseSplitUserCard(
        userInfo = UserInfo(
            id = "1",
            username = "User",
            charImage = "\uD83E\uDDD1"
        ),
        isSelected = isSelected,
        amount = amount,
        onSelect = { isSelected = !isSelected },
        onAmountChange = { amount = it }
    )
}
