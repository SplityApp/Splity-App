package com.igorj.splity.ui.composable.main.groupDetails.expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.igorj.splity.R
import com.igorj.splity.api.AddExpenseRequest
import com.igorj.splity.api.Split
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.model.main.UserInfo
import com.igorj.splity.model.main.expense.ExpenseCategory
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.composable.main.groupDetails.NewExpenseSplitMethodPicker
import com.igorj.splity.ui.composable.main.groupDetails.NewExpenseSplitUserCard
import com.igorj.splity.ui.composable.main.groupDetails.SplitMethod
import com.igorj.splity.ui.composable.main.home.DropdownPicker
import com.igorj.splity.ui.composable.main.home.DropdownPickerData
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExpenseBottomSheet(
    showSheet: Boolean,
    focusManager: FocusManager,
    sheetState: SheetState,
    users: List<UserInfo>,
    groupId: String,
    onDismiss: () -> Unit,
    onAddExpense: (AddExpenseRequest) -> Unit
) {
    if (showSheet) {
        var expenseName by rememberSaveable { mutableStateOf("") }
        var selectedSplitMethod by remember { mutableStateOf(SplitMethod.Equally) }
        var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }
        var selectedPaidBy by remember { mutableStateOf(users.first()) }
        var selectedAmount by remember { mutableDoubleStateOf(0.0) }
        var selectedUsers by remember { mutableStateOf(mapOf<String, Boolean>()) }
        var userAmounts by remember { mutableStateOf(mapOf<String, String>()) }
        var splitError by remember { mutableStateOf<String?>(null) }
        var calculatedSplits by remember { mutableStateOf(mapOf<String, Double>()) }

        fun recalculateSplits() {
            val result = calculateSplit(
                totalAmount = selectedAmount,
                selectedUsers = selectedUsers,
                splitMethod = selectedSplitMethod,
                userAmounts = userAmounts
            )

            when (result) {
                is SplitResult.Success -> {
                    splitError = null
                    calculatedSplits = result.splits
                }
                is SplitResult.Error -> {
                    splitError = result.message
                    calculatedSplits = emptyMap()
                }
            }
        }

        ModalBottomSheet(
            containerColor = localColorScheme.tertiaryContainer,
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            dragHandle = { }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.newExpenseModalBottomSheet_ui_newExpenseTitle),
                    style = typography.headlineLarge,
                    color = localColorScheme.secondary,
                )
                TextField(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    label = stringResource(R.string.newExpenseModalBottomSheet_ui_newExpenseTextFieldHint),
                    value = expenseName,
                    onValueChange = { expenseName = it },
                    type = TextFieldType.Common,
                    onImeAction = { focusManager.clearFocus() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                
                DropdownPicker(
                    label = "Category",
                    menuWidth = 200.dp,
                    availableValues = ExpenseCategory.entries.map {
                        DropdownPickerData(text = it.title, leadingIcon = it.emoji)
                    }.filter { it.text.isNotEmpty() },
                    selectedValue = "${selectedCategory.emoji}  " + selectedCategory.title,
                    onValueSelected = {
                        selectedCategory = ExpenseCategory.entries.first { category ->
                            category.title == it
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.newExpenseModalBottomSheet_ui_paidByAndAmountSectionTitle),
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary,
                )

                DropdownPicker(
                    label = "Paid by",
                    availableValues = users.map { DropdownPickerData(text = it.username, leadingIcon = it.charImage) },
                    selectedValue = "${selectedPaidBy.charImage}  " + selectedPaidBy.username,
                    onValueSelected = {
                        selectedPaidBy = users.first { user ->
                            user.username == it
                        }
                    }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Total paid amount",
                    value = if (selectedAmount == 0.0) "" else selectedAmount.toString(),
                    onValueChange = { newAmount ->
                        if (newAmount.isEmpty()) {
                            selectedAmount = 0.0
                            recalculateSplits()
                            return@TextField
                        }
                        if (newAmount.toDoubleOrNull() == null || (newAmount.contains(".") && newAmount.substringAfterLast(".").length > 2)) {
                            return@TextField
                        }

                        val filteredAmount = newAmount.filter { it.isDigit() || it == '.' }

                        filteredAmount.toDoubleOrNull()?.let {
                            selectedAmount = it
                            recalculateSplits()
                        }
                    },
                    onImeAction = { focusManager.clearFocus() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text(
                    text = stringResource(R.string.newExpenseModalBottomSheet_ui_newExpenseSplitSectionTitle),
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary,
                )

                NewExpenseSplitMethodPicker(
                    modifier = Modifier.padding(vertical = 4.dp),
                    selectedSplitMethod = selectedSplitMethod,
                    onSplitMethodSelected = {
                        selectedSplitMethod = it
                        userAmounts = mutableMapOf()
                        recalculateSplits()
                    }
                )

                Column(
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                ) {
                    users.forEach { user ->
                        NewExpenseSplitUserCard(
                            userInfo = user,
                            isSelected = selectedUsers[user.id] ?: false,
                            amount = userAmounts[user.id] ?: "",
                            calculatedAmount = calculatedSplits[user.id],
                            splitMethod = selectedSplitMethod,
                            onSelect = {
                                selectedUsers = selectedUsers.toMutableMap().apply {
                                    put(user.id, !(selectedUsers[user.id] ?: false))
                                }
                                recalculateSplits()
                            },
                            onAmountChange = { newAmount ->
                                userAmounts = userAmounts.toMutableMap().apply {
                                    put(user.id, newAmount)
                                }
                                recalculateSplits()
                            },
                            onImeAction = { focusManager.clearFocus() }
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        if (splitError == null) {
                            onAddExpense(
                                AddExpenseRequest(
                                    paidBy = selectedPaidBy.id,
                                    groupId = groupId,
                                    description = expenseName,
                                    category = selectedCategory.title,
                                    amount = selectedAmount,
                                    splits = calculatedSplits.map { (userId, amount) ->
                                        Split(userId, amount)
                                    }
                                )
                            )
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (splitError != null) Red else localColorScheme.primary
                    )
                ) {
                    if (splitError != null) {
                        Text(
                            text = splitError!!,
                            style = typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            color = localColorScheme.secondary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.newExpenseModalBottomSheet_ui_newExpenseButtonLabel),
                            style = typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            color = localColorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

fun calculateSplit(
    totalAmount: Double,
    selectedUsers: Map<String, Boolean>,
    splitMethod: SplitMethod,
    userAmounts: Map<String, String>
): SplitResult {
    val selectedUserIds = selectedUsers.filter { it.value }.keys

    if (selectedUserIds.isEmpty()) {
        return SplitResult.Error("No users selected")
    }

    return when (splitMethod) {
        SplitMethod.Equally -> {
            val amountPerPerson = totalAmount / selectedUserIds.size
            SplitResult.Success(selectedUserIds.associateWith { amountPerPerson })
        }

        SplitMethod.Amount -> {
            val amounts = selectedUserIds.mapNotNull { userId ->
                userAmounts[userId]?.toDoubleOrNull()?.let { amount ->
                    userId to amount
                }
            }.toMap()

            if (amounts.size != selectedUserIds.size) {
                return SplitResult.Error("All selected users must have valid amounts")
            }

            val sum = amounts.values.sum()
            if (abs(sum - totalAmount) > 0.01) {
                return SplitResult.Error("Sum of amounts (${String.format("%.2f", sum)}) doesn't match total (${String.format("%.2f", totalAmount)})")
            }

            SplitResult.Success(amounts)
        }

        SplitMethod.Percentage -> {
            val percentages = selectedUserIds.mapNotNull { userId ->
                userAmounts[userId]?.toDoubleOrNull()?.let { percentage ->
                    userId to percentage
                }
            }.toMap()

            if (percentages.size != selectedUserIds.size) {
                return SplitResult.Error("All selected users must have valid percentages")
            }

            val sum = percentages.values.sum()
            if (abs(sum - 100.0) > 0.01) {
                return SplitResult.Error("Percentages must sum to 100%")
            }

            val amounts = percentages.mapValues { (_, percentage) ->
                (percentage / 100.0) * totalAmount
            }

            SplitResult.Success(amounts)
        }
    }
}

sealed class SplitResult {
    data class Success(val splits: Map<String, Double>) : SplitResult()
    data class Error(val message: String) : SplitResult()
}
