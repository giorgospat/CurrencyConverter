package com.patronas.network.config

object NetworkConstants {

    const val connectTimeoutSeconds: Long = 20
    const val readTimeoutSeconds: Long = 20
    const val writeTimeoutSeconds: Long = 20

    const val API_KEY = "422WU5WOmHzeAoIUcaY2vfx0kefyYf7L"

    //Test config
    private const val BASE_URL_TEST = "https://api.apilayer.com/"

    //Live config
    //placeholder for production url. For the needs of this assignment we use the same as test
    private const val BASE_URL_LIVE = "https://api.apilayer.com/"

    fun getTestConfiguration(): NetworkConfiguration {
        return NetworkConfiguration(
            baseUrl = BASE_URL_TEST
        )
    }

    fun getLiveConfiguration(): NetworkConfiguration {
        return NetworkConfiguration(
            baseUrl = BASE_URL_LIVE
        )
    }

}