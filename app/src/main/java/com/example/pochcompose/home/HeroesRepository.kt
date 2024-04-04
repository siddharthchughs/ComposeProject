package com.example.pochcompose.home

import com.example.pochcompose.database.Hero
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface HeroesRepository {
    suspend fun getAllHeroes(): HeroesNetworkResult
    suspend fun saveAllHeroes(heroes:List<Hero>)

}

class HeroesRepositoryImpl @Inject constructor(
    private val heroesNetworkLocalDataSource: HeroesNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val heroesDatabase: HeroesDatabase
) : HeroesRepository {
    override suspend fun getAllHeroes(): HeroesNetworkResult  =
        heroesNetworkLocalDataSource.getHeroesList(
            userAgent = "user",
            baseUrl = applicationSetting.getBaseUrl().first()
        )

    override suspend fun saveAllHeroes(heroes: List<Hero>) {
    }
}