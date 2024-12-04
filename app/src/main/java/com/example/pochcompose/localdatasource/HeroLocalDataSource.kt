package com.example.pochcompose.localdatasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pochcompose.database.Hero
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.home.HeroesNetworkLocalDataSource
import com.example.pochcompose.homepagingsource.HeroRemoteMediator
import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HeroLocalDataSource {
    fun getAllHeroes(): Flow<PagingData<Hero>>
}

class HeroLocalDataSourceImpl @Inject constructor(
    private val heroesNetworkLocalDataSource: HeroesNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val heroesDatabase: HeroesDatabase
) : HeroLocalDataSource {
    private val heroesDao = heroesDatabase.heroesDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllHeroes(): Flow<PagingData<Hero>> {
        val pagingSourceFactory = { heroesDao.getAllHeroes() }
        return Pager(
            config = PagingConfig(pageSize = 3),
            remoteMediator = HeroRemoteMediator(
                heroesNetworkLocalDataSource = heroesNetworkLocalDataSource,
                applicationSetting = applicationSetting,
                heroesDatabase = heroesDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}