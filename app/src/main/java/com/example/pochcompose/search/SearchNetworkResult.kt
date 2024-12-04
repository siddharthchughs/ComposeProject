package com.example.pochcompose.search

import com.example.pochcompose.database.Hero
import com.example.pochcompose.setting.ApplicationSetting
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed interface SearchNetworkResult {
    data class AvailableForSearch(
        val searchHero: SearchNetworkClient.ApiResponseModel
    ) : SearchNetworkResult

    data class UnexpectedError(
        val code: Int
    ) : SearchNetworkResult

    data class UnexpectedResponse(
        val message: String,
        val detail: String
    ) : SearchNetworkResult

}

interface SearchHeroNetworkLocalDataSource {
    suspend fun getHeroesBySearch(
        userAgent: String,
        baseUrl: String,
        name: String
    ): SearchNetworkResult
}

interface SearchNetworkClient {
    @Serializable
    data class ApiResponseModel(
        val success: Boolean,
        val message: String,
        val prevPage: String,
        val nextPage: String,
        val heroes: List<Hero>
    )

    @GET("/search")
    suspend fun searchHeroes(
        @Header("User-Agent") userAgent: String,
        @Header("baseurl") baseUrl: String,
        @Query("name") name: String
    ): Response<ApiResponseModel>

}

class SearchHeroNetworkLocalLocalDataSourceImpl @Inject constructor(
    private val clientFactory: SearchHeroNetworkClientFactory,
    private val applicationSetting: ApplicationSetting,
    private val dispatcher: CoroutineDispatcher
) : SearchHeroNetworkLocalDataSource {
    override suspend fun getHeroesBySearch(
        userAgent: String,
        baseUrl: String,
        name: String
    ): SearchNetworkResult = withContext(dispatcher) {
        try {
            val client = clientFactory.createClientFactory(baseUrl = baseUrl)
            val response = client.searchHeroes(
                userAgent = "User-Agent",
                baseUrl = baseUrl,
                name = name
            )
            return@withContext when {
                response.isSuccessful -> {
                    val result = response.body()!!
                    SearchNetworkResult.AvailableForSearch(result)
                }

                response.code() == 401 -> SearchNetworkResult.UnexpectedError(response.code())

                else -> SearchNetworkResult.UnexpectedResponse(
                    message = response.message(),
                    detail = response.errorBody().toString()
                )
            }
        } catch (t: Throwable) {
            return@withContext SearchNetworkResult.UnexpectedResponse(
                message = t.message ?: "Unknown", detail = t.printStackTrace().toString()
            )
        }
    }
}

class SearchHeroNetworkClientFactory @Inject constructor() {
    fun createClientFactory(baseUrl: String): SearchNetworkClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.MILLISECONDS)
            .callTimeout(35, TimeUnit.SECONDS)
            .build()
        val gsonBuilder = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
        return retrofit.create(SearchNetworkClient::class.java)
    }
}

