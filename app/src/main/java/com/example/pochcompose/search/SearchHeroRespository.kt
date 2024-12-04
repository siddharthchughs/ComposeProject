package com.example.pochcompose.search

import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SearchHeroRepository {
    suspend fun searchByHero(name: String): SearchNetworkResult
}

class SearchHeroRepositoryImpl @Inject constructor(
    private val searchHeroNetworkLocalDataSource: SearchHeroNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val dispatcher: CoroutineDispatcher
) : SearchHeroRepository {
    override suspend fun searchByHero(name: String): SearchNetworkResult =
        withContext(dispatcher) {
            try {
                val response = searchHeroNetworkLocalDataSource.getHeroesBySearch(
                    userAgent = "User",
                    baseUrl = applicationSetting.getBaseUrl().first(),
                    name = name
                )
                return@withContext when (response) {
                    is SearchNetworkResult.AvailableForSearch -> {
                        response
                    }

                    is SearchNetworkResult.UnexpectedError -> {
                        response
                    }

                    is SearchNetworkResult.UnexpectedResponse ->
                        response
                }
            } catch (t: Throwable) {
              return@withContext  SearchNetworkResult.UnexpectedResponse(
                    message = t.message ?: "Unknown",
                    detail = ""
                )
            }
        }
}