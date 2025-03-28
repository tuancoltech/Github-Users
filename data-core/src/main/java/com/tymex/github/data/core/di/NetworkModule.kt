package com.tymex.github.data.core.di

import com.squareup.moshi.Moshi
import com.tymex.github.data.core.BuildConfig
import com.tymex.github.data.core.data.repository.users.remote.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitInstance

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @LoggingInterceptor
    fun providesLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    @HttpClient
    fun providesHttpClient(@LoggingInterceptor loggingInterceptor: HttpLoggingInterceptor)
            : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    @RetrofitInstance
    fun providesRetrofit(@HttpClient okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserService(@RetrofitInstance retrofit: Retrofit)
            : UserService = retrofit.create(UserService::class.java)
}