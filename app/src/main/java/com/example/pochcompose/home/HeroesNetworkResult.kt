package com.example.pochcompose.home

import javax.inject.Inject

sealed interface HeroesNetworkResult {

}

interface HeroesNetworkLocalDataSource{
    suspend fun getHeroesList()
}

interface HeroesNetworkClient{

}

class HeroesLocalDataSourceImpl @Inject constructor(

): HeroesNetworkLocalDataSource{
    override suspend fun getHeroesList() {

    }
}

class HeroesClientNetworkFactory(baseUrl:String):HeroesNetworkClient{

}

