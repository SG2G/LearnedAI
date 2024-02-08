package com.sginnovations.asked.core

import com.sginnovations.asked.data.network.CloudService
import com.sginnovations.asked.data.network.MathpixOCRService
import com.sginnovations.asked.data.network.OpenAIApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a customized OkHttpClient instance.
     */
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)  // Increase the connection timeout
            .readTimeout(40, TimeUnit.SECONDS)     // Increase the read timeout
            .writeTimeout(40, TimeUnit.SECONDS)    // Increase the write timeout
            .build()
    }

    /**
     * Provides a Ktor HttpClient instance using the OkHttp engine.
     */
    @Provides
    fun provideKtorHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Example configuration
                    // Add other configurations as needed
                })
            }
        }
    }

    /**
     * Provides the CloudService using the HttpClient instance.
     */
    @Provides
    fun provideCloudService(httpClient: HttpClient): CloudService {
        return CloudService(httpClient)
    }

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
