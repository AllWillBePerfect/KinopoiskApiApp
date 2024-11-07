package com.example.kinopoiskapiapp.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiFactory {
    private var retrofit: Retrofit? = null
    private const val url = "https://api.kinopoisk.dev/v1.4/"
    private const val token = "N63HMHP-4ZDMZR5-KWC7GH6-350TQQZ"
    private fun getClient(baseUrl: String, token: String): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!
    }

    fun getKonopoiskApi(): KinopoiskApi = getClient(url, token).create(KinopoiskApi::class.java)


    class AuthInterceptor(private val authToken: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("X-API-KEY", authToken)
                .build()
            return chain.proceed(newRequest)
        }
    }
}