package com.patronas.data.base

sealed class DomainApiResult<Data> {
    class Success<Data>(var data: Data) : DomainApiResult<Data>()
    class Error<Data> : DomainApiResult<Data>()
}