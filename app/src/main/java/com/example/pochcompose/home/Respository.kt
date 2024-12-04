package com.example.pochcompose.home

import androidx.paging.PagingData
import com.example.pochcompose.database.Hero
import com.example.pochcompose.localdatasource.HeroLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Repository{ fun getAllHeroes(): Flow<PagingData<Hero>>
}

class RepositoryImpl @Inject constructor(
    private val  heroLocalDataSource: HeroLocalDataSource
) : Repository{
    override  fun getAllHeroes() :Flow<PagingData<Hero>> =
        heroLocalDataSource.getAllHeroes()
}