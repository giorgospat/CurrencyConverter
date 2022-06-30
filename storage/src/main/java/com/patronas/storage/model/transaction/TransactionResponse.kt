package com.patronas.storage.model.transaction

sealed class TransactionResponse {
    object Success : TransactionResponse()
    object Error : TransactionResponse()
}