package com.example.anujsarawattask.di

import android.content.Context
import androidx.room.Room
import com.example.anujsarawattask.data.local.PortfolioDatabase
import com.example.anujsarawattask.data.local.dao.HoldingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePortfolioDatabase(
        @ApplicationContext context: Context
    ): PortfolioDatabase {
        return Room.databaseBuilder(
            context,
            PortfolioDatabase::class.java,
            "portfolio_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideHoldingDao(database: PortfolioDatabase): HoldingDao {
        return database.holdingDao()
    }
}