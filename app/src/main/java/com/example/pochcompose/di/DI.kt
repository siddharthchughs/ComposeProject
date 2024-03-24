package com.example.pochcompose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.pochcompose.database.HeroesDao
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.setting.ApplicationSetting
import com.example.pochcompose.setting.ApplicationSettingImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences>  by preferencesDataStore(name ="POCH_FILENAME")

@Module
@InstallIn(SingletonComponent::class)
class HeroesAppModule {
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
        "heroes_db"
    )
        .fallbackToDestructiveMigration()
        .build()
}

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule{
    @Singleton
    @Provides
    fun bindDataStore(@ApplicationContext context: Context):DataStore<Preferences>{
        return context.dataStore
    }
}

@Module
@InstallIn(SingletonComponent::class)
 abstract class ApplicationSettingModule {
    @Binds
     abstract fun bindApplicationSetting(applicationSettingImpl: ApplicationSettingImpl):ApplicationSetting

//    @Provides
//    @Singleton
//    fun provideDataStoreOperation(@ApplicationContext context: Context): ApplicationSetting {
//        return ApplicationSettingImpl(context = context)
//    }
}
