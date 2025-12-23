package com.assignment.john.repository.weather

import okhttp3.Interceptor

fun apiKeyInterceptor(apiKey: String) = Interceptor { chain ->
    val originalRequest = chain.request()
    val originalHttpUrl = originalRequest.url

    // Add the "api_key" query parameter to the URL
    val newUrl = originalHttpUrl.newBuilder()
        .addQueryParameter("appid", apiKey)
        .build()

    // Build the new request with the modified URL
    val newRequest = originalRequest.newBuilder()
        .url(newUrl)
        .build()

    chain.proceed(newRequest)
}
