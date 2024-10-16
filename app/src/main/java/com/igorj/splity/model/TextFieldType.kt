package com.igorj.splity.model

sealed interface TextFieldType {
    data object Common : TextFieldType
    data object Password : TextFieldType
    data object RepeatPassword : TextFieldType
}
