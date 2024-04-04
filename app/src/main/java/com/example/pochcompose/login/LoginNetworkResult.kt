package com.example.pochcompose.login

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
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface LoginNetworkResult {
    data class LoggedIn(
        val token: String
    ) : LoginNetworkResult

    data class UnAuthorizedError(
        val code: Int
    ) : LoginNetworkResult

    data class UnAuthorizedResponse(
        val message: String,
        val detail: String
    ) : LoginNetworkResult
}


interface LoginNetworkLocalDataSource {
    suspend fun login(
        userAgent: String,
        username: String,
        password: String
    ): LoginNetworkResult
}

interface LoginNetworkClient {
    @Serializable
    data class LoginResponse(
        val token: String
    )

    @POST("/userlogin")
    suspend fun login(
        @Header("User-Agent") userAgent:String,
        @Body loginRequest: LoginRequestModel
    ): Response<LoginResponse>

}

data class LoginRequestModel(
    val username: String,
    val password: String
)

class LoginNetworkLocalDataSourceImpl @Inject constructor(
    private val clientFactory: LoginNetworkClientFactory,
    private val applicationSetting: ApplicationSetting,
    private val dispatcher: CoroutineDispatcher
) : LoginNetworkLocalDataSource {
    override suspend fun login(
        userAgent: String,
        username: String,
        password: String
    ):
            LoginNetworkResult = withContext(dispatcher) {
        try {
            val client =
                clientFactory.createClientFactory(baseUrl = applicationSetting.getBaseUrl().first())
            val requestModel = client.login(
                userAgent = userAgent,
                LoginRequestModel(
                    username = username,
                    password = password
                )
            )

            when {
                requestModel.isSuccessful -> {
                    LoginNetworkResult.LoggedIn(requestModel.body()!!.token)
                }

                requestModel.code() == 401 -> {
                    LoginNetworkResult.UnAuthorizedError(requestModel.code())
                }

                else -> throw IllegalStateException("Unknown error  ::  ${requestModel.code()}")
            }
        } catch (t: Throwable) {
            return@withContext LoginNetworkResult.UnAuthorizedResponse(
                message = t.message ?: "", detail = t.printStackTrace().toString()
            )
        }
    }
}

class LoginNetworkClientFactory @Inject constructor(
) {
    fun createClientFactory(baseUrl: String): LoginNetworkClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.MILLISECONDS)
            .writeTimeout(2, TimeUnit.MINUTES)
            .callTimeout(15, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(LoginNetworkClient::class.java)
    }
}