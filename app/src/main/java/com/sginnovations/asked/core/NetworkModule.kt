package com.sginnovations.asked.core

import com.sginnovations.asked.data.network.MathpixOCRService
import com.sginnovations.asked.data.network.OpenAIApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient { return OkHttpClient.Builder()
        .connectTimeout(40, TimeUnit.SECONDS)  // Increase the connection timeout
        .readTimeout(40, TimeUnit.SECONDS)     // Increase the read timeout
        .writeTimeout(40, TimeUnit.SECONDS)    // Increase the write timeout
        .build() }
    /**
     * Open AI API
     */
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    fun provideOpenAIApiService(retrofit: Retrofit): OpenAIApiService {
        return retrofit.create(OpenAIApiService::class.java)
    }
    /**
     * Mathpix OCR API
     */
    @Provides
    fun provideMathpixOCRService(okHttpClient: OkHttpClient): MathpixOCRService {
        return Retrofit.Builder()
            .baseUrl("https://api.mathpix.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MathpixOCRService::class.java)
    }
}
