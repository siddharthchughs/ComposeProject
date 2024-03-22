package com.example.pochcompose.di

import android.content.Context
import androidx.room.Room
import com.example.pochcompose.database.HeroesDao
import com.example.pochcompose.database.HeroesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroesAppModule {
    @Singleton
    @Provides
    fun providerHeroesDao(heroesDatabase: HeroesDatabase): HeroesDao = heroesDatabase.heroesDao()

    @Singleton
    @Provides
    fun providerHeroesAppDatabase(
        @ApplicationContext context: Context
    ): HeroesDatabase = Room.databaseBuilder(
        context,
        HeroesDatabase::class.java,
        "heroes_db")
        .fallbackToDestructiveMigration()
        .build()

}
