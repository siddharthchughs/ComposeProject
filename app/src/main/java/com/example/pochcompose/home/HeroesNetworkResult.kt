package com.example.pochcompose.home

import com.example.pochcompose.database.Hero
import com.example.pochcompose.setting.ApplicationSetting
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
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
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed interface HeroesNetworkResult {
    data class AvailableHeroesList(
        val heroesResponse: HeroesNetworkClient.ApiResponseModel
    ) : HeroesNetworkResult
    data class UnAuthorizedError(
        val code: Int
    ) : HeroesNetworkResult

    data class UnAuthorizedResponse(
        val message: String,
        val detail: String
    ) : HeroesNetworkResult
}

interface HeroesNetworkLocalDataSource {
    suspend fun getHeroesList(
        userAgent: String,
        baseUrl: String,
        page: Int
    ): HeroesNetworkResult
}

interface HeroesNetworkClient {

    @Serializable
    data class ApiResponseModel(
        val success: Boolean,
        val prevPage:Int,
        val nextPage:Int,
        val heroes : List<Hero>
    )

    @GET("/downloadContacts")
    suspend fun getAllHeroes(
        @Header("User-Agent") userAgent: String,
        @Header("baseurl") baseUrl: String,
        @Query("page") page: Int = 1
    ): Response<ApiResponseModel>
}

class HeroesLocalDataSourceImpl @Inject constructor(
    val clientNetworkFactory: HeroesClientNetworkFactory,
    val applicationSetting: ApplicationSetting,
    val dispatcher: CoroutineDispatcher
) : HeroesNetworkLocalDataSource {
    override suspend fun getHeroesList(
        userAgent: String,
        baseUrl: String,
        page: Int
    ): HeroesNetworkResult = withContext(dispatcher) {
        try {
            val client = clientNetworkFactory.clientFactory(applicationSetting.getBaseUrl().first())
            val response = client.getAllHeroes(
                userAgent = "User-Agent",
                baseUrl = baseUrl,
                page = page
            )

            when {
                response.isSuccessful -> {
                    val heroList = response.body()!!
                    Timber.i("Response :: ${heroList.heroes}")
                    HeroesNetworkResult.AvailableHeroesList(heroList)
                }

                response.code() == 401 -> {
                    HeroesNetworkResult.UnAuthorizedError(response.code())
                }

                else -> throw IllegalStateException("Unhandled  response code :: ${response.code()}")
            }

        } catch (t: Throwable) {
            return@withContext HeroesNetworkResult.UnAuthorizedResponse(
                message = t.message ?: "Unknown", detail = t.printStackTrace().toString()
            )
        }
    }
}

class HeroesClientNetworkFactory @Inject constructor(){
    fun clientFactory(baseUrl: String): HeroesNetworkClient {
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
        return retrofit.create(HeroesNetworkClient::class.java)
    }
}
