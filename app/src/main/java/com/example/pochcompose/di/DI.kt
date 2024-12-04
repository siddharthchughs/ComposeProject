package com.example.pochcompose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.pochcompose.database.HeroesDao
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.home.HeroesLocalDataSourceImpl
import com.example.pochcompose.home.HeroesNetworkLocalDataSource
import com.example.pochcompose.home.HeroesRepository
import com.example.pochcompose.home.HeroesRepositoryImpl
import com.example.pochcompose.home.Repository
import com.example.pochcompose.home.RepositoryImpl
import com.example.pochcompose.localdatasource.HeroLocalDataSource
import com.example.pochcompose.localdatasource.HeroLocalDataSourceImpl
import com.example.pochcompose.login.LoginNetworkLocalDataSource
import com.example.pochcompose.login.LoginNetworkLocalDataSourceImpl
import com.example.pochcompose.login.LoginRepository
import com.example.pochcompose.login.LoginRepositoryImpl
import com.example.pochcompose.search.SearchHeroNetworkLocalDataSource
import com.example.pochcompose.search.SearchHeroNetworkLocalLocalDataSourceImpl
import com.example.pochcompose.search.SearchHeroRepository
import com.example.pochcompose.search.SearchHeroRepositoryImpl
import com.example.pochcompose.setting.ApplicationSetting
import com.example.pochcompose.setting.ApplicationSettingImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "POCH_FILENAME")

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
object DataStoreModule {
    @Singleton
    @Provides
    fun bindDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationSettingModule {
    @Binds
    abstract fun bindApplicationSetting(applicationSettingImpl: ApplicationSettingImpl): ApplicationSetting
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {
    @Binds
    abstract fun bindLoginNetworkLocalDataSource(loginNetworkLocalResponseImpl: LoginNetworkLocalDataSourceImpl): LoginNetworkLocalDataSource

    @Binds
    abstract fun bindLoginRepository(loginRepoImpl: LoginRepositoryImpl): LoginRepository
}

@Module
@InstallIn(SingletonComponent::class) // Assuming you're using Hilt
object AppModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}


@Module
@InstallIn(SingletonComponent::class)
abstract class HeroesModule {
    @Binds
    abstract fun bindHeroNetworkLocalDataSource(heroesLocalDataSourceImpl: HeroesLocalDataSourceImpl): HeroesNetworkLocalDataSource

    @Binds
    abstract fun bindHeroesRepositoryImpl(heroesRepositoryImpl: HeroesRepositoryImpl): HeroesRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class HeroesRemoteModule {
    @Binds
    abstract fun bindHeroNetworkLocalDataSource(heroesLocalDataSourceImpl: HeroLocalDataSourceImpl): HeroLocalDataSource
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchHeroesModule {
    @Binds
    abstract fun bindSearchHeroNetworkLocalDataSource(searchHeroNetworkLocalLocalDataSourceImpl: SearchHeroNetworkLocalLocalDataSourceImpl): SearchHeroNetworkLocalDataSource

    @Binds
    abstract fun bindHeroesRepositoryImpl(searchHeroRepositoryImpl: SearchHeroRepositoryImpl): SearchHeroRepository
}
