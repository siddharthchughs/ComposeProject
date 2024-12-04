@file:OptIn(ExperimentalPagingApi::class)
@file:Suppress("KotlinConstantConditions")

package com.example.pochcompose.homepagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pochcompose.database.Hero
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.database.HeroesRemoteKeys
import com.example.pochcompose.home.HeroesNetworkLocalDataSource
import com.example.pochcompose.home.HeroesNetworkResult
import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class HeroRemoteMediator @Inject constructor(
    private val heroesNetworkLocalDataSource: HeroesNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val heroesDatabase: HeroesDatabase
) : RemoteMediator<Int, Hero>() {

    private val heroDao = heroesDatabase.heroesDao()
    private val heroesRemoteKeys = heroesDatabase.heroesRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Hero>
    ): MediatorResult {
        return try {

            val page = when (loadType) {
                /*    this operation is triggered due to referesh action
                *  */
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPage(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                // new load operation is triggered to load the data at the beginning
                // of the data set.
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey ?: MediatorResult.Success(
                         endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                // new load operations is triggered to load the data at the end of the dataset
                //this operation is quite common as and when the user scrolls the screen
                // the data os loaded in the UI.
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            val response = heroesNetworkLocalDataSource.getHeroesList(
                userAgent = "user-agent",
                baseUrl = applicationSetting.getBaseUrl().first().toString(),
                page = page as Int
            )

            when (response) {
                is HeroesNetworkResult.AvailableHeroesList -> {
                    heroesDatabase.withTransaction {
                        when (loadType) {
                            LoadType.REFRESH -> {

                                heroDao.deleteHero()
                                heroesRemoteKeys.deleteAllRemoteKey()
                                val prevPage = response.heroesResponse.prevPage
                                val nextPage = response.heroesResponse.nextPage
                                val keys = response.heroesResponse.heroes.map {
                                    HeroesRemoteKeys(
                                        id = it.id,
                                        prevKey = prevPage,
                                        nextKey = nextPage
                                    )
                                }
                                heroesRemoteKeys.addAllRemoteKey(keys)
                                heroDao.addHeroes(heroEntities = response.heroesResponse.heroes)
                            }

                            else -> {
                                Timber.i("Type of lad does not exist: $loadType")
                            }
                        }
                    }
                }

                else -> {
                    Timber.i("Type of lad does not exist: $response")
                }
            }
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPage(state: PagingState<Int, Hero>): HeroesRemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                heroesRemoteKeys.getRemoteKeys(id = id)
            }
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Hero>): HeroesRemoteKeys? =
        state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { hero ->
            heroesRemoteKeys.getRemoteKeys(hero.id)
        }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Hero>): HeroesRemoteKeys? =
        state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { hero ->
            heroesRemoteKeys.getRemoteKeys(hero.id)
        }
}