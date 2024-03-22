package com.example.pochcompose.database

import androidx.paging.PagingSource
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Entity(tableName = "heroes")
data class Hero(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "heroId") val id: Int,
    @ColumnInfo(name = "heroName") val name: String,
    @ColumnInfo(name = "heroImage") val image: String,
    @ColumnInfo(name = "heroAbout") val about: String,
    @ColumnInfo(name = "heroRating") val rating: Double,
    @ColumnInfo(name = "heroPower") val power: Int,
    @ColumnInfo(name = "heroMonth") val month: String,
    @ColumnInfo(name = "heroDay") val day: String,
    @ColumnInfo(name = "heroFamily") val family: List<String>,
    @ColumnInfo(name = "heroAbility") val abilities: List<String>,
    @ColumnInfo(name = "heroTypes") val natureTypes: List<String>
)

@Dao
interface HeroesDao {
    @Query(
        """
        SELECT * FROM heroes order by heroId ASC
    """
    )
    fun getAllHeroes(): PagingSource<Int, Hero>

    @Query(
        """
        SELECT * FROM heroes where heroId = :Id
    """
    )
    fun getHeroById(Id: Int): Hero

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHeroes(heroes: List<Hero>)

    @Query("""DELETE FROM heroes""")
    suspend fun deleteHero()
}

@Entity(tableName = "hero_remote_key")
data class HeroesRemoteKey(
    @PrimaryKey(autoGenerate = true)
    val id: Int, val prevKey: Int?,
    val nextKey: Int
)

@Dao
interface HeroesRemoteDao {
    @Query(
        """
        SELECT  *  FROM hero_remote_key where id = :id
    """
    )
    suspend fun getRemoteKey(id: Int): HeroesRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKey(heroRemoteKey: List<HeroesRemoteKey>)

    @Query("""DELETE FROM hero_remote_key""")
    suspend fun deleteAllRemoteKey()
}

@Database(entities = [Hero::class, HeroesRemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(CustomConverter::class)
abstract class HeroesDatabase : RoomDatabase() {
    abstract fun heroesDao(): HeroesDao
    abstract fun heroesRemoteKeyDao(): HeroesRemoteDao
}
