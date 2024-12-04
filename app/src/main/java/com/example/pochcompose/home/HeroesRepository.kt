package com.example.pochcompose.home

import com.example.pochcompose.database.Hero
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.setting.ApplicationSetting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface HeroesRepository {
    suspend fun getAllHeroes() : HeroesNetworkResult
    suspend fun insertIntoHeroes(heroesList :List<Hero>)
}

class HeroesRepositoryImpl @Inject constructor(
    private val heroesNetworkLocalDataSource: HeroesNetworkLocalDataSource,
    private val applicationSetting: ApplicationSetting,
    private val heroesDatabase: HeroesDatabase,
    private val dispatcher: CoroutineDispatcher,

) : HeroesRepository {
    override suspend fun getAllHeroes(): HeroesNetworkResult  = withContext(dispatcher){
     try{
         val heroResponse = heroesNetworkLocalDataSource.getHeroesList(
             userAgent = "user",
             baseUrl = applicationSetting.getBaseUrl().first(),
             page = 1
         )
         return@withContext when (heroResponse) {
             is HeroesNetworkResult.AvailableHeroesList -> {
                 heroResponse

             }
             is HeroesNetworkResult.UnAuthorizedError -> {
                heroResponse
             }

             is HeroesNetworkResult.UnAuthorizedResponse -> {
               heroResponse
             }
         }
     }catch (t:Throwable) {
         return@withContext HeroesNetworkResult.UnAuthorizedResponse(
             message = t.message?:"", detail = t.printStackTrace().toString()
         )
     }

    }

    override suspend fun insertIntoHeroes(heroesList: List<Hero>) {
        heroesDatabase.heroesDao().addHeroes(heroEntities = heroesList)
    }
}
