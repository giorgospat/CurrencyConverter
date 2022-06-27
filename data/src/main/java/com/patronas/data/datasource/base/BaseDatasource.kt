package com.patronas.data.datasource.base

import com.patronas.data.base.DomainApiResult
import com.patronas.network.model.base.BaseApiResponse
import retrofit2.Response

abstract class BaseDatasource {
    fun <T : BaseApiResponse> execute(response: Response<T>): DomainApiResult<T> {

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return DomainApiResult.Success(it)
            } ?: return DomainApiResult.Error()
        } else {
            return DomainApiResult.Error()
        }

    }

}