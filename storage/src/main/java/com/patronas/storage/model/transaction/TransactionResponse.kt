package com.patronas.storage.model.transaction

sealed class TransactionResponse {
    object Success : TransactionResponse()
    class Error(val error: TransactionError) : TransactionResponse()
}