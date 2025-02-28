package com.saudigitus.support_module.data.models.erros

import java.util.Date

data class ErrorModel(
    val creationDate: Date?,
    val errorCode: String?,
    val errorDescription: String?,
    val errorComponent: String?,
    var isSelected: Boolean = false,
)
