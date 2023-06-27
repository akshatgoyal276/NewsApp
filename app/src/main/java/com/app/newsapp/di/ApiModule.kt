package com.app.newsapp.di

import com.app.newsapp.BuildConfig
import com.app.newsapp.MainApplication
import com.app.newsapp.api.ApiService
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = 90L

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    fun interceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api-key", BuildConfig.API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(provideConnectionTimeout(), TimeUnit.SECONDS)
            .readTimeout(provideConnectionTimeout(), TimeUnit.SECONDS)
            .writeTimeout(provideConnectionTimeout(), TimeUnit.SECONDS)
            .addInterceptor(interceptor())
        return if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor(chuckerInterceptor()).build()
        } else okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun chuckerInterceptor(): ChuckerInterceptor {
        val chuckerCollector =
            ChuckerCollector(context = MainApplication.context!!, showNotification = true)
        return ChuckerInterceptor.Builder(MainApplication.context!!).collector(chuckerCollector)
            .alwaysReadResponseBody(true).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}